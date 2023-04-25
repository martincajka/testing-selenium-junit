package org.example.interactions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

import static org.junit.jupiter.api.Assertions.*;

public class ActionKeysTest {

    private WebDriver driver;
    private static final String URL = "https://www.selenium.dev/selenium/web/single_text_input.html";
    private static final String TEXT_INPUT = "#textInput";

    private WebElement textField;


    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
        textField = driver.findElement(By.cssSelector(TEXT_INPUT));
    }

    @Test
    void sendingKeys(){
        new Actions(driver)
                .keyDown(Keys.SHIFT)
                .sendKeys("a")
                .keyUp(Keys.SHIFT)
                .sendKeys("b")
                .perform();

        assertEquals("Ab",textField.getAttribute("value"));
    }

    @Test
    void copyAndPasteActionTest(){
        Keys cmdCtrl = Platform.getCurrent().is(Platform.MAC) ? Keys.COMMAND : Keys.CONTROL;

        new Actions(driver)
                .sendKeys(textField, "Hello World")
                .keyDown(Keys.SHIFT)
                .keyDown(Keys.ARROW_UP)
                .keyUp(Keys.SHIFT)
                .keyDown(cmdCtrl)
                .sendKeys("xvv")
                .keyUp(cmdCtrl)
                .perform();

        assertEquals("Hello WorldHello World",textField.getAttribute("value"));

    }
    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
