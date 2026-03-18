freeStyleJob('Data-Engineering/data-pipeline-ingest') {
    displayName('Data Pipeline - Ingest')
    description('Runs data ingestion from external sources. Polls SCM every 15 minutes.')

    scm {
        git {
            remote {
                url('https://bitbucket.org/redknot/data-etl-pipeline.git')
                credentials('github-pat')
            }
            branches('*/main')
        }
    }

    triggers {
        scm('H/15 * * * *')
    }

    wrappers {
        timestamps()
        timeout {
            absolute(30)
        }
        credentialsBinding {
            string('DB_CONNECTION_STRING', 'data-pipeline-db-conn')
        }
    }

    steps {
        shell('''
            python3 -m venv venv
            . venv/bin/activate
            pip install -r requirements.txt
            python3 -m pytest tests/ -v --junitxml=test-results.xml
            python3 -m src.ingest
        '''.stripIndent())
    }

    publishers {
        archiveJunit('test-results.xml') {
            allowEmptyResults(true)
        }
        archiveArtifacts {
            pattern('output/**/*.csv')
            allowEmpty(true)
        }
        extendedEmail {
            recipientList('data-engineering@redknot-enterprises.com')
            defaultSubject('Data Pipeline Ingest - Build ${BUILD_STATUS}')
            defaultContent('Check console output at ${BUILD_URL}')
            triggers {
                failure {
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
        downstream('Data-Engineering/data-pipeline-deploy', 'SUCCESS')
    }
}
