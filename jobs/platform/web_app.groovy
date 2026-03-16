pipelineJob('Platform/Web/web-app-ci') {
    displayName('Web App CI')
    description('Builds and tests the Next.js web application. Uses shared library nodeCI step.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://github.com/codecargo-demo-internal/jenkins-web-nextjs.git')
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
                    numToKeepStr('20')
                }
            }
        }
    }
}
