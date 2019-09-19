package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraCoreFormula
import org.apache.http.client.methods.HttpGet
import org.apache.http.impl.client.HttpClients
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.nio.file.Paths

class JiraCoreFormulaIT {

    private val jiraVersion = System.getenv("JIRA_CORE_VERSION") ?: "8.0.0"

    @Test
    fun shouldBeSeenByAnotherDocker() {
        JiraCoreFormula.Builder()
            .version(jiraVersion)
            .inDockerNetwork(true)
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
            .version(jiraVersion)
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