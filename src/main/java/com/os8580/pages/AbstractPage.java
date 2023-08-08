package com.os8580.pages;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class AbstractPage {

    protected WebDriver driver;
    private final String URL;

    public AbstractPage(WebDriver driver, String URL) {
        this.driver = driver;
        this.URL = URL;
    }

    public void loadPage() {
        // Load the page by navigating to the URL
        driver.get("about:blank");
        driver.get(URL);
    }

    public boolean isPageLoaded() {
        // Check if the page is fully loaded by verifying the URL
        return new WebDriverWait(driver, Duration.ofSeconds(45L)).until(ExpectedConditions.urlToBe(URL));
    }

    public boolean isTextPresent(String text) {
        // Check if the specified text is present on the page
        WebElement bodyElement = driver.findElement(By.tagName("body"));
        return bodyElement.getText().contains(text);
    }
}
