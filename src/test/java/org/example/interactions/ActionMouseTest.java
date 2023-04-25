package org.example.interactions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ActionMouseTest {

    private WebDriver driver;
    private static final String URL = "https://www.selenium.dev/selenium/web/mouse_interaction.html";
    private static final String NEW_PAGE_REF = "#click";
    private static final String CLICKABLE_FIELD = "#clickable";
    private static final String CLICKABLE_FIELD_STATUS = "#click-status";
    private static final String DRAGGABLE_ITEM = "#draggable";
    private static final String DROPPABLE_BOX = "#droppable";
    private static final String DRAG_N_DROP_STATUS = "#drop-status";
    private static final String HOVERABLE_ELEMENT = "#hover";
    private static final String HOVER_STATUS = "#move-status";


    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get(URL);
    }

    @Test
    void leftClickWithMouseTest() {
        new Actions(driver)
                .click(driver.findElement(By.cssSelector(CLICKABLE_FIELD)))
                .perform();

        assertEquals("focused", driver.findElement(By.cssSelector(CLICKABLE_FIELD_STATUS)).getText());
    }

    @Test
    void rightClickWithMouseTest() {
        new Actions(driver)
                .contextClick(driver.findElement(By.cssSelector(CLICKABLE_FIELD)))
                .perform();

        assertEquals("context-clicked", driver.findElement(By.cssSelector(CLICKABLE_FIELD_STATUS)).getText());
    }

    @Test
    void doubleClickWithMouseTest() {
        new Actions(driver)
                .doubleClick(driver.findElement(By.cssSelector(CLICKABLE_FIELD)))
                .perform();

        assertEquals("double-clicked", driver.findElement(By.cssSelector(CLICKABLE_FIELD_STATUS)).getText());
    }

    @Test
    void hoveringWithMouseTest() {
        new Actions(driver)
                .moveToElement(driver.findElement(By.cssSelector(HOVERABLE_ELEMENT)))
                .perform();

        assertEquals("hovered", driver.findElement(By.cssSelector(HOVER_STATUS)).getText());
    }

    @Test
    void dragAndDropTest() {
        WebElement draggable = driver.findElement(By.cssSelector(DRAGGABLE_ITEM));
        WebElement droppable = driver.findElement(By.cssSelector(DROPPABLE_BOX));
        new Actions(driver)
                .dragAndDrop(draggable, droppable)
                .perform();

        assertEquals("dropped", driver.findElement(By.cssSelector(DRAG_N_DROP_STATUS)).getText());
    }

    @Test
    void backClickOnMouseTest() {
        WebElement newPage = driver.findElement(By.cssSelector(NEW_PAGE_REF));
        newPage.click();
        assertEquals("We Arrive Here", driver.getTitle());

        PointerInput mouse = new PointerInput(PointerInput.Kind.MOUSE, "default mouse");

        Sequence actions = new Sequence(mouse, 0)
                .addAction(mouse.createPointerDown(PointerInput.MouseButton.BACK.asArg()))
                .addAction(mouse.createPointerUp(PointerInput.MouseButton.BACK.asArg()));
        ((RemoteWebDriver) driver).perform(Collections.singletonList(actions));

        assertEquals("BasicMouseInterfaceTest", driver.getTitle());
    }


    @AfterEach
    void tearDown() {
        driver.quit();
    }
}
