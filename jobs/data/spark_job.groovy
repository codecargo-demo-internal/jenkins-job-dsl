freeStyleJob('Data-Engineering/spark-job-submit') {
    displayName('Spark Job Submit')
    description('Submit Spark jobs to EMR cluster. TODO: migrate to Airflow (JIRA-3847)')
    disabled(true)

    parameters {
        choiceParam('JOB_NAME', ['daily-aggregation', 'user-analytics', 'revenue-report'], 'Spark job to run')
        stringParam('EMR_CLUSTER_ID', 'j-PLACEHOLDER123', 'EMR cluster ID')
    }

    steps {
        shell('''
            echo "Submitting Spark job: ${JOB_NAME}"
            echo "EMR Cluster: ${EMR_CLUSTER_ID}"
            # This was pointing at an EMR cluster that was decommissioned in 2024-06
            # aws emr add-steps --cluster-id ${EMR_CLUSTER_ID} --steps Type=Spark,Name=${JOB_NAME},...
            echo "DISABLED - See JIRA-3847"
        '''.stripIndent())
    }
}
