// Dashboard views for quick status overview

listView('All CI Jobs') {
    description('All continuous integration jobs')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex('.*-ci.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView('All Deploy Jobs') {
    description('All deployment jobs')
    filterBuildQueue()
    filterExecutors()
    jobs {
        regex('.*-deploy.*|.*-promote.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}

listView('Nightly Jobs') {
    description('Jobs that run on a schedule')
    filterBuildQueue()
    filterExecutors()
    jobs {
        name('nightly-full-regression')
        name('dependency-check')
        name('proto-gen-verify')
        regex('.*nightly.*|.*etl.*')
    }
    columns {
        status()
        weather()
        name()
        lastSuccess()
        lastFailure()
        lastDuration()
        buildButton()
    }
}
