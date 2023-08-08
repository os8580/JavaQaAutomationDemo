package com.os8580.utils;

import com.os8580.pages.AbstractPage;
import com.os8580.pages.GooglePage;
import com.os8580.pages.Pages;
import com.os8580.pages.RozetkaPage;
import org.openqa.selenium.WebDriver;

public class PageFactory {

    private final WebDriver driver;

    public PageFactory(WebDriver driver) {
        this.driver = driver;
    }

    public AbstractPage get(Pages pages) {
        // Create and return an instance of the requested page based on the provided enum value
        switch (pages) {
            case GOOGLE:
                return new GooglePage(driver);
            case ROZETKA:
                return new RozetkaPage(driver);
            default:
                throw new RuntimeException("Unsupported page requested");
        }
    }

    public void tearDown() {
        // Quit the WebDriver instance to clean up resources
        driver.quit();
    }
}
