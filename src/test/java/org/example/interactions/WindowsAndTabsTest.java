package org.example.interactions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class WindowsAndTabsTest {
    private WebDriver driver;
    private static final String URL = "https://www.selenium.dev/documentation/webdriver/interactions/windows/";
    private String originalWindow;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
        originalWindow = driver.getWindowHandle();
        assertEquals(1, driver.getWindowHandles().size());
    }


    @Test
    void openAndCloseNewTabTest() {
        driver.findElement(By.linkText("new window")).click();
        assertEquals(2, driver.getWindowHandles().size());

        for (String handle : driver.getWindowHandles()) {
            if (!handle.contentEquals(originalWindow)) {
                driver.switchTo().window(handle);
                break;
            }
        }

    }

    @ParameterizedTest
    @EnumSource(names = {"TAB", "WINDOW"})
    void openNewTabAndAutomaticallySwitchToIt(WindowType type) {
        driver.switchTo().newWindow(type);
        assertEquals(2, driver.getWindowHandles().size());
        assertEquals("", driver.getTitle());

        driver.close();
        assertThrows(NoSuchWindowException.class, () -> driver.getTitle());

        driver.switchTo().window(originalWindow);
        assertEquals("Working with windows and tabs | Selenium", driver.getTitle());
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
