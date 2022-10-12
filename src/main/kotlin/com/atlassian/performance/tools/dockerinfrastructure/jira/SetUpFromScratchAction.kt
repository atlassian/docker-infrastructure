package com.atlassian.performance.tools.dockerinfrastructure.jira

import org.openqa.selenium.By
import org.openqa.selenium.ElementNotInteractableException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
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
     *  Uses "3 hour expiration for all Atlassian host products*" license
     *  from https://developer.atlassian.com/platform/marketplace/timebomb-licenses-for-testing-server-apps/
     */
    private fun setupLicense() {
        val timebombLicense = """
AAACLg0ODAoPeNqNVEtv4jAQvudXRNpbpUSEx6FIOQBxW3ZZiCB0V1WllXEG8DbYke3A8u/XdUgVQ
yg9ZvLN+HuM/e1BUHdGlNvuuEHQ73X73Y4bR4nbbgU9ZwFiD2IchcPH+8T7vXzuej9eXp68YSv45
UwoASYhOeYwxTsIE7RIxtNHhwh+SP3a33D0XnntuxHsIeM5CIdwtvYxUXQPoRIF6KaC0FUGVlEB3
v0hOAOWYiH9abFbgZith3i34nwOO65gsAGmZBhUbNC/nIpjhBWEcefJWelzqIDPWz/OtjmXRYv2X
yqwnwueFkT57x8e4cLmbCD1QnX0UoKQoRc4EUgiaK4oZ2ECUrlZeay75sLNs2JDmZtWR8oPCfWZG
wHAtjzXgIo0SqmZiKYJmsfz8QI5aI+zApuq6fqJKVPAMCPnNpk4LPW6kBWgkZb+kQAzzzS2g6Dnt
e69Tqvsr4SOskIqEFOeggz1v4zrHbr0yLJR8rU64FpQpVtBy1mZxM4CnHC9Faf8tKMnTF1AiXORF
ixyQaWto3RZ+ncWLXtMg6EnKZZRpmQNb2R8tnJXFulCfXmXLry7TrHBWn2HNVyH8WYxj9AzmsxiN
L/R88Xg6rA1lVs4QpO5titxhplJcCY2mFFZLutAZVhKipm15/VhJx36YVqyN8YP7IaGC1+lwnJ7Q
5pJpNmxk5hP3qovutY8Pi4E2WIJ59esnr1p+T6eD67teBVCHf+ga+ho4/4D9YItZDAsAhQ5qQ6pA
SJ+SA7YG9zthbLxRoBBEwIURQr5Zy1B8PonepyLz3UhL7kMVEs=X02q6"""
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
