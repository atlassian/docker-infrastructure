package com.atlassian.performance.tools.dockerinfrastructure.jira

import com.atlassian.performance.tools.dockerinfrastructure.api.jira.Jira
import org.testcontainers.containers.Network
import java.net.URI

internal class DockerisedJira(
    private val jiraContainer: JiraContainer,
    private val network: Network,
    private val uri : URI
) : Jira {

    override fun getUri(): URI {
        return uri
    }

    override fun close() {
        jiraContainer.close()
        network.close()
    }
}
