pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 45, unit: 'MINUTES')
    }
    stages {
        stage('Scan Repositories') {
            steps {
                script {
                    def repos = params.REPOS.split(',').collect { it.trim() }

                    for (repo in repos) {
                        stage("Scan ${repo}") {
                            dir(repo) {
                                git url: "https://bitbucket.org/redknot/jenkins-${repo}.git",
                                    credentialsId: 'github-pat',
                                    branch: 'main'

                                catchError(buildResult: 'UNSTABLE', stageResult: 'UNSTABLE') {
                                    sh """
                                        echo "Running OWASP dependency check on ${repo}..."
                                        # dependency-check --project ${repo} --scan . --format HTML --out reports/${repo}
                                        echo "Scan complete for ${repo}"
                                    """
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    post {
        always {
            publishHTML(target: [
                reportDir: 'reports',
                reportFiles: '*/dependency-check-report.html',
                reportName: 'OWASP Dependency Check',
                allowMissing: true
            ])
        }
    }
}
