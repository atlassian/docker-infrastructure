package com.atlassian.performance.tools.dockerinfrastructure.jira

import org.testcontainers.containers.GenericContainer
import org.testcontainers.images.builder.ImageFromDockerfile

internal class JiraContainer(dockerImageName: ImageFromDockerfile) : GenericContainer<JiraContainer>(dockerImageName)