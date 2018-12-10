package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraCoreFormula
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.assertj.core.api.Assertions
import org.junit.Test

class DockerInfrastructureIT {

    @Test
    fun shouldProvisionJiraCore() {
        JiraCoreFormula.Builder()
            .version("7.2.12")
            .build()
            .provision()
            .use { jira ->
                DockerisedChrome().start().use { chrome ->
                    chrome.driver.navigate() to jira.getDockerUri()
                }

                val jiraAddress = jira.getUri()
                val httpclient = HttpClients.createDefault()
                val get = HttpGet(jiraAddress)
                val response = httpclient.execute(get)
                Assertions.assertThat(response.statusLine.statusCode).isEqualTo(200)
            }
    }
}