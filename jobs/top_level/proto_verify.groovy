pipelineJob('proto-gen-verify') {
    displayName('Protobuf Generation Verify')
    description('Verifies protobuf generated code is up to date across repos')

    triggers {
        cron('H 7 * * 1-5')  // Weekdays at 7 AM
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/proto-verify.groovy'))
            sandbox()
        }
    }
}
