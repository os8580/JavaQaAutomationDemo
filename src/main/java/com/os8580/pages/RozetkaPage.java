package com.os8580.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class RozetkaPage extends AbstractPage {

    private static final String URL = "https://rozetka.com.ua/";

    public RozetkaPage(WebDriver driver) {
        super(driver, URL);
        this.driver = driver;
    }

    public void setSearchValue(String value) {
        // Set the search value in the search input field
        driver.findElement(By.name("search")).sendKeys(value);
    }

    public void performSearch() {
        // Submit the search form
        driver.findElement(By.name("search")).sendKeys(Keys.ENTER);
    }

    public List<WebElement> getSearchResults() {
        // Get the list of search results on the page
        List<WebElement> searchLinks = new ArrayList<>();

        List<WebElement> elements = new WebDriverWait(driver, Duration.ofSeconds(5L))
                .until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.cssSelector(".goods-tile")));

        for (WebElement e : elements) {
            try {
                if (e.isDisplayed()) {
                    searchLinks.add(e);
                }
            } catch (StaleElementReferenceException ignored) {
                // Continue the loop if the element becomes stale
            }
        }

        return searchLinks;
    }

    public String getProductName() {
        // Get the name of the first product in the search results
        WebElement firstProduct = getSearchResults().get(0);
        WebElement productNameElement = firstProduct.findElement(By.cssSelector(".goods-tile__title"));
        return productNameElement.getText();
    }

    public String getProductPrice() {
        // Get the price of the first product in the search results
        WebElement firstProduct = getSearchResults().get(0);
        WebElement productPriceElement = firstProduct.findElement(By.cssSelector(".goods-tile__price"));
        return productPriceElement.getText();
    }

    public void clickAddToCartButton() {
        // Click the "Add to Cart" button of the first product in the search results
        WebElement firstProduct = getSearchResults().get(0);
        WebElement addToCartButton = firstProduct.findElement(By.cssSelector(".catalog-grid__cell:nth-child(1) .goods-tile__prices:nth-child(7) svg:nth-child(1)"));

        // Explicitly wait for the button to be clickable before clicking
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton));

        addToCartButton.click();
    }

    public void openCart() {
        // Open the cart by clicking the cart button
        driver.findElement(By.cssSelector(".catalog-grid__cell:nth-child(1) .goods-tile__prices:nth-child(7) svg:nth-child(1)")).click();
    }

    public void waitForTextVisibility(String text) {
        // Wait for the specified text to be visible on the page
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[contains(text(), '" + text + "')]")));
    }

    public String getCartProductName() {
        // Get the name of the product in the cart
        WebElement productNameElement = driver.findElement(By.cssSelector(".cart-product__title"));
        return productNameElement.getText();
    }

    public String getCartProductPrice() {
        // Get the price of the product in the cart with explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".cart-product__price")));
        WebElement productPriceElement = driver.findElement(By.cssSelector(".cart-product__price"));
        return productPriceElement.getText();
    }

    public void increaseProductQuantityByOne() {
        String initialPrice = getCartTotalPrice();
        WebElement quantityIncreaseButton = driver.findElement(By.cssSelector("button[data-testid='cart-counter-increment-button']"));
        quantityIncreaseButton.click();
        waitForCartTotalPriceToChange(initialPrice);
    }

    public void decreaseProductQuantityByOne() {
        String initialPrice = getCartTotalPrice();
        WebElement quantityDecreaseButton = driver.findElement(By.cssSelector("button[data-testid='cart-counter-decrement-button']"));
        quantityDecreaseButton.click();
        waitForCartTotalPriceToChange(initialPrice);
    }

    public void setProductQuantity(int quantity) {
        WebElement quantityInput = driver.findElement(By.cssSelector("input[data-testid='cart-counter-input']"));
        String initialQuantity = quantityInput.getAttribute("value");
        quantityInput.clear();  // Clear the existing quantity
        quantityInput.sendKeys(Integer.toString(quantity));  // Set the new quantity

        // Wait till the new quantity is reflected in the input box.
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30L));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector("input[data-testid='cart-counter-input']"), initialQuantity)));

        String initialPrice = getCartTotalPrice();

        // Wait for the cart total price to change from the initial price
        wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector("div[class*='sum-price']"), initialPrice)));
    }

    public int getProductQuantity() {
        // Get the quantity of the product in the cart
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-testid='cart-counter-input']")));

        WebElement quantityElement = driver.findElement(By.cssSelector("[data-testid='cart-counter-input']"));
        String quantityText = quantityElement.getAttribute("value");
        return Integer.parseInt(quantityText);
    }

    public int getSingleProductPrice() {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("div[class='cart-receipt__sum-price']")));

        WebElement productPriceElement = driver.findElement(By.cssSelector("div[class='cart-receipt__sum-price']"));
        String productPriceText = productPriceElement.getText().replaceAll("\\D+", ""); // Remove non-numeric characters
        return Integer.parseInt(productPriceText) / getProductQuantity();
    }

    private String getCartTotalPrice() {
        // Get the total price in the cart
        WebElement totalPriceElement = driver.findElement(By.cssSelector("div[class*='sum-price']"));
        return totalPriceElement.getText();
    }

    private void waitForCartTotalPriceToChange(String initialPrice) {
        // Wait for the cart total price to change from the initial price
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10L));
        wait.until(ExpectedConditions.not(ExpectedConditions.textToBe(By.cssSelector("div[class*='sum-price']"), initialPrice)));
    }
}
