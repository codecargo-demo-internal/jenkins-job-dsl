pipelineJob('Platform/Operator/operator-ci') {
    displayName('Operator CI')
    description('Builds and tests the Go Kubernetes operator. Uses shared library goCI step.')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://bitbucket.org/redknot/jenkins-operator-go.git')
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
}
