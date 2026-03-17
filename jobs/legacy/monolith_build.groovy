pipelineJob('Legacy/monolith-build') {
    displayName('Monolith Build')
    description('Builds the Java monolith. Jenkinsfile lives in the repo. PLEASE DO NOT MODIFY without checking with @garcia.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/codecargo-demo-internal/jenkins-legacy-monolith-java.git')
                        credentials('github-pat')
                    }
                    branches('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }

    triggers {
        scm('H/5 * * * *')
    }

    properties {
        buildDiscarder {
            strategy {
                logRotator {
                    numToKeepStr('50')
                    daysToKeepStr('')
                    artifactNumToKeepStr('10')
                    artifactDaysToKeepStr('')
                }
            }
        }
    }
}
