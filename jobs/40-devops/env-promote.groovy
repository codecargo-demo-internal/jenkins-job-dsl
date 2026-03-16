pipelineJob('DevOps/environment-promote') {
    displayName('Environment Promote')
    description('Promote services from one environment to another. Uses shared library.')

    parameters {
        choiceParam('FROM_ENV', ['dev', 'staging'], 'Source environment')
        choiceParam('TO_ENV', ['staging', 'prod'], 'Target environment')
        stringParam('VERSION', '', 'Version to promote')
        stringParam('SERVICES', 'api,web,operator', 'Comma-separated list of services')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/env-promote.groovy'))
            sandbox()
        }
    }
}
