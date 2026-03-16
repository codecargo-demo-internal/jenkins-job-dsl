freeStyleJob('Data-Engineering/etl-nightly-run') {
    displayName('ETL Nightly Run')
    description('Nightly ETL processing job. Runs at 2 AM.')

    parameters {
        stringParam('DATE_OVERRIDE', '', 'Override processing date (YYYY-MM-DD). Defaults to yesterday.')
        booleanParam('DRY_RUN', false, 'Run in dry-run mode without writing to database')
    }

    triggers {
        cron('H 2 * * *')
    }

    // Hardcoded env vars - the data team knows this is bad but "it works"
    environmentVariables {
        env('DB_HOST', 'redknot-rds-dev.cluster-abc123.us-east-2.rds.amazonaws.com')
        env('DB_PORT', '5432')
        env('DB_NAME', 'analytics')
        env('S3_BUCKET', 'redknot-data-lake-dev')
    }

    wrappers {
        timestamps()
        timeout {
            absolute(120)
        }
    }

    steps {
        shell('''
            echo "Starting ETL run for date: ${DATE_OVERRIDE:-$(date -d yesterday +%Y-%m-%d)}"
            echo "Dry run: ${DRY_RUN}"

            python3 -m venv venv
            . venv/bin/activate
            pip install -r requirements.txt

            if [ "${DRY_RUN}" = "true" ]; then
                echo "[DRY RUN] Would process data for ${DATE_OVERRIDE:-$(date -d yesterday +%Y-%m-%d)}"
            else
                python3 -m src.transform
            fi

            echo "ETL complete"
        '''.stripIndent())
    }

    publishers {
        extendedEmail {
            recipientList('data-engineering@redknot-enterprises.com')
            triggers {
                always {
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
    }
}
