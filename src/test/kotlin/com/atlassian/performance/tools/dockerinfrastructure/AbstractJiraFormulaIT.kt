package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.api.jira.AbstractJiraFormula
import java.nio.file.Paths

abstract class AbstractJiraFormulaIT {
    protected fun build(builder: AbstractJiraFormula.Builder) {
        builder
            .version("7.6.17")
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
}