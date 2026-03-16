pipelineJob('DevOps/Docker/image-scan') {
    displayName('Container Image Scan')
    description('Trivy security scan for all container images. Almost always UNSTABLE (thresholds too strict).')

    parameters {
        stringParam('IMAGE', '', 'Specific image to scan (leave empty for all)')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/image-scan.groovy'))
            sandbox()
        }
    }

    triggers {
        cron('H 6 * * 1-5')  // Weekdays at 6 AM
    }
}
