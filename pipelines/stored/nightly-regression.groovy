@Library('codecargo-shared') _

pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 60, unit: 'MINUTES')
    }
    stages {
        stage('Trigger CI Jobs') {
            steps {
                script {
                    def jobs = [
                        'Platform/API/api-service-ci',
                        'Platform/Web/web-app-ci',
                        'Platform/Operator/operator-ci',
                        'Legacy/monolith-build'
                    ]

                    def results = [:]
                    for (job in jobs) {
                        try {
                            build job: job, wait: true, propagate: false
                            results[job] = 'SUCCESS'
                        } catch (Exception e) {
                            results[job] = 'FAILED'
                        }
                    }

                    def summary = results.collect { k, v -> "${k}: ${v}" }.join('\n')
                    echo "Nightly regression results:\n${summary}"
                    currentBuild.description = "Results: ${results.values().count { it == 'SUCCESS' }}/${results.size()} passed"
                }
            }
        }
    }
    post {
        always {
            notifyEmail(
                recipients: 'platform-team@redknot-enterprises.com,legacy-support@redknot-enterprises.com',
                subject: "Nightly Regression: ${currentBuild.currentResult}",
                attachLog: true
            )
        }
    }
}
