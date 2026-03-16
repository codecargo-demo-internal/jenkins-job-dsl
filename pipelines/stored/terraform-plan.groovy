pipeline {
    agent any
    options {
        timestamps()
        timeout(time: 15, unit: 'MINUTES')
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
    }
    post {
        always {
            archiveArtifacts artifacts: 'tfplan', allowEmptyArchive: true
        }
    }
}
