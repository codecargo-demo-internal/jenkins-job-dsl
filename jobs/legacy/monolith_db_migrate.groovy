freeStyleJob('Legacy/monolith-db-migrate') {
    displayName('Monolith DB Migration')
    description('Run Flyway database migrations for the monolith')

    parameters {
        choiceParam('ENVIRONMENT', ['dev', 'staging', 'prod'], 'Target environment')
        stringParam('MIGRATION_VERSION', '', 'Specific migration version (leave empty for latest)')
        booleanParam('DRY_RUN', true, 'Preview migrations without applying')
    }

    wrappers {
        timestamps()
        credentialsBinding {
            usernamePassword('DB_USER', 'DB_PASS', 'monolith-db-creds')
        }
    }

    steps {
        shell('''
            echo "Running Flyway migration on ${ENVIRONMENT}"

            case ${ENVIRONMENT} in
                dev)     DB_URL="jdbc:postgresql://redknot-rds-dev.cluster-abc123.us-east-2.rds.amazonaws.com:5432/monolith" ;;
                staging) DB_URL="jdbc:postgresql://redknot-rds-staging.cluster-def456.us-east-2.rds.amazonaws.com:5432/monolith" ;;
                prod)    DB_URL="jdbc:postgresql://redknot-rds-prod.cluster-ghi789.us-east-2.rds.amazonaws.com:5432/monolith" ;;
            esac

            FLYWAY_CMD="flyway -url=${DB_URL} -user=${DB_USER} -password=${DB_PASS}"

            if [ -n "${MIGRATION_VERSION}" ]; then
                FLYWAY_CMD="${FLYWAY_CMD} -target=${MIGRATION_VERSION}"
            fi

            if [ "${DRY_RUN}" = "true" ]; then
                ${FLYWAY_CMD} info
            else
                ${FLYWAY_CMD} migrate
            fi
        '''.stripIndent())
    }
}
