// Deploy jobs per environment - yes these are copy-paste with hardcoded values.
// We know. It's on the backlog. See JIRA-5102.

pipelineJob('Platform/API/api-deploy-dev') {
    displayName('API Deploy - Dev')
    description('Deploy API service to dev environment')

    parameters {
        stringParam('VERSION', '', 'Docker image version to deploy')
        booleanParam('SKIP_TESTS', false, 'Skip smoke tests after deploy')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/api-deploy-dev.groovy'))
            sandbox()
        }
    }
}

pipelineJob('Platform/API/api-deploy-staging') {
    displayName('API Deploy - Staging')
    description('Deploy API service to staging environment')

    parameters {
        stringParam('VERSION', '', 'Docker image version to deploy')
        booleanParam('SKIP_TESTS', false, 'Skip smoke tests after deploy')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/api-deploy-staging.groovy'))
            sandbox()
        }
    }
}
