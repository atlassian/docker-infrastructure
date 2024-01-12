package com.atlassian.performance.tools.dockerinfrastructure.api.jira

import com.atlassian.performance.tools.dockerinfrastructure.DockerContainers
import com.atlassian.performance.tools.dockerinfrastructure.api.browser.DockerisedChrome
import com.atlassian.performance.tools.dockerinfrastructure.jira.DockerisedJira
import com.atlassian.performance.tools.dockerinfrastructure.jira.JiraContainer
import com.atlassian.performance.tools.dockerinfrastructure.jira.SetUpFromScratchAction
import com.atlassian.performance.tools.dockerinfrastructure.lib.WebDriverDiagnostics
import com.atlassian.performance.tools.dockerinfrastructure.network.SharedNetwork.Companion.DEFAULT_NETWORK_NAME
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.testcontainers.containers.Network
import org.testcontainers.images.builder.ImageFromDockerfile
import java.net.URI
import java.nio.file.Path
import java.nio.file.Paths

abstract class AbstractJiraFormula internal constructor(
    private val port: Int,
    private val version: String,
    private val inDockerNetwork: Boolean,
    private val diagnoses: Path,
    private val jiraVariant: String
) : JiraFormula {
    private val logger: Logger = LogManager.getLogger(this::class.java)
    private val networkAlias = "jira"
    private val networkName = DEFAULT_NETWORK_NAME

    override fun provision(): Jira {
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
                        .from("ubuntu:20.04")
                        .expose(port)
                        .run("apt-get", "update", "-qq")
                        .run("apt-get", "install", "wget", "openjdk-8-jdk", "-qq")
                        .run("wget", "https://product-downloads.atlassian.com/software/jira/downloads/atlassian-jira-$jiraVariant-$version.tar.gz")
                        .run("tar", "-xzf", "atlassian-jira-$jiraVariant-$version.tar.gz")
                        .run("rm", "/atlassian-jira-$jiraVariant-$version-standalone/bin/check-java.sh")
                        .run("mkdir", "jira-home")
                        .env("JIRA_HOME", "/jira-home")
                        .cmd("/atlassian-jira-$jiraVariant-$version-standalone/bin/start-jira.sh", "-fg")
                        .build()
                }
        )
            .withExposedPorts(port)
            .withNetwork(network)
            .withNetworkAliases(networkAlias)
            .withLogConsumer { outputFrame ->
                val logLine = outputFrame.utf8String.replace(Regex("((\\r?\\n)|(\\r))$"), "")
                logger.info("Jira: $logLine")
            }
        jiraContainer.start()
        val dockerisedJira = DockerisedJira(
            jiraContainer = jiraContainer,
            network = network,
            uri = getJiraUri(jiraContainer)
        )
        provisionJira()
        return dockerisedJira
    }

    private fun getJiraUri(jiraContainer: JiraContainer): URI {
        return if (inDockerNetwork) {
            getJiraUriInDockerNetwork()
        } else {
            URI("http://localhost:${jiraContainer.getMappedPort(port)}/")
        }
    }

    private fun getJiraUriInDockerNetwork() = URI("http://$networkAlias:$port/")

    private fun provisionJira() {
        DockerisedChrome(diagnoses.resolve("recordings")).start().use { chrome ->
            try {
                SetUpFromScratchAction(getJiraUriInDockerNetwork(), chrome.driver).run()
            } catch (e: Exception) {
                WebDriverDiagnostics(chrome.driver, diagnoses).diagnose(e)
                throw Exception("Jira setup failed, look for diagnoses in $diagnoses", e)
            }
        }
    }

    abstract class Builder {
        protected var port: Int = 8080
        protected var version: String = "7.12.3"
        protected var inDockerNetwork = true
        protected var diagnoses: Path = Paths.get("build/diagnoses")

        fun port(port: Int) = apply { this.port = port }
        fun version(version: String) = apply { this.version = version }

        /**
         * Determines if the Jira will be accessed from the host machine or another docker via docker network:
         *  - Use `true` if you're going to access Jira from another docker.
         *  - Use `false` if you're going to access Jira form the host.
         *
         * It's `true` by default.
         * @since 0.2.0
         */
        fun inDockerNetwork(inDockerNetwork: Boolean) = apply { this.inDockerNetwork = inDockerNetwork }

        fun diagnoses(diagnoses: Path) = apply { this.diagnoses = diagnoses }

        abstract fun build(): JiraFormula
    }
}
