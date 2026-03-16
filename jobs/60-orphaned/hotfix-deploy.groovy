// Emergency hotfix deploy from 2024-03. Was supposed to be deleted after.
freeStyleJob('_Archived/deploy-hotfix-2024-03') {
    displayName('HOTFIX Deploy 2024-03-15')
    description('Emergency hotfix for CVE-2024-XXXX. Deploy specific commit. SHOULD HAVE BEEN DELETED.')
    disabled(true)

    steps {
        shell('''
            echo "HOTFIX DEPLOY - March 2024"
            echo "Deploying commit: a1b2c3d4e5f6"
            echo "This was a one-time emergency fix"
            # git checkout a1b2c3d4e5f6
            # docker build -t api-service:hotfix-20240315 .
            # kubectl set image deployment/api-service api-service=api-service:hotfix-20240315
        '''.stripIndent())
    }
}
