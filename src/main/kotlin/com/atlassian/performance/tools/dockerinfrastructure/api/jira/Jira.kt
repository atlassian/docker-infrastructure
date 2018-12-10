package com.atlassian.performance.tools.dockerinfrastructure.api.jira

import java.net.URI

interface Jira : AutoCloseable {
    fun getUri(): URI
    fun getDockerUri(): URI
}