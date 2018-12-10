package com.atlassian.performance.tools.dockerinfrastructure.jira

import com.atlassian.performance.tools.dockerinfrastructure.api.jira.Jira
import org.testcontainers.containers.Network
import java.net.URI

internal class DockerisedJira(
    private val jiraContainer: JiraContainer,
    private val network: Network,
    private val networkAlias: String,
    private val port: Int
) : Jira {

    override fun getUri(): URI {
        return URI("http://localhost:${jiraContainer.getMappedPort(port)}/")
    }

    override fun getDockerUri(): URI {
        return URI("http://$networkAlias:$port/")
    }

    override fun close() {
        jiraContainer.close()
        network.close()
    }
}
