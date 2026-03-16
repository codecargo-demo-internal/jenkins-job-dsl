// Copy-paste of terraform-plan with apply step added
pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 30, unit: 'MINUTES')
    }
    environment {
        TF_IN_AUTOMATION = 'true'
    }
    stages {
        stage('Init') {
            steps {
                withCredentials([string(credentialsId: "aws-${params.WORKSPACE}-credentials", variable: 'AWS_ACCESS_KEY_ID'),
                                 string(credentialsId: "aws-${params.WORKSPACE}-credentials-secret", variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        export AWS_DEFAULT_REGION=us-east-2
                        terraform init -backend-config="key=${params.WORKSPACE}/terraform.tfstate"
                        terraform workspace select ${params.WORKSPACE} || terraform workspace new ${params.WORKSPACE}
                    """
                }
            }
        }
        stage('Plan') {
            steps {
                withCredentials([string(credentialsId: "aws-${params.WORKSPACE}-credentials", variable: 'AWS_ACCESS_KEY_ID'),
                                 string(credentialsId: "aws-${params.WORKSPACE}-credentials-secret", variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    script {
                        def targetFlag = params.TARGET ? "-target=${params.TARGET}" : ""
                        sh """
                            export AWS_DEFAULT_REGION=us-east-2
                            terraform plan ${targetFlag} -out=tfplan
                        """
                    }
                }
            }
        }
        stage('Approval') {
            when {
                expression { params.WORKSPACE != 'dev' || !params.AUTO_APPROVE }
            }
            steps {
                input message: "Apply terraform changes to ${params.WORKSPACE}?",
                      submitter: 'admin,devops-team'
            }
        }
        stage('Apply') {
            steps {
                withCredentials([string(credentialsId: "aws-${params.WORKSPACE}-credentials", variable: 'AWS_ACCESS_KEY_ID'),
                                 string(credentialsId: "aws-${params.WORKSPACE}-credentials-secret", variable: 'AWS_SECRET_ACCESS_KEY')]) {
                    sh """
                        export AWS_DEFAULT_REGION=us-east-2
                        terraform apply tfplan
                    """
                }
            }
        }
    }
    post {
        success {
            slackSend(channel: '#infrastructure', color: 'good', message: "Terraform applied to ${params.WORKSPACE}: ${env.BUILD_URL}")
        }
        failure {
            slackSend(channel: '#infrastructure', color: 'danger', message: "Terraform apply FAILED on ${params.WORKSPACE}: ${env.BUILD_URL}")
        }
    }
}
