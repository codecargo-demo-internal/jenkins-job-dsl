pipelineJob('dependency-check') {
    displayName('OWASP Dependency Check')
    description('Scans all repositories for vulnerable dependencies')

    parameters {
        extendedChoice {
            name('REPOS')
            type('PT_CHECKBOX')
            value('api-dotnet,web-nextjs,operator-go,legacy-monolith-java,mobile-bff-node,data-pipeline-python')
            defaultValue('api-dotnet,web-nextjs,operator-go')
            description('Repositories to scan')
        }
    }

    triggers {
        cron('H 5 * * 1')  // Monday at 5 AM
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/dependency-check.groovy'))
            sandbox()
        }
    }
}
