package com.atlassian.performance.tools.dockerinfrastructure.api.browser

import com.atlassian.performance.tools.dockerinfrastructure.Ryuk
import com.atlassian.performance.tools.dockerinfrastructure.network.SharedNetwork
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.remote.RemoteWebDriver
import org.testcontainers.containers.BrowserWebDriverContainer
import org.testcontainers.containers.wait.strategy.HostPortWaitStrategy

class DockerisedChrome {

    fun start(): Browser {
        val container = BrowserWebDriverContainerImpl()
            .withDesiredCapabilities(DesiredCapabilities.chrome())
            .withRecordingMode(BrowserWebDriverContainer.VncRecordingMode.SKIP, null)
            .waitingFor(HostPortWaitStrategy())
            .withNetwork(SharedNetwork(SharedNetwork.DEFAULT_NETWORK_NAME))
            .withExposedPorts(4444)
        Ryuk.disable()
        container.start()
        return ContainerBrowser(container)
    }

    private class ContainerBrowser(
        private val container: BrowserWebDriverContainerImpl
    ) : Browser {

        override val driver: RemoteWebDriver = container.webDriver

        override fun close() {
            driver.quit()
            container.close()
        }
    }
}

private class BrowserWebDriverContainerImpl : BrowserWebDriverContainer<BrowserWebDriverContainerImpl>()
