multibranchPipelineJob('Platform/Operator/operator-multibranch') {
    displayName('Operator (Multibranch)')

    branchSources {
        git {
            id('operator-go-repo')
            remote('https://bitbucket.org/redknot/jenkins-operator-go.git')
            credentialsId('github-pat')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(10)
        }
    }
}
