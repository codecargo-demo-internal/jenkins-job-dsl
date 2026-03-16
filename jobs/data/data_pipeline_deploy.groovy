freeStyleJob('Data-Engineering/data-pipeline-deploy') {
    displayName('Data Pipeline - Deploy')
    description('Deploys the data pipeline container. Triggered by successful ingest build.')

    wrappers {
        timestamps()
    }

    steps {
        copyArtifacts('Data-Engineering/data-pipeline-ingest') {
            includePatterns('output/**/*.csv')
            optional(true)
            buildSelector {
                upstreamBuild()
            }
        }
        shell('''
            echo "Building data pipeline Docker image..."
            docker build -t 145023098958.dkr.ecr.us-east-2.amazonaws.com/data-pipeline:${BUILD_NUMBER} .
            echo "Pushing to ECR..."
            aws ecr get-login-password --region us-east-2 | docker login --username AWS --password-stdin 145023098958.dkr.ecr.us-east-2.amazonaws.com
            docker push 145023098958.dkr.ecr.us-east-2.amazonaws.com/data-pipeline:${BUILD_NUMBER}
            echo "Deploy complete"
        '''.stripIndent())
    }
}
