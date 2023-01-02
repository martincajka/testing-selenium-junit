package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for simple App.
 */
public class BrowserNavigationTest {
    WebDriver driver;
    public static final String URL = "https://www.selenium.dev/selenium/web/web-form.html";
    public static final String URL_TITLE = "Web form";
    public static final String OTHER_URL = "https://www.selenium.dev/selenium/web/index.html";
    public static final String OTHER_URL_TITLE = "Index of Available Pages";


    @BeforeEach
    void init() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
    }

    @Test
    public void successfulNavigationToUrl() {
        driver.get(URL);
        assertEquals(URL_TITLE, driver.getTitle());
    }

    @Test
    public void navigatingBackAndForward() {
        driver.get(URL);
        driver.navigate().to(OTHER_URL);
        assertEquals(OTHER_URL_TITLE, driver.getTitle());

        driver.navigate().back();
        assertEquals(URL_TITLE, driver.getTitle());

        driver.navigate().forward();
        assertEquals(OTHER_URL_TITLE, driver.getTitle());
    }

    @Test
    public void refreshingPage() {
        driver.get(URL);
        driver.navigate().refresh();
        assertEquals(URL_TITLE,driver.getTitle());
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
