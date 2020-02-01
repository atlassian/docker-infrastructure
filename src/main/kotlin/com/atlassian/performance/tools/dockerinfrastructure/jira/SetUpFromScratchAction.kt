package com.atlassian.performance.tools.dockerinfrastructure.jira

import org.openqa.selenium.*
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration


/**
 * The class can be adapted to implement `com.atlassian.performance.tools.jiraactions.api.action.Action`.
 */
internal class SetUpFromScratchAction(
    private val uri: URI,
    private val driver: WebDriver
) {

    internal fun run() {
        waitForJira()
        chooseSetupMode()
        setupDatabase()
        setupWizard()
        setupLicense()
        setupAdministratorAccount()
        setupEmailNotifications()
        setupLanguage()
        setupAvatar()
        createSampleProject()
    }

    private fun waitForJira() {
        driver.wait(
            condition = ExpectedCondition {
                it!!
                return@ExpectedCondition try {
                    it.navigate().to(uri.toURL())
                    it.findElements(By.id("jira")).firstOrNull()
                } catch (e: Exception) {
                    null
                }
            },
            timeout = Duration.ofMinutes(15),
            precision = Duration.ofSeconds(4)
        )
    }

    private fun chooseSetupMode() {
        waitAndClick(By.cssSelector("div[data-choice-value='classic']"))
        clickAndAwaitTransition(By.id("jira-setup-mode-submit"))
    }

    private fun setupDatabase() {
        clickAndAwaitTransition(By.id("jira-setup-database-submit"))
    }

    private fun setupWizard() {
        clickAndAwaitTransition(By.id("jira-setupwizard-submit"))
    }

    /**
     *  Uses "10 user starter non-eval host product license, expires in 3 hours" license
     *  from https://developer.atlassian.com/platform/marketplace/timebomb-licenses-for-testing-server-apps/.
     */
    private fun setupLicense() {
        val timebombLicense = """
            AAABiQ0ODAoPeNp1kk9TwjAQxe/9FJnxXKYpeoCZHqCtgsqfgaIO4yWELURD0tm0KN/eWOjYdvD68
            vbtb3dzM9GKTBgS2iOU9n3a7/pkHiXE96jvbNhho3XnWXBQBuKtyIVWQTxN4sV8MV7GTirMHk5QO
            ZJTBsG91eITvPdJBEeQOgN0uNRHwIYtLKWGa1ocNoCzdGUATUA9h2uVdhjPxRGCHAtw5gXyPTMQs
            RwCn1Lf9XzXv3NqwVN2gGCZDBYWstLj70zgqSyad0fVWPXgJaClGUfB8KGXuG+rl1v3ab0euUOPv
            jofAlmD/XG8GJBY5YAZCtMa9Ze5MagVZAGKX/FVE4eyMDZtqrdgAq+19zJlWEr/Na0TXjkTx4KLj
            WzeKbyIjaAJE7aDYpa2tTSO+mvbCrBKo/ryate4Up9KfylnhjumhGEl0SCXzBjB1B9Q/QYhQulrH
            /fcue6svl1di8BwFFnZKAGTE3mGIalGksliJxTZVqTmvLF6fXxksjhzpkwaqP5s3fMDBMYhRDAtA
            hUAhcR3uL05YCxbclq7h1dNa+Nc+j4CFBrdN005oVlMN9yBlWeM4TlnrOhqX02j3"""
            .trimIndent()
        val licenseKeyLocator = By.id("licenseKey")
        val licenceKeyInput = 
            driver.wait(Duration.ofMinutes(2), ExpectedConditions.elementToBeClickable(licenseKeyLocator))
        licenceKeyInput.click()
        licenceKeyInput.sendKeys(timebombLicense)
        clickAndAwaitTransition(By.className("aui-button-primary"))
    }

    private fun setupAdministratorAccount() {
        val fullnameLocator = By.cssSelector("input[name='fullname']")
        val emailLocator = By.cssSelector("input[name='email']")
        val usernameLocator = By.cssSelector("input[name='username']")
        val passwordLocator = By.cssSelector("input[name='password']")
        val confirmLocator = By.cssSelector("input[name='confirm']")

        driver.wait(Duration.ofMinutes(3), ExpectedConditions.visibilityOfElementLocated(fullnameLocator))
        driver.findElement(fullnameLocator).sendKeys("Admin Fixer")
        driver.findElement(emailLocator).sendKeys("admin@fixer.com")
        driver.findElement(usernameLocator).sendKeys("admin")
        driver.findElement(passwordLocator).sendKeys("admin")
        driver.findElement(confirmLocator).sendKeys("admin")
        driver.findElement(By.id("jira-setupwizard-submit")).click()
    }

    private fun setupEmailNotifications() {
        clickAndAwaitTransition(By.id("jira-setupwizard-submit"))
    }

    private fun setupLanguage() {
        clickAndAwaitTransition(By.id("next"))
    }

    private fun setupAvatar() {
        clickAndAwaitTransition(By.className("avatar-picker-done"))
    }

    private fun createSampleProject() {
        waitAndClick(By.id("sampleData"))
        waitAndClick(By.className("create-project-dialog-create-button"))

        val projectNameInputLocator = By.id("name")
        driver.wait(Duration.ofMinutes(1), ExpectedConditions.visibilityOfElementLocated(projectNameInputLocator))
        driver.findElement(projectNameInputLocator).sendKeys("Sample")

        val addProjectButtonLocator = By.className("add-project-dialog-create-button")
        driver.wait(Duration.ofMinutes(10), ExpectedConditions.elementToBeClickable(addProjectButtonLocator))
        driver.findElement(addProjectButtonLocator).click()

        cleanErrorMessages()
    }

    private fun cleanErrorMessages() {
        driver.wait(Duration.ofMinutes(10), ExpectedConditions.visibilityOfElementLocated(By.className("subnavigator-title")))
        val slideOutTime = Duration.ofMillis(1000)
        for (i in 1..10) {
            val closeButtons = driver
                .findElements(By.className("icon-close"))
                .filter { it.isDisplayed && it.isEnabled }
            if (closeButtons.isEmpty()) {
                break
            } else {
                try {
                    closeButtons.first().click()
                } catch (e: ElementNotInteractableException) {
                }
                Thread.sleep(slideOutTime.toMillis() * 2)
            }
        }
    }

    private fun waitAndClick(by: By, timeout: Duration = Duration.ofMinutes(5)) {
        driver.wait(timeout, ExpectedConditions.elementToBeClickable(by))
            .click()
    }
    
    private fun clickAndAwaitTransition(by: By, timeout: Duration = Duration.ofMinutes(5)) {
        val clickable = driver.wait(timeout, ExpectedConditions.elementToBeClickable(by))
        clickable.click()
        val initialTimeout = Duration.ofSeconds(10)
        try {
            driver.wait(initialTimeout, ExpectedConditions.stalenessOf(clickable))
        } catch (e: Exception) {
            try {
                //this both resends the click and executes a staleness check better than stalenessOf
                clickable.click()
            } catch (e: StaleElementReferenceException) {
                // click can happen on a stale element
                return
            }
            driver.wait(timeout, ExpectedConditions.stalenessOf(clickable))
        }
    }
}

fun <T> WebDriver.wait(
    timeout: Duration,
    condition: ExpectedCondition<T>,
    precision: Duration = Duration.ofMillis(50)
): T {
    return WebDriverWait(
        this,
        timeout.seconds,
        precision.toMillis()
    ).until(condition)
}
