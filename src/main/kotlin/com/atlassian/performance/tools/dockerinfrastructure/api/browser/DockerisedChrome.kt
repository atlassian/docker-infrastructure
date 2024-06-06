package com.atlassian.performance.tools.dockerinfrastructure.api.browser

import com.atlassian.performance.tools.dockerinfrastructure.network.SharedNetwork
import com.atlassian.performance.tools.io.api.ensureDirectory
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.VncRecordingContainer
import org.testcontainers.containers.VncRecordingContainer.VncRecordingFormat
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy
import org.testcontainers.lifecycle.TestDescription
import java.nio.file.Path
import java.util.*

class DockerisedChrome(
    private val recordings: Path
) {

    fun start(): Browser {
        val recordingsDirectory = recordings.toFile().ensureDirectory()
        val container = BrowserWebDriverContainerImpl()
            .withCapabilities(ChromeOptions())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.RECORD_ALL, recordingsDirectory, VncRecordingFormat.MP4)
            .waitingFor(HostPortWaitStrategy())
            .withNetwork(SharedNetwork(SharedNetwork.DEFAULT_NETWORK_NAME))
            .withExposedPorts(4444)
        container.start()
        return ContainerBrowser(container)
    }

    private class ContainerBrowser(
        private val container: BrowserWebDriverContainerImpl
    ) : Browser {

        override val driver: RemoteWebDriver = container.webDriver

        override fun close() {
            container.afterTest(
                object : TestDescription {
                    override fun getFilesystemFriendlyName(): String = UUID.randomUUID().toString()
                    override fun getTestId(): String = filesystemFriendlyName
                },
                Optional.empty()
            )
            driver.quit()
            container.close()
        }
    }
}

private class BrowserWebDriverContainerImpl : BrowserWebDriverContainer<BrowserWebDriverContainerImpl>()
