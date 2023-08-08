package com.os8580;

import com.os8580.utils.PageFactory;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import com.os8580.pages.*;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RozetkaTestSuite {

    private PageFactory pageFactory;

    @BeforeSuite
    public void setupDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized"); // Add argument to start Chrome in maximized window
        pageFactory = new PageFactory(new ChromeDriver(options));
    }

    @Test
    public void testGoogleUsingPageObjectWithChrome() {
        // Create instances of page objects
        GooglePage googlePage = (GooglePage) pageFactory.get(Pages.GOOGLE);
        RozetkaPage rozetkaPage = (RozetkaPage) pageFactory.get(Pages.ROZETKA);

        // Perform Google search
        googlePage.loadPage();
        googlePage.acceptCookiesIfPresent();
        googlePage.setSearchValue("rozetka ua");
        googlePage.performSearch();
        List<WebElement> searchResults = googlePage.getSearchResults();
        Assert.assertTrue(searchResults.size() > 1, "No search results found!");
        searchResults.get(0).click();

        // Perform Rozetka search
        Assert.assertTrue(rozetkaPage.isPageLoaded());
        rozetkaPage.setSearchValue("Iphone");
        rozetkaPage.performSearch();

        List<WebElement> searchResultsRozetka = rozetkaPage.getSearchResults();
        Assert.assertFalse(searchResultsRozetka.isEmpty(), "No search results found on Rozetka page!");

        // Get product details from the page
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("name", rozetkaPage.getProductName());
        String productPrice = rozetkaPage.getProductPrice().replaceAll("\\D", ""); // Remove non-numeric characters
        productDetails.put("price", Integer.parseInt(productPrice));

        // Add the product to the cart
        rozetkaPage.clickAddToCartButton();
        rozetkaPage.waitForTextVisibility("Товар добавлен в корзину");
        rozetkaPage.openCart();
        rozetkaPage.waitForTextVisibility("Вместе дешевле");
        Assert.assertTrue(rozetkaPage.isTextPresent("33 499"), "Text '33 499' not found on the page.");
        Assert.assertTrue(rozetkaPage.isTextPresent("(MLPF3HU/A)"), "Text '(MLPF3HU/A)' not found on the page.");

        // Get product details from the cart
        Map<String, Object> productDetailsInCart = new HashMap<>();
        productDetailsInCart.put("name", rozetkaPage.getCartProductName());
        String productPriceInCart = rozetkaPage.getCartProductPrice().replaceAll("\\D+", ""); // Remove non-numeric characters
        productDetailsInCart.put("price", Integer.parseInt(productPriceInCart));

        // Verify that product details in the cart match the selected product details
        Assert.assertEquals(productDetailsInCart, productDetails, "Product details in the cart do not match the selected product details!");

        // Get initial quantity and price of the product
        int initialQuantity = rozetkaPage.getProductQuantity();
        int initialPrice = rozetkaPage.getProductPriceNumeric();

        // Increase product quantity by 1
        rozetkaPage.increaseProductQuantityByOne();
        int increasedQuantity = rozetkaPage.getProductQuantity();
        int increasedPrice = rozetkaPage.getProductPriceNumeric();

        // Verify that product quantity increased by 1 and price increased after adding another instance
        Assert.assertEquals(increasedQuantity, initialQuantity + 1, "Product quantity did not increase by 1.");
        Assert.assertTrue(increasedPrice > initialPrice, "Product price did not increase after adding another instance.");

        // Decrease product quantity by 1
        rozetkaPage.decreaseProductQuantityByOne();
        int decreasedQuantity = rozetkaPage.getProductQuantity();
        int decreasedPrice = rozetkaPage.getProductPriceNumeric();

        // Verify that product quantity decreased by 1 and price returned to the initial value after removing an instance
        Assert.assertEquals(decreasedQuantity, initialQuantity, "Product quantity did not decrease by 1.");
        Assert.assertEquals(decreasedPrice, initialPrice, "Product price did not return to the initial value after removing an instance.");

        // Increase product quantity by 3
        rozetkaPage.increaseProductQuantityByOne();
        rozetkaPage.increaseProductQuantityByOne();
        rozetkaPage.increaseProductQuantityByOne();
        int increasedQuantityByThree = rozetkaPage.getProductQuantity();
        int increasedPriceByThree = rozetkaPage.getProductPriceNumeric();

        // Verify that product quantity increased by 3 and price is equal to the expected value
        Assert.assertEquals(increasedQuantityByThree, 4);
        Assert.assertEquals(increasedPriceByThree, 133996);
    }

    @AfterSuite
    public void tearDown() {
        pageFactory.tearDown();
    }
}
