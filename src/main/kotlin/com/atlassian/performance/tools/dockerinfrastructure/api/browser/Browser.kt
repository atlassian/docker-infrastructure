package com.atlassian.performance.tools.dockerinfrastructure.api.browser

import org.openqa.selenium.remote.RemoteWebDriver

interface Browser : AutoCloseable {
    val driver: RemoteWebDriver
}