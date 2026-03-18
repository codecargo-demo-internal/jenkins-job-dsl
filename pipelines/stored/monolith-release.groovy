node {
    def mvnHome = tool 'maven-3.9'

    lock('release-lock') {
        stage('Checkout') {
            checkout scm: [
                $class: 'GitSCM',
                branches: [[name: '*/main']],
                userRemoteConfigs: [[
                    url: 'https://bitbucket.org/redknot/monolith.git',
                    credentialsId: 'bitbucket-legacy-creds'
                ]]
            ]
        }

        stage('Prepare Release') {
            sh """
                ${mvnHome}/bin/mvn versions:set -DnewVersion=\$(${mvnHome}/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout | sed 's/-SNAPSHOT//')
                ${mvnHome}/bin/mvn versions:commit
            """
            def releaseVersion = sh(script: "${mvnHome}/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
            echo "Release version: ${releaseVersion}"
            currentBuild.description = "Release: ${releaseVersion}"

            sh """
                git add -A
                git commit -m "Release ${releaseVersion}"
                git tag -a v${releaseVersion} -m "Release ${releaseVersion}"
            """
        }

        stage('Build Release') {
            sh "${mvnHome}/bin/mvn clean deploy -DskipTests"
        }

        stage('Push') {
            sshagent(['deploy-ssh-key']) {
                sh "git push origin main --tags"
            }
        }

        stage('Trigger Deploy') {
            def releaseVersion = sh(script: "${mvnHome}/bin/mvn help:evaluate -Dexpression=project.version -q -DforceStdout", returnStdout: true).trim()
            build job: 'Legacy/monolith-deploy',
                  parameters: [
                      string(name: 'ENVIRONMENT', value: 'dev'),
                      string(name: 'VERSION', value: releaseVersion),
                      booleanParam(name: 'ROLLBACK', value: false)
                  ],
                  wait: false
        }

        stage('Bump to Next SNAPSHOT') {
            sh """
                ${mvnHome}/bin/mvn versions:set -DnextSnapshot=true
                ${mvnHome}/bin/mvn versions:commit
                git add -A
                git commit -m "Bump to next SNAPSHOT"
            """
            sshagent(['deploy-ssh-key']) {
                sh "git push origin main"
            }
        }
    }
}
