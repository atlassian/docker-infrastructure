package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraCoreFormula
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths

class JiraCoreFormulaIT {

    @Test
    fun shouldBeSeenByAnotherDocker() {
        JiraCoreFormula.Builder()
            .version("7.2.12")
            .build()
            .provision()
            .use { jira ->
                DockerisedChrome(Paths.get("build/diagnoses/recordings/shouldBeSeenByAnotherDocker"))
                    .start()
                    .use { chrome ->
                        chrome.driver.navigate() to jira.getUri()
                    }
            }
    }

    @Test
    fun shouldBeSeenByTheHost() {
        JiraCoreFormula.Builder()
            .version("7.2.12")
            .inDockerNetwork(false)
            .build()
            .provision()
            .use { jira ->
                val jiraAddress = jira.getUri()
                val httpClient = HttpClients.createDefault()
                val response = httpClient.execute(HttpGet(jiraAddress))
                assertThat(response.statusLine.statusCode).isEqualTo(200)
            }
    }
}