freeStyleJob('_Archived/prototype-recommender') {
    displayName('Prototype - Recommender Engine')
    description('POC for ML-based recommendations. DO NOT DELETE - may need to reference later. Contact: @chen (left company 2024-08)')
    disabled(true)

    scm {
        git {
            remote {
                url('https://github.com/codecargo-demo-internal/jenkins-data-pipeline-python.git')
                credentials('github-pat')
            }
            branches('*/experimental/recommender')
        }
    }

    steps {
        shell('''
            echo "This was a prototype for the recommendation engine"
            echo "It used TensorFlow 2.x and required GPU nodes"
            echo "See confluence page: /wiki/spaces/ML/pages/12345"
            pip install tensorflow==2.15.0
            python train_model.py
        '''.stripIndent())
    }
}
