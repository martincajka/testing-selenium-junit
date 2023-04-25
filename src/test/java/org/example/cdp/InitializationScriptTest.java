package org.example.cdp;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.events.EventFiringDecorator;
import org.openqa.selenium.support.events.WebDriverListener;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.lang.reflect.Method;
import java.time.Duration;

public class InitializationScriptTest {
    private WebDriver driver;
    private WebDriver decoratedDriver;
    private WebDriverWait wait;
    private Actions actions;
    private final int WAIT_FOR_ELEMENT_TIMEOUT = 30;

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        DevTools devTools = ((ChromeDriver) driver).getDevTools();
        devTools.createSession();
        devTools.getDomains().javascript().pin("notifications", """
                 window.onload = () => {
                             if (!window.jQuery) {
                                 var jquery = document.createElement('script');\s
                                 jquery.type = 'text/javascript';
                                 jquery.src = 'https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js';
                                 document.getElementsByTagName('head')[0].appendChild(jquery);
                             } else {
                                 $ = window.jQuery;
                             }
                 
                             $.getScript('https://cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.8/jquery.jgrowl.min.js')
                             $('head').append('<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/jquery-jgrowl/1.4.8/jquery.jgrowl.min.css" type="text/css" />');
                         }
                        
                         function highlight(element){
                             let defaultBG = element.style.backgroundColor;
                             let defaultOutline = element.style.outline;
                             element.style.backgroundColor = '#FDFF47';
                             element.style.outline = '#f00 solid 2px';
                             setTimeout(function()
                             {
                                 element.style.backgroundColor = defaultBG;
                                 element.style.outline = defaultOutline;
                             }, 1000);
                         }
                """);

        WebDriverListener listener = new WebDriverListener() {

            @Override
            public void beforeAnyWebElementCall(WebElement element, Method method, Object[] args) {
                try {
                    growlMessage(String.format("Calling %s on %s", method.getName(), element.getTagName()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterAnyWebElementCall(WebElement element, Method method, Object[] args, Object result) {
                try {
                    growlMessage(String.format("Called %s on %s", method.getName(), element.getTagName()));
                    highlightElement(element);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void beforeAnyWebDriverCall(WebDriver driver, Method method, Object[] args) {
                try {
                    growlMessage(String.format("Webdriver Calling %s", method.getName()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void afterAnyWebDriverCall(WebDriver driver, Method method, Object[] args, Object result) {
                try {
                    growlMessage(String.format("Webdriver Called %s", method.getName()));
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        this.decoratedDriver = new EventFiringDecorator(listener).decorate(driver);
        decoratedDriver.manage().window().maximize();
        wait = new WebDriverWait(decoratedDriver, Duration.ofSeconds(WAIT_FOR_ELEMENT_TIMEOUT));
        actions = new Actions(decoratedDriver);
    }

    @Test
    public void verifyToDoListCreatedSuccessfully_noParams() {
        decoratedDriver.navigate().to("https://todomvc.com/");
        openTechnologyApp("Backbone.js");
        addNewToDoItem("Item 1");
        addNewToDoItem("Item 2");
        addNewToDoItem("Item 3");
        getItemCheckbox("Item 3").click();

        assertLeftItems(2);
    }

    private void addNewToDoItem(String todoItem) {
        WebElement todoInput = waitAndFindElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys(todoItem);
        actions.click(todoInput).sendKeys(Keys.ENTER).perform();
    }

    private void openTechnologyApp(String technology) {
        WebElement technologyLink = waitAndFindElement(By.linkText(technology));
        technologyLink.click();
    }

    private WebElement waitAndFindElement(By by) {
        return wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

    private WebElement getItemCheckbox(String todoItem) {
        return waitAndFindElement(By.xpath(String.format("//label[text()='%s']/preceding-sibling::input", todoItem)));
    }

    private void assertLeftItems(int expectedCount) {
        WebElement resultElement = waitAndFindElement(By.cssSelector(".todo-count"));
        String expectedText = expectedCount == 1 ? "%d item left" : "%d items left";
        validateInnerTextIs(resultElement, String.format(expectedText, expectedCount));
    }

    private void validateInnerTextIs(WebElement element, String expectedText) {
        wait.until(driver -> element.getText().equals(expectedText));
    }

    private void growlMessage(String message) throws InterruptedException {
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript(String.format("$.jGrowl('%s', { header: 'Important' });", message));
    }

    private void highlightElement(WebElement element) {
        try {
            ((JavascriptExecutor) driver).executeScript("highlight(arguments[0])", element);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
