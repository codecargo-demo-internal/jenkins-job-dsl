// This lives at the top level because "it covers everything" - nobody wanted to own it
pipelineJob('nightly-full-regression') {
    displayName('Nightly Full Regression')
    description('Triggers all CI jobs and collects results. Runs at midnight.')

    triggers {
        cron('H 0 * * *')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/nightly-regression.groovy'))
            sandbox()
        }
    }
}
