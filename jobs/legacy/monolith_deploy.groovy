pipelineJob('Legacy/monolith-deploy') {
    displayName('Monolith Deploy')
    description('Deploy the monolith to target environment. Requires approval for prod.')

    parameters {
        choiceParam('ENVIRONMENT', ['dev', 'staging', 'prod'], 'Target environment')
        stringParam('VERSION', '', 'Version to deploy (e.g., 3.14.2-42)')
        booleanParam('ROLLBACK', false, 'Rollback to previous version instead of deploying')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/monolith-deploy.groovy'))
            sandbox()
        }
    }
}
