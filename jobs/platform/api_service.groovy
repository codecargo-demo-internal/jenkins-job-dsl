pipelineJob('Platform/API/api-service-ci') {
    displayName('API Service CI')
    description('Builds and tests the .NET API service. Uses shared library dotnetCI step.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://bitbucket.org/redknot/jenkins-api-dotnet.git')
                        credentials('bitbucket-pat')
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
                    numToKeepStr('20')
                    daysToKeepStr('30')
                    artifactNumToKeepStr('')
                    artifactDaysToKeepStr('')
                }
            }
        }
    }
}
