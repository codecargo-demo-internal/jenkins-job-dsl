pipelineJob('DevOps/Docker/build-base-images') {
    displayName('Build Base Images')
    description('Builds all base Docker images used by other pipelines')

    definition {
        cpsScm {
            scm {
                git {
                    remote {
                        url('https://bitbucket.org/redknot/infra-docker-images.git')
                        credentials('bitbucket-pat')
                    }
                    branches('*/main')
                }
            }
            scriptPath('Jenkinsfile')
        }
    }

    triggers {
        cron('H 4 * * 0')  // Weekly on Sunday at 4 AM
    }
}
