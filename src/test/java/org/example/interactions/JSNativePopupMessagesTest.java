package org.example.interactions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class JSNativePopupMessagesTest {
    public static final String ALERT_MSG = "Sample alert";
    public static final String CONFIRM_MSG = "Are you sure?";
    WebDriver driver;
    FluentWait<WebDriver> wait;
    private static final String URL = "https://www.selenium.dev/documentation/webdriver/interactions/alerts/";

    @BeforeEach
    void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
        wait = new FluentWait<>(driver)
                .ignoring(NoSuchElementException.class)
                .withTimeout(Duration.ofMillis(500))
                .pollingEvery(Duration.ofMillis(100));
    }

    @Test
    void nativeJSAlertTest() {
        driver.findElement(By.linkText("See an example alert")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        String alertMsg = alert.getText();
        assertEquals(ALERT_MSG, alertMsg);

        alert.accept();
        assertFalse(isAlertPresent());
    }

    @Test
    void nativeJSConfirmBox() {
        driver.findElement(By.linkText("See a sample confirm")).click();
        wait.until(ExpectedConditions.alertIsPresent());
        Alert alert = driver.switchTo().alert();
        String alertMsg = alert.getText();
        assertEquals(CONFIRM_MSG, alertMsg);

        alert.dismiss();
        assertFalse(isAlertPresent());
    }

    @Test
    void nativeJSPrompt() {
        driver.findElement(By.linkText("See a sample prompt")).click();
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.sendKeys("Hello world");

        alert.accept();
        assertFalse(isAlertPresent());
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }

    private boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }


}
