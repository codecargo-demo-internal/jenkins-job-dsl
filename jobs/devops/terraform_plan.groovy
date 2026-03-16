pipelineJob('DevOps/Infrastructure/terraform-plan') {
    displayName('Terraform Plan')
    description('Run terraform plan for infrastructure changes')

    parameters {
        choiceParam('WORKSPACE', ['dev', 'staging', 'prod'], 'Terraform workspace')
        stringParam('TARGET', '', 'Specific resource target (optional)')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/terraform-plan.groovy'))
            sandbox()
        }
    }
}
