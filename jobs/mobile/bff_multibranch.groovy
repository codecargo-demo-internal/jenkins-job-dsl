// This multibranch was configured by someone expecting the mobile team to add a Jenkinsfile.
// They never did. The job is perpetually empty with no discovered branches.
multibranchPipelineJob('Mobile/bff-multibranch') {
    displayName('Mobile BFF (Multibranch)')
    description('Waiting for mobile team to add Jenkinsfile to their repo...')

    branchSources {
        git {
            id('mobile-bff-repo')
            remote('https://github.com/codecargo-demo-internal/jenkins-mobile-bff-node.git')
            credentialsId('github-pat')
        }
    }

    orphanedItemStrategy {
        discardOldItems {
            numToKeep(5)
        }
    }
}
