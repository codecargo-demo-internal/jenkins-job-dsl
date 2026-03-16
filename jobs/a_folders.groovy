// Create the folder hierarchy for organizing jobs by team
// This runs first (01-) to ensure folders exist before jobs are created

folder('Platform') {
    displayName('Platform Team')
    description('Microservices owned by the Platform team')
}

folder('Platform/API') {
    displayName('API Service')
}

folder('Platform/Web') {
    displayName('Web Application')
}

folder('Platform/Operator') {
    displayName('Kubernetes Operator')
}

folder('Data-Engineering') {
    displayName('Data Engineering')
    description('ETL pipelines and data processing jobs')
}

folder('Legacy') {
    displayName('Legacy Applications')
    description('Monolith and legacy services - see JIRA-4521 for decomposition plan')
}

folder('DevOps') {
    displayName('DevOps')
    description('Infrastructure, Docker images, and operational tooling')
}

folder('DevOps/Docker') {
    displayName('Docker Images')
}

folder('DevOps/Infrastructure') {
    displayName('Infrastructure')
}

folder('Mobile') {
    displayName('Mobile Team')
    description('Mobile BFF and related services')
}

folder('_Archived') {
    displayName('Archived')
    description('Disabled/archived jobs - DO NOT DELETE without checking with original owners')
}
