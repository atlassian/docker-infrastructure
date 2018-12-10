package com.atlassian.performance.tools.dockerinfrastructure.api.jira

import com.atlassian.performance.tools.dockerinfrastructure.DockerContainers
import com.atlassian.performance.tools.dockerinfrastructure.LogWaitStrategy
import com.atlassian.performance.tools.dockerinfrastructure.Ryuk
import com.atlassian.performance.tools.dockerinfrastructure.network.SharedNetwork.Companion.DEFAULT_NETWORK_NAME
import com.atlassian.performance.tools.dockerinfrastructure.jira.DockerisedJira
import com.atlassian.performance.tools.dockerinfrastructure.jira.JiraContainer
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testcontainers.containers.Network
import org.testcontainers.images.builder.ImageFromDockerfile

class JiraCoreFormula private constructor(
    private val port: Int,
    private val version: String

) : JiraFormula {
    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val networkAlias = "jira"
    private val networkName = DEFAULT_NETWORK_NAME

    override fun provision(): Jira {
        Ryuk.disable()
        DockerContainers().clean(networkName)
        val network = Network.NetworkImpl
            .builder()
            .createNetworkCmdModifier { createNetworkCmd ->
                createNetworkCmd.withName(networkName)
            }
            .build()
        val jiraContainer = JiraContainer(
            ImageFromDockerfile()
                .withDockerfileFromBuilder { builder ->
                    builder
                        .from("ubuntu:18.10")
                        .expose(port)
                        .run("apt-get", "update", "-qq")
                        .run("apt-get", "install", "wget", "openjdk-8-jdk", "-qq")
                        .run("wget", "https://product-downloads.atlassian.com/software/jira/downloads/atlassian-jira-core-$version.tar.gz")
                        .run("tar", "-xzf", "atlassian-jira-core-$version.tar.gz")
                        .run("rm", "/atlassian-jira-core-$version-standalone/bin/check-java.sh")
                        .run("mkdir", "jira-home")
                        .env("JAVA_HOME", "/usr/lib/jvm/java-1.8.0-openjdk-amd64/jre")
                        .env("JIRA_HOME", "/jira-home")
                        .cmd("/atlassian-jira-core-$version-standalone/bin/start-jira.sh", "-fg")
                        .build()
                }
        )
            .withExposedPorts(port)
            .withNetwork(network)
            .withNetworkAliases(networkAlias)
            .withLogConsumer { outputFrame ->
                val logLine = outputFrame.utf8String.replace(Regex("((\\r?\\n)|(\\r))$"), "")
                logger.info(logLine)
            }
            .waitingFor(
                LogWaitStrategy("The database is not yet configured")
            )
        jiraContainer.start()
        return DockerisedJira(
            jiraContainer = jiraContainer,
            network = network,
            networkAlias = networkAlias,
            port = port
        )
    }

    class Builder {
        private var port: Int = 8080
        private var version: String = "7.12.3"

        fun port(port: Int) = apply { this.port = port }
        fun version(version: String) = apply { this.version = version }

        fun build(): JiraFormula {
            return JiraCoreFormula(
                port = port,
                version = version
            )
        }
    }
}