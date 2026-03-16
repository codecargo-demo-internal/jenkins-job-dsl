multibranchPipelineJob('Platform/API/api-multibranch') {
    displayName('API Service (Multibranch)')
    description('Automatically discovers and builds branches and PRs for the API service')

    branchSources {
        git {
            id('api-dotnet-repo')
            remote('https://github.com/codecargo-demo-internal/jenkins-api-dotnet.git')
            credentialsId('github-pat')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(20)
            daysToKeep(30)
        }
    }

    triggers {
        periodicFolderTrigger {
            interval('5 minutes')
        }
    }
}
