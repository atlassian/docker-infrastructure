package com.atlassian.performance.tools.dockerinfrastructure.api.network

import org.junit.rules.ExternalResource
import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.Network

class SharedNetwork(name: String) : ExternalResource(), Network {
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