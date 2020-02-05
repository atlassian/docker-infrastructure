package com.atlassian.performance.tools.dockerinfrastructure.api.jira

import java.nio.file.Path

class JiraSoftwareFormula private constructor(
    private val port: Int,
    private val version: String,
    private val inDockerNetwork: Boolean,
    private val diagnoses: Path
) : AbstractJiraFormula(port, version, inDockerNetwork, diagnoses, "software") {
    class Builder : AbstractJiraFormula.Builder() {
        override fun build(): JiraFormula {
            return JiraSoftwareFormula(
                port = port,
                version = version,
                inDockerNetwork = inDockerNetwork,
                diagnoses = diagnoses
            )
        }
    }
}