pipelineJob('_Archived/jenkins-pipeline-test') {
    displayName('Jenkins Pipeline Test')
    description('Test pipeline - someone was learning Jenkins. Safe to delete probably.')
    disabled(true)

    definition {
        cps {
            script('''
pipeline {
    agent any
    stages {
        stage('Hello') {
            steps {
                echo 'Hello World'
            }
        }
        stage('Test') {
            steps {
                echo "Build number: ${env.BUILD_NUMBER}"
                echo "This is a test pipeline"
                sh 'uname -a'
                sh 'whoami'
            }
        }
    }
}
'''.stripIndent())
            sandbox()
        }
    }
}
