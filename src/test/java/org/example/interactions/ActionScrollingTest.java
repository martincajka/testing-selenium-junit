package org.example.interactions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ActionScrollingTest {

    private WebDriver driver;
    private static final String URL = "https://www.selenium.dev/selenium/web/scrolling_tests/frame_with_nested_scrolling_frame_out_of_view.html";
    private static final String FOOTER = "footer";

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @Test
    void scrollToElementTest() {
        WebElement footer = driver.findElement(By.cssSelector(FOOTER));

        new Actions(driver)
                .scrollToElement(footer)
                .perform();

        assertTrue(inViewport(footer));
    }


    @AfterEach
    void tearDown() {
        driver.quit();
    }

    private boolean inViewport(WebElement element) {

        String script =
                "for(var e=arguments[0],f=e.offsetTop,t=e.offsetLeft,o=e.offsetWidth,n=e.offsetHeight;\n"
                        + "e.offsetParent;)f+=(e=e.offsetParent).offsetTop,t+=e.offsetLeft;\n"
                        + "return f<window.pageYOffset+window.innerHeight&&t<window.pageXOffset+window.innerWidth&&f+n>\n"
                        + "window.pageYOffset&&t+o>window.pageXOffset";

        return (boolean) ((RemoteWebDriver) driver).executeScript(script, element);
    }
}
