pipelineJob('dependency-check') {
    displayName('OWASP Dependency Check')
    description('Scans all repositories for vulnerable dependencies')

    parameters {
        stringParam('REPOS', 'api-dotnet,web-nextjs,operator-go', 'Comma-separated list of repos to scan (options: api-dotnet,web-nextjs,operator-go,legacy-monolith-java,mobile-bff-node,data-pipeline-python)')
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
