pipelineJob('Platform/Operator/operator-deploy') {
    displayName('Operator Deploy')
    description('Deploy operator via Helm upgrade')

    parameters {
        choiceParam('ENVIRONMENT', ['dev', 'staging', 'prod'], 'Target environment')
        stringParam('VERSION', '', 'Chart version to deploy')
        booleanParam('DRY_RUN', true, 'Helm dry-run only')
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
                    sh \"\"\"                        helm upgrade --install operator oci://145023098958.dkr.ecr.us-east-2.amazonaws.com/charts/operator \\
                            --version \${params.VERSION} \\
                            --namespace operators \\
                            --set image.tag=\${params.VERSION} \\
                            \${params.DRY_RUN ? '--dry-run' : ''}
                    \"\"\"
                }
            }
        }
    }
    post {
        success {
            slackSend(channel: '#deployments', color: 'good', message: "Operator \${params.VERSION} deployed to \${params.ENVIRONMENT}")
        }
    }
}
""".stripIndent())
            sandbox()
        }
    }
}
