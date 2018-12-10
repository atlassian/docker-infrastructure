package com.atlassian.performance.tools.dockerinfrastructure.network

import com.atlassian.performance.tools.dockerinfrastructure.Ryuk
import org.junit.rules.ExternalResource
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.Network

internal class SharedNetwork(name: String) : ExternalResource(), Network {
    internal companion object {
        init {
            Ryuk.disable()
        }

        val DEFAULT_NETWORK_NAME: String = "shared-network"
    }

    private val id: String = DockerClientFactory
        .instance()
        .client()
        .listNetworksCmd()
        .withNameFilter(name)
        .exec()
        .single()
        .id

    override fun getId(): String {
        return this.id
    }

    override fun close() {
        DockerClientFactory
            .instance()
            .client()
            .removeNetworkCmd(id)
    }
}