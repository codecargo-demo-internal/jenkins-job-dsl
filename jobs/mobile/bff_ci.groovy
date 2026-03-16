// Mobile team never added a Jenkinsfile to their repo.
// Pipeline script is stored here in Jenkins. This is a known anti-pattern.
pipelineJob('Mobile/bff-api-ci') {
    displayName('Mobile BFF CI')
    description('Build and test the mobile BFF. Pipeline stored in Jenkins (no Jenkinsfile in repo).')

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/mobile-bff-ci.groovy'))
            sandbox()
        }
    }

    triggers {
        scm('H/5 * * * *')
    }

    properties {
        pipelineTriggers {
            triggers {
                pollSCM {
                    scmpoll_spec('H/5 * * * *')
                }
            }
        }
    }
}
