// Pipeline stored in Jenkins because the mobile team never added a Jenkinsfile to their repo.
// This is the ONLY place this pipeline definition exists. Handle with care.
pipeline {
    agent any
    tools {
        nodejs 'node-18'
    }
    options {
        timestamps()
        timeout(time: 15, unit: 'MINUTES')
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/codecargo-demo-internal/jenkins-mobile-bff-node.git',
                    credentialsId: 'github-pat',
                    branch: 'main'
            }
        }
        stage('Install') {
            steps {
                sh 'npm ci'
            }
        }
        stage('Lint') {
            steps {
                sh 'npm run lint'
            }
        }
        stage('Test') {
            steps {
                sh 'npm test'
            }
            post {
                always {
                    publishHTML(target: [
                        reportDir: 'coverage/lcov-report',
                        reportFiles: 'index.html',
                        reportName: 'BFF Coverage Report'
                    ])
                }
            }
        }
        stage('Build Image') {
            when { branch 'main' }
            steps {
                sh "docker build -t 145023098958.dkr.ecr.us-east-2.amazonaws.com/mobile-bff:${env.BUILD_NUMBER} ."
            }
        }
    }
    post {
        failure {
            slackSend(channel: '#mobile-team', color: 'danger', message: "BFF CI FAILED: ${env.BUILD_URL}")
        }
    }
}
