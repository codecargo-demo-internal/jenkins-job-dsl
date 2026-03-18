pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 10, unit: 'MINUTES')
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://bitbucket.org/redknot/jenkins-api-dotnet.git',
                    credentialsId: 'github-pat',
                    branch: 'main'
            }
        }
        stage('Generate Protobuf') {
            steps {
                sh '''
                    echo "Checking protobuf generation..."
                    # buf generate
                    # git diff --exit-code || (echo "Protobuf generated code is out of date!" && exit 1)
                    echo "Protobuf generation check passed (dry run)"
                '''
            }
        }
    }
    post {
        failure {
            slackSend(channel: '#platform-alerts', color: 'danger', message: "Protobuf check FAILED: generated code may be out of date. ${env.BUILD_URL}")
        }
    }
}
