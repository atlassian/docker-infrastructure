package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraSoftwareFormula
import org.junit.Test

class JiraSoftwareFormulaIT : AbstractJiraFormulaIT() {
    @Test
    fun shouldBeSeenByTheHost() {
        build(JiraSoftwareFormula.Builder())
    }
}