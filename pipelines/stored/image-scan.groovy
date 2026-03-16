pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }
    stages {
        stage('Scan Images') {
            steps {
                script {
                    def images = params.IMAGE ? [params.IMAGE] : [
                        '145023098958.dkr.ecr.us-east-2.amazonaws.com/api-service:latest',
                        '145023098958.dkr.ecr.us-east-2.amazonaws.com/web-app:latest',
                        '145023098958.dkr.ecr.us-east-2.amazonaws.com/mobile-bff:latest',
                        '145023098958.dkr.ecr.us-east-2.amazonaws.com/redknot-monolith:latest',
                        '145023098958.dkr.ecr.us-east-2.amazonaws.com/data-pipeline:latest'
                    ]

                    def results = [:]
                    for (img in images) {
                        catchError(buildResult: 'UNSTABLE', stageResult: 'UNSTABLE') {
                            sh "trivy image --exit-code 1 --severity CRITICAL ${img}"
                        }
                        // Thresholds are set so strict this is almost always UNSTABLE
                        catchError(buildResult: 'UNSTABLE', stageResult: 'UNSTABLE') {
                            sh "trivy image --exit-code 1 --severity HIGH ${img}"
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            publishHTML(target: [
                reportDir: '.',
                reportFiles: 'trivy-report.html',
                reportName: 'Trivy Security Report',
                allowMissing: true
            ])
        }
        unstable {
            slackSend(channel: '#security', color: 'warning', message: "Container scan found vulnerabilities: ${env.BUILD_URL}")
        }
    }
}
