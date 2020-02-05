package com.atlassian.performance.tools.dockerinfrastructure

import com.atlassian.performance.tools.dockerinfrastructure.api.jira.JiraCoreFormula
import org.junit.Test

class JiraCoreFormulaIT : AbstractJiraFormulaIT() {

    @Test
    fun shouldBeSeenByAnotherDocker() {
        val builder = JiraCoreFormula.Builder()
        build(builder)
    }
}