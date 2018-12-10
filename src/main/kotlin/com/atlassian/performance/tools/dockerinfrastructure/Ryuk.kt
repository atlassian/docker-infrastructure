package com.atlassian.performance.tools.dockerinfrastructure

import org.testcontainers.DockerClientFactory
import org.testcontainers.dockerclient.DockerClientProviderStrategy
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean

/**
 *  Test containers 1.9.1 doesn't work with Bitbucket pipelines.
 *  Test containers start a docker with Ryuk. Ryuk needs to mount the docker socket, and this is forbidden in Bitbucket pipelines.
 *  Ryuk isn't a critical feature. It makes after test docker container cleanup more robust.
 *  This class is a hack to disable the feature.
 *  Sorry for the hack, but I neither understand how to fix Ryuk problems, neither want to fork and release forked test-containers.
 *
 *  See:
 *  https://github.com/testcontainers/testcontainers-java/issues/712
 *
 *  Please remove it after the issue resolved, and keep the class updated when upgrading test-containers.
 */
internal object Ryuk {
    private val initialized = AtomicBoolean(false)

    internal fun disable() {
        val dockerClientFactory = DockerClientFactory.instance()
        synchronized(dockerClientFactory) {
            if (this.initialized.get().not()) {
                val configurationStrategies = ArrayList<DockerClientProviderStrategy>()
                ServiceLoader.load(DockerClientProviderStrategy::class.java).forEach { cs -> configurationStrategies.add(cs) }
                val strategy = DockerClientProviderStrategy.getFirstValidStrategy(configurationStrategies)
                val client = strategy.getClient()
                val version = client.versionCmd().exec()
                val activeApiVersion = DockerClientFactory::class.java.getDeclaredField("activeApiVersion")
                activeApiVersion.isAccessible = true
                activeApiVersion.set(dockerClientFactory, version.getApiVersion())

                val dockerInfo = client.infoCmd().exec()
                val activeExecutionDriver = DockerClientFactory::class.java.getDeclaredField("activeExecutionDriver")
                activeExecutionDriver.isAccessible = true
                activeExecutionDriver.set(dockerClientFactory, dockerInfo.getExecutionDriver())

                val initialized = DockerClientFactory::class.java.getDeclaredField("initialized")
                initialized.isAccessible = true
                initialized.set(dockerClientFactory, true)
                this.initialized.set(true)
            }
        }
    }
}