pipeline {
    agent any
    stages {
        stage('Build Image') {
            steps {
                git url: 'https://bitbucket.org/redknot/jenkins-web-nextjs.git',
                    credentialsId: 'github-pat',
                    branch: 'main'
                sh """
                    docker build -t 145023098958.dkr.ecr.us-east-2.amazonaws.com/web-app:${params.VERSION ?: env.BUILD_NUMBER} .
                """
            }
        }
        stage('Push Image') {
            steps {
                // TODO: move this to the shared library dockerBuild step
                withCredentials([usernamePassword(credentialsId: 'ecr-credentials', usernameVariable: 'USER', passwordVariable: 'PASS')]) {
                    sh """
                        echo \${PASS} | docker login --username \${USER} --password-stdin 145023098958.dkr.ecr.us-east-2.amazonaws.com
                        docker push 145023098958.dkr.ecr.us-east-2.amazonaws.com/web-app:${params.VERSION ?: env.BUILD_NUMBER}
                    """
                }
            }
        }
        stage('Deploy') {
            steps {
                sh """
                    kubectl set image deployment/web-app \
                        web-app=145023098958.dkr.ecr.us-east-2.amazonaws.com/web-app:${params.VERSION ?: env.BUILD_NUMBER} \
                        -n default --record
                    kubectl rollout status deployment/web-app -n default --timeout=300s
                """
            }
        }
    }
}
