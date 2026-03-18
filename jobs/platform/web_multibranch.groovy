multibranchPipelineJob('Platform/Web/web-multibranch') {
    displayName('Web App (Multibranch)')

    branchSources {
        git {
            id('web-nextjs-repo')
            remote('https://bitbucket.org/redknot/jenkins-web-nextjs.git')
            credentialsId('bb-platform-deploy-key')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(15)
            daysToKeep(30)
        }
    }
}
