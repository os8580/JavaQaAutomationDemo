package com.os8580.pages;

//import com.beust.ah.A;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GooglePage extends AbstractPage {

    private static final String URL = "https://google.com/";

    public GooglePage(WebDriver driver) {
        super(driver, URL);
        this.driver = driver;
    }

    public void acceptCookiesIfPresent() {
        try {
            List<WebElement> buttons = new WebDriverWait(driver, Duration.ofSeconds(5L))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.tagName("button")));

            if (buttons.size() == 5) {
                buttons.get(3).click();
            }
        } catch (TimeoutException e) {
            //
        }
    }

    public void setSearchValue(String value) {
        // Enter the search value in the search input field
        driver.findElement(By.name("q")).sendKeys(value);
    }

    public void performSearch() {
        // Press the Enter key to perform the search
        driver.findElement(By.name("q")).sendKeys(Keys.ENTER);
    }

    public List<WebElement> getSearchResults() {
        // Get all the search result elements on the page
        List<WebElement> elements = new WebDriverWait(driver, Duration.ofSeconds(5L))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.xpath("//h3/..")));

        List<WebElement> searchLinks = new ArrayList<>();

        // Iterate through the elements and add the displayed ones to the searchLinks list
        for (WebElement e : elements) {
            if (e.isDisplayed()) {
                searchLinks.add(e);
            }
        }

        return searchLinks;
    }
}
