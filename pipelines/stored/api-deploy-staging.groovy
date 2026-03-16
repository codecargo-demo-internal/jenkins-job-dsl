// Copy-paste of api-deploy-dev.groovy with staging values
// TODO: parameterize this properly (JIRA-5102)
pipeline {
    agent any
    stages {
        stage('Validate') {
            steps {
                script {
                    if (!params.VERSION) {
                        error "VERSION parameter is required"
                    }
                }
            }
        }
        stage('Deploy to Staging') {
            steps {
                withCredentials([string(credentialsId: 'aws-staging-credentials', variable: 'AWS_ACCESS_KEY_ID'),
                                 string(credentialsId: 'aws-staging-credentials-secret', variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        export AWS_DEFAULT_REGION=us-east-2
                        kubectl set image deployment/api-service \
                            api-service=234189402038.dkr.ecr.us-east-2.amazonaws.com/api-service:${params.VERSION} \
                            -n default \
                            --record
                        kubectl rollout status deployment/api-service -n default --timeout=300s
                    """
                }
            }
        }
        stage('Smoke Test') {
            when { expression { !params.SKIP_TESTS } }
            steps {
                sh 'curl -sf http://api-service.default.svc:8080/health || exit 1'
            }
        }
    }
    post {
        success {
            slackSend(channel: '#deployments', color: 'good', message: "API ${params.VERSION} deployed to staging")
        }
        failure {
            slackSend(channel: '#platform-alerts', color: 'danger', message: "API deploy to staging FAILED: ${params.VERSION}")
        }
    }
}
