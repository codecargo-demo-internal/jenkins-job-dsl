pipelineJob('Legacy/monolith-release') {
    displayName('Monolith Release')
    description('Cut a release: bump version, tag, and trigger deploy. Acquires release lock.')

    parameters {
        choiceParam('RELEASE_TYPE', ['minor', 'patch'], 'Release type')
    }

    properties {
        lockableResources {
            label('release-lock')
            resourceNumber(1)
        }
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/monolith-release.groovy'))
            sandbox()
        }
    }
}
