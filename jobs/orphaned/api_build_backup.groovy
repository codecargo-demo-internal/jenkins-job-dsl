// This is a backup copy of the API build job from before the shared library migration.
// Created by @patel on 2024-01-15 "just in case"
freeStyleJob('_Archived/api-build-BACKUP') {
    displayName('API Build (BACKUP - OLD)')
    description('BACKUP of API build job from before shared library migration. Created 2024-01-15 by @patel.')
    disabled(true)

    scm {
        git {
            remote {
                url('https://bitbucket.org/redknot/jenkins-api-dotnet.git')
                credentials('redknot-bb-readonly')
            }
            branches('*/main')
        }
    }

    steps {
        shell('''
            dotnet restore src/RedknotApi
            dotnet build src/RedknotApi -c Release
            dotnet test test/RedknotApi.Tests -c Release --logger trx
            dotnet publish src/RedknotApi -c Release -o publish/
            docker build -t api-service:${BUILD_NUMBER} .
        '''.stripIndent())
    }

    publishers {
        archiveArtifacts {
            pattern('publish/**/*')
        }
    }
}
