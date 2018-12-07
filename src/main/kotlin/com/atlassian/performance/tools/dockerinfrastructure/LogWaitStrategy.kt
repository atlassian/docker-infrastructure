package com.atlassian.performance.tools.dockerinfrastructure

import org.testcontainers.DockerClientFactory
import org.testcontainers.containers.ContainerLaunchException
import org.testcontainers.containers.output.OutputFrame
import org.testcontainers.containers.output.WaitingConsumer
import org.testcontainers.containers.wait.strategy.AbstractWaitStrategy
import org.testcontainers.utility.LogUtils
import java.time.Duration
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

internal class LogWaitStrategy(
    private val message: String,
    private val timeout: Duration = Duration.ofMinutes(5)
) : AbstractWaitStrategy() {

    override fun waitUntilReady() {
        val waitingConsumer = WaitingConsumer()
        LogUtils.followOutput(DockerClientFactory.instance().client(), waitStrategyTarget.containerId, waitingConsumer)

        val waitPredicate = { outputFrame: OutputFrame -> outputFrame.getUtf8String().contains(message) }

        try {
            waitingConsumer.waitUntil(waitPredicate, timeout.toMillis().toInt(), TimeUnit.MILLISECONDS)
        } catch (e: TimeoutException) {
            throw ContainerLaunchException("Timed out waiting for log output containing '$message'", e)
        }
    }
}