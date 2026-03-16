// Monolith deploy - scripted pipeline because it predates declarative syntax adoption
node {
    def environment = params.ENVIRONMENT
    def version = params.VERSION
    def isRollback = params.ROLLBACK

    // Hardcoded server IPs per environment - yes, really
    def servers = [
        dev: ['10.0.1.50', '10.0.1.51'],
        staging: ['10.0.2.50', '10.0.2.51', '10.0.2.52'],
        prod: ['10.0.3.50', '10.0.3.51', '10.0.3.52', '10.0.3.53']
    ]

    try {
        stage('Validate') {
            if (!version) {
                error "VERSION is required"
            }
            echo "Deploying monolith v${version} to ${environment}"
        }

        if (environment == 'prod') {
            stage('Production Approval') {
                input message: "Deploy v${version} to PRODUCTION?",
                      submitter: 'admin,garcia,jenkins-admins',
                      parameters: [
                          booleanParam(name: 'CONFIRMED', defaultValue: false, description: 'I confirm this production deployment')
                      ]
            }
        }

        stage('Deploy') {
            def targetServers = servers[environment]
            for (server in targetServers) {
                echo "Deploying to ${server}..."
                sshagent(['deploy-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no deploy@${server} '
                            cd /opt/monolith &&
                            docker pull 145023098958.dkr.ecr.us-east-2.amazonaws.com/redknot-monolith:${version} &&
                            docker-compose down &&
                            export IMAGE_TAG=${version} &&
                            docker-compose up -d
                        '
                    """
                }
            }
        }

        stage('Health Check') {
            def targetServers = servers[environment]
            for (server in targetServers) {
                retry(3) {
                    sh "curl -sf http://${server}:8080/health || (sleep 10 && exit 1)"
                }
            }
        }

        currentBuild.description = "${environment}: v${version}"
    } catch (Exception e) {
        currentBuild.result = 'FAILURE'
        throw e
    } finally {
        slackSend(
            channel: '#legacy-builds',
            color: currentBuild.result == 'FAILURE' ? 'danger' : 'good',
            message: "Monolith deploy ${environment} v${version}: ${currentBuild.result ?: 'SUCCESS'}"
        )
    }
}
