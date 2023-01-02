package org.example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FramesTest {
    private static final String URL = "https://www.selenium.dev/selenium/web/click_frames.html";
    private final String FRAME = "frame[name='source']";
    String INNER_FRAME_CSS = "#source";
    private WebDriver driver;


    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @Test
    void getElementsTextInsideFrame() {
        WebElement frame = driver.findElement(By.cssSelector(FRAME));
        driver.switchTo().frame(frame);
        String title = driver.findElement(By.cssSelector("h1")).getText();
        assertEquals("Testing Clicks", title);

        driver.switchTo().defaultContent();
        assertThrows(NoSuchElementException.class,() -> driver.findElement(By.cssSelector("h1")));
    }

    @Test
    void accessElementsInsideFrameInsideFrame() {
        WebElement frame = driver.findElement(By.cssSelector(FRAME));
        driver.switchTo().frame(frame);
        WebElement innerFrame = driver.findElement(By.cssSelector(INNER_FRAME_CSS));
        driver.switchTo().frame(innerFrame);
        int hrefCount = driver.findElements(By.cssSelector("a")).size();
        assertEquals(2, hrefCount);
    }

    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
