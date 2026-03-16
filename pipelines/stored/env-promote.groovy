@Library('codecargo-shared') _

pipeline {
    agent any
    stages {
        stage('Validate') {
            steps {
                script {
                    if (params.FROM_ENV == params.TO_ENV) {
                        error "Cannot promote from ${params.FROM_ENV} to itself"
                    }
                    if (params.TO_ENV == 'prod' && params.FROM_ENV != 'staging') {
                        error "Production promotions must come from staging"
                    }
                }
            }
        }
        stage('Promote Images') {
            steps {
                script {
                    def serviceList = params.SERVICES.split(',').collect { it.trim() }
                    promoteEnvironment(
                        fromEnv: params.FROM_ENV,
                        toEnv: params.TO_ENV,
                        services: serviceList,
                        version: params.VERSION
                    )
                }
            }
        }
        stage('Deploy') {
            steps {
                script {
                    def serviceList = params.SERVICES.split(',').collect { it.trim() }
                    for (svc in serviceList) {
                        deployService(
                            service: svc,
                            image: "${com.codecargo.jenkins.Constants.getRegistry(params.TO_ENV)}/${svc}:${params.VERSION}",
                            environment: params.TO_ENV,
                            namespace: 'default'
                        )
                    }
                }
            }
        }
    }
    post {
        success {
            notifySlack(channel: '#deployments', message: "Promoted ${params.SERVICES} v${params.VERSION}: ${params.FROM_ENV} -> ${params.TO_ENV}")
        }
    }
}
