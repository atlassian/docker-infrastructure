package com.atlassian.performance.tools.dockerinfrastructure.jira

import org.openqa.selenium.By
import org.openqa.selenium.ElementNotInteractableException
import org.openqa.selenium.StaleElementReferenceException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.ExpectedConditions.*
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URI
import java.time.Duration
import java.time.Duration.ofMinutes
import java.time.Duration.ofSeconds


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
                try {
                    it?.navigate()?.to(uri.toURL())
                    it?.findElements(By.id("jira"))?.firstOrNull()
                } catch (e: Exception) {
                    null
                }
            },
            timeout = ofMinutes(15),
            precision = ofSeconds(4)
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
     *  Uses "10 user Jira Software Data Center license, expires in 3 hours" license
     *  from https://developer.atlassian.com/platform/marketplace/timebomb-licenses-for-testing-server-apps/
     */
    private fun setupLicense() {
        val timeBombLicense = """
AAAB8w0ODAoPeNp9Uk2P2jAQvedXWOoNydmELVKLFKlL4u7SLglKQj+27cEkA3gb7GjssMu/rwnQl
s9DDvHMvPfmvXmTN0BGfE08n3jdftfv927J/SgnXc9/58wRQC5UXQO6j6IAqYGVwgglAxbnLB2nw
4w5cbOcAiaziQbUge85oZKGFybmSwjKmiMKvfjATcW1Fly6hVo64waLBdcQcQPBhot6Per5zo4lX
9fQjofJaMTScHj3uC+x11rgup0b3z7sudiIi+oSWQa4AhxGweD+fU6/Tb68pZ+fnh7owPO/Os8Cu
VujKpvCuJsfqtXMvHAE1+KKFQQGG3A+2cp412XJeQjSHLVkzVQXKOrWn/bljH/nNmslXPa30+nES
U4/Jikdp0k0CfNhEtNJxmwhCBGsFSWZrolZANmhECYLVQISu9gzFIb8WBhT/+zf3MyVe2DOTbWdo
LCd+OWSSBGpDCmFNiimjQGLLDQxihSNNmppU3Yd67c0ILksjhOxqsKU3eUsooPvG4kXUrli/MlF7
dayEU7kb6lepJOxOLAf7XneFmkfCuCp95nh+LdwhfegL8E5l0LzNo4IVlApi0Vy0GZvs9O6b+vHZ
xzBv0toB3Yuk5lCwuualHs8fSD0/3NqdZ48nBd+5bjYilfNdokZr6zmP7TmY5YwLAIUNq8MbmR8G
faV9ulfLz1K+3g9j1YCFDeq7aYROMQbwMIvHimNt7/bJCCIX02nj"""
            .trimIndent()
        driver.wait(ofMinutes(2), elementToBeClickable(By.id("licenseKey"))).let {
            it.click()
            it.sendKeys(timeBombLicense)
        }
        clickAndAwaitTransition(By.className("aui-button-primary"))
    }

    private fun setupAdministratorAccount() {
        driver.wait(ofMinutes(3), visibilityOfElementLocated(By.cssSelector("input[name='fullname']")))
        driver.findElement(By.cssSelector("input[name='fullname']")).sendKeys("Admin Fixer")
        driver.findElement(By.cssSelector("input[name='email']")).sendKeys("admin@fixer.com")
        driver.findElement(By.cssSelector("input[name='username']")).sendKeys("admin")
        driver.findElement(By.cssSelector("input[name='password']")).sendKeys("admin")
        driver.findElement(By.cssSelector("input[name='confirm']")).sendKeys("admin")
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

        driver.wait(ofSeconds(10), visibilityOfElementLocated(By.cssSelector("form#add-project-form")))
        driver.wait(ofMinutes(1), elementToBeClickable(By.cssSelector("input[name='name']"))).sendKeys("Sample")
        driver.wait(ofMinutes(1), elementToBeClickable(By.cssSelector("input[name='key']")))

        waitAndClick(By.className("add-project-dialog-create-button"), ofMinutes(10))

        cleanErrorMessages()
    }

    private fun cleanErrorMessages() {
        driver.wait(ofMinutes(10), visibilityOfElementLocated(By.className("subnavigator-title")))
        val slideOutTime = Duration.ofMillis(1000)
        repeat(10) {
            val closeButtons = driver
                .findElements(By.className("icon-close"))
                .filter { it.isDisplayed && it.isEnabled }
            if (closeButtons.isEmpty()) {
                return@repeat
            } else {
                try {
                    closeButtons.first().click()
                } catch (e: ElementNotInteractableException) {
                }
                Thread.sleep(slideOutTime.toMillis() * 2)
            }
        }
    }

    private fun waitAndClick(by: By, timeout: Duration = ofMinutes(5)) {
        driver.wait(timeout, elementToBeClickable(by)).click()
    }

    private fun clickAndAwaitTransition(by: By, timeout: Duration = ofMinutes(5)) {
        val clickable = driver.wait(timeout, elementToBeClickable(by))
        clickable.click()
        val initialTimeout = ofSeconds(10)
        try {
            driver.wait(initialTimeout, stalenessOf(clickable))
        } catch (e: Exception) {
            try {
                //this both resends the click and executes a staleness check better than stalenessOf
                clickable.click()
            } catch (e: StaleElementReferenceException) {
                // click can happen on a stale element
                return
            }
            driver.wait(timeout, stalenessOf(clickable))
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
