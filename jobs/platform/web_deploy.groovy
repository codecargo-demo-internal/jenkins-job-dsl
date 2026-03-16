pipelineJob('Platform/Web/web-deploy') {
    displayName('Web App Deploy')
    description('Build Docker image and deploy web app')

    parameters {
        choiceParam('ENVIRONMENT', ['dev', 'staging'], 'Target environment')
        stringParam('VERSION', '', 'Version to deploy (defaults to latest)')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/web-deploy.groovy'))
            sandbox()
        }
    }
}
