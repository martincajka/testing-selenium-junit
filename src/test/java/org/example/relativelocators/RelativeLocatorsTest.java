package org.example.relativelocators;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/*
    For complex use cases is preferable to use xpath instead of relative locator,
    as it is easier to paste it directly to browser
    also it is most of the time more readable or shorter
*/
public class RelativeLocatorsTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private static final String URL = "https://www.zip-codes.com/search.asp?selectTab=3";

    @BeforeAll
    public static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setUp() {
        driver = new ChromeDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(30));
        driver.manage().window().maximize();
    }

    @Test
    public void zipCodeTest() {
        driver.get(URL);
//  standard way of finding element using xpath
        WebElement cityInputStandard = driver.findElement(By.xpath("//label[text()='Town/City:']/following-sibling::input"));
//  defining same element using RelativeLocators
        WebElement cityInput = driver.findElement(RelativeLocator.with(By.tagName("input")).toLeftOf(By.xpath("//label[text()='Town/City:']")));
        cityInput.sendKeys("New York");

        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//p[text()='Consent']"))).click();

        WebElement submitBtnStandard = driver.findElement(By.xpath("//h4[text()='Find ZIP Codes by City, State, Address, or Area Code']//following-sibling::form//input[@name='Submit']"));
        WebElement submitBtn = driver.findElement(RelativeLocator.with(By.xpath("//input[@name='Submit']")).below(By.xpath("//h4[text()='Find ZIP Codes by City, State, Address, or Area Code']")));

        submitBtn.click();

        List<String> zipTableLinks = driver.findElements(By.xpath("//*[@class='statTable']/tbody/tr/td[1]/a")).stream()
                .limit(3)
                .map(a -> String.format("https://www.zip-codes.com/%s", a.getAttribute("href")))
                .toList();

        List<ZipInfo> zipInfos = new ArrayList<>();

        zipTableLinks.forEach(link -> {
            driver.navigate().to(link);
            zipInfos.add(new ZipInfo(findZipDataByLabel("City"), findZipDataByLabel("State"), findZipDataByLabel("Zip Code"), findZipDataByLabel("Longitude"), findZipDataByLabel("Latitude")));
            driver.navigate().back();
        });

        System.out.println(zipInfos);
    }

    private String findZipDataByLabel(String label) {
        return driver.findElement(By.xpath(String.format("//span[text()='%s:']/parent::td//following-sibling::td", label))).getText();
//       return driver.findElement(RelativeLocator.with(By.xpath("td")).toRightOf(By.xpath("//span[text()='" + label + "']"))).getText();
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
