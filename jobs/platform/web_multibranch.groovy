multibranchPipelineJob('Platform/Web/web-multibranch') {
    displayName('Web App (Multibranch)')

    branchSources {
        git {
            id('web-nextjs-repo')
            remote('https://bitbucket.org/redknot/jenkins-web-nextjs.git')
            credentialsId('github-pat')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(15)
            daysToKeep(30)
        }
    }
}
