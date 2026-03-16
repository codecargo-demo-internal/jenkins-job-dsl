freeStyleJob('DevOps/Infrastructure/vault-rotate-secrets') {
    displayName('Vault Secret Rotation')
    description('Weekly rotation of Vault secrets. Uses hardcoded Vault token (we know, SEC-2341).')

    triggers {
        cron('H 3 * * 0')  // Weekly on Sunday at 3 AM
    }

    wrappers {
        timestamps()
        credentialsBinding {
            string('VAULT_TOKEN', 'vault-root-token')
        }
    }

    steps {
        shell('''
            export VAULT_ADDR=https://vault.internal.redknot.com:8200
            echo "Rotating secrets with Vault..."
            # ./scripts/rotate-secrets.sh
            echo "Secret rotation complete (dry run)"
        '''.stripIndent())
    }
}
