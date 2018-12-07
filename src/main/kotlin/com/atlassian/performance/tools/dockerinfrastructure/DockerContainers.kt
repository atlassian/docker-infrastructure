package com.atlassian.performance.tools.dockerinfrastructure

import com.github.dockerjava.api.DockerClient
import com.github.dockerjava.api.model.Container
import org.testcontainers.DockerClientFactory

internal class DockerContainers {

    fun clean(networkName: String) {
        val dockerClient = DockerClientFactory
            .instance()
            .client()
        cleanContainers(dockerClient)
        cleanNetwork(dockerClient, networkName)
    }

    private fun cleanContainers(dockerClient: DockerClient) {
        val containers: List<Container> = dockerClient
            .listContainersCmd()
            .withLabelFilter(mapOf("org.testcontainers" to "true"))
            .exec()
        containers.forEach { container ->
            dockerClient
                .stopContainerCmd(container.id)
                .exec()
        }
    }

    private fun cleanNetwork(dockerClient: DockerClient, networkName: String) {
        dockerClient
            .listNetworksCmd()
            .withNameFilter(networkName)
            .exec()
            .forEach { network ->
                dockerClient
                    .removeNetworkCmd(network.id)
                    .exec()
            }
    }
}