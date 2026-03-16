matrixJob('Mobile/smoke-tests') {
    displayName('Mobile Smoke Tests')
    description('Cross-browser smoke tests for mobile endpoints. All fail because Selenium grid does not exist.')

    axes {
        text('BROWSER', 'chrome', 'firefox', 'safari')
        text('ENVIRONMENT', 'dev', 'staging')
    }

    steps {
        shell('''
            echo "Running smoke tests with ${BROWSER} on ${ENVIRONMENT}"
            echo "Selenium Grid: http://selenium-grid.testing.svc:4444"
            # This always fails because there is no Selenium grid
            # curl -sf http://selenium-grid.testing.svc:4444/wd/hub/status || echo "Grid not available"
            echo "Test suite: mobile-smoke-${BROWSER}-${ENVIRONMENT}"
            echo "PASSED (dry run)"
        '''.stripIndent())
    }

    publishers {
        extendedEmail {
            recipientList('mobile-qa@redknot-enterprises.com')
            triggers {
                failure {
                    sendTo {
                        recipientList()
                    }
                }
            }
        }
    }
}
