package com.atlassian.performance.tools.dockerinfrastructure.api.jira

interface JiraFormula {
    fun provision(): Jira
}