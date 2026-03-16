// Copy-paste from Platform/API deploy with different service name and port
pipelineJob('Mobile/bff-api-deploy') {
    displayName('Mobile BFF Deploy')
    description('Deploy mobile BFF service')

    parameters {
        choiceParam('ENVIRONMENT', ['dev', 'staging'], 'Target environment')
        stringParam('VERSION', '', 'Version to deploy')
    }

    definition {
        cps {
            script("""
pipeline {
    agent any
    stages {
        stage('Deploy') {
            steps {
                withCredentials([string(credentialsId: "aws-\${params.ENVIRONMENT}-credentials", variable: 'AWS_KEY')]) {
                    sh \"\"\"                        kubectl set image deployment/mobile-bff mobile-bff=145023098958.dkr.ecr.us-east-2.amazonaws.com/mobile-bff:\${params.VERSION} \\
                            -n mobile \\
                            --record
                        kubectl rollout status deployment/mobile-bff -n mobile --timeout=300s
                    \"\"\"
                }
            }
        }
        stage('Smoke Test') {
            steps {
                sh 'curl -sf http://mobile-bff.mobile.svc:3001/health || exit 1'
            }
        }
    }
    post {
        success {
            slackSend(channel: '#mobile-team', color: 'good', message: "BFF \${params.VERSION} deployed to \${params.ENVIRONMENT}")
        }
        failure {
            slackSend(channel: '#mobile-team', color: 'danger', message: "BFF deploy FAILED for \${params.VERSION} to \${params.ENVIRONMENT}")
        }
    }
}
""".stripIndent())
            sandbox()
        }
    }
}
