// Copy-paste of terraform-plan with apply added. We know.
pipelineJob('DevOps/Infrastructure/terraform-apply') {
    displayName('Terraform Apply')
    description('Run terraform apply - REQUIRES APPROVAL')

    parameters {
        choiceParam('WORKSPACE', ['dev', 'staging', 'prod'], 'Terraform workspace')
        stringParam('TARGET', '', 'Specific resource target (optional)')
        booleanParam('AUTO_APPROVE', false, 'Skip approval (dev only)')
    }

    definition {
        cps {
            script(readFileFromWorkspace('pipelines/stored/terraform-apply.groovy'))
            sandbox()
        }
    }
}
