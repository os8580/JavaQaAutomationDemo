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

    // Navigate to the specified URL
    private void navigateToUrl(String URL) {
        driver.get(URL);
    }

    public void loadPage() {
        navigateToUrl("about:blank");
        navigateToUrl(this.URL);
    }

    public boolean isPageLoaded() {
        return new WebDriverWait(driver, Duration.ofSeconds(45L)).until(ExpectedConditions.urlToBe(URL));
    }

    public boolean isTextPresent(String text) {
        WebElement bodyElement = driver.findElement(By.tagName("body"));
        return bodyElement.getText().contains(text);
    }
}