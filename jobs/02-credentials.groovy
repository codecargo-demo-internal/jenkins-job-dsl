// Note: Most credentials are managed in JCasC, but some teams added their own
// at the folder level. This is a known mess - see SEC-2341 for cleanup initiative.

// These are placeholder definitions that reference credentials already in Jenkins.
// Job DSL can't create credentials directly, but it documents what's expected.

println "=== Credentials Check ==="
println "Expecting the following credentials to exist:"
println "  - github-pat (Username/Password)"
println "  - slack-webhook (Secret Text)"
println "  - artifactory-creds (Username/Password)"
println "  - sonarqube-token (Username/Password)"
println "  - ecr-credentials (Username/Password)"
println "  - deploy-ssh-key (SSH Key)"
println "  - aws-dev-credentials (Secret Text)"
println "  - aws-dev-credentials-secret (Secret Text)"
println "  - aws-staging-credentials (Secret Text)"
println "  - aws-staging-credentials-secret (Secret Text)"
println "  - aws-prod-credentials (Secret Text)"
println "  - aws-prod-credentials-secret (Secret Text)"
println "=== End Credentials Check ==="
