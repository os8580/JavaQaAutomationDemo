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
        // Setting up Chrome driver with start-maximized option
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        pageFactory = new PageFactory(new ChromeDriver(options));
    }

    @Test
    public void testRozetkaUsingPageObjectWithChrome() {
        // Create pages
        GooglePage googlePage = (GooglePage) pageFactory.get(Pages.GOOGLE);
        RozetkaPage rozetkaPage = (RozetkaPage) pageFactory.get(Pages.ROZETKA);

        // Google search for 'rozetka ua'
        googlePage.loadPage();
        googlePage.acceptCookiesIfPresent();
        googlePage.setSearchValue("rozetka ua");
        googlePage.performSearch();

        List<WebElement> searchResults = googlePage.getSearchResults();
        Assert.assertTrue(searchResults.size() > 1, "No search results found!");
        searchResults.get(0).click();

        // Rozetka search for 'Iphone'
        Assert.assertTrue(rozetkaPage.isPageLoaded());
        rozetkaPage.setSearchValue("Iphone");
        rozetkaPage.performSearch();

        List<WebElement> searchResultsRozetka = rozetkaPage.getSearchResults();
        Assert.assertFalse(searchResultsRozetka.isEmpty(), "No search results found on Rozetka page!");

        // Get product details
        Map<String, Object> productDetails = new HashMap<>();
        productDetails.put("name", rozetkaPage.getProductName());
        String productPrice = rozetkaPage.getProductPrice().replaceAll("\\D", "");
        productDetails.put("price", Integer.parseInt(productPrice));

        // Add product to the cart and open it
        rozetkaPage.clickAddToCartButton();
        rozetkaPage.waitForTextVisibility("Товар добавлен в корзину");
        rozetkaPage.openCart();
        rozetkaPage.waitForTextVisibility("Вместе дешевле");

        // Get product details from the cart
        Map<String, Object> productDetailsInCart = new HashMap<>();
        productDetailsInCart.put("name", rozetkaPage.getCartProductName());
        String productPriceInCart = rozetkaPage.getCartProductPrice().replaceAll("\\D+", "");
        productDetailsInCart.put("price", Integer.parseInt(productPriceInCart));

        // Verify that product details from the cart match the initial product details
        Assert.assertEquals(productDetailsInCart, productDetails, "Product details in the cart do not match the selected product details!");

        // Get initial quantity and price of a single product
        int initialQuantity = rozetkaPage.getProductQuantity();
        int singleProductPrice = rozetkaPage.getSingleProductPrice();

        // Increase product quantity by one
        rozetkaPage.increaseProductQuantityByOne();
        int increasedQuantity = rozetkaPage.getProductQuantity();
        int increasedPrice = rozetkaPage.getSingleProductPrice() * rozetkaPage.getProductQuantity();

        // Verify that product quantity was increased by one and that total product price was increased proportionally
        Assert.assertEquals(increasedQuantity, initialQuantity + 1, "Product quantity did not increase by 1.");
        Assert.assertTrue(increasedPrice > initialQuantity * singleProductPrice, "Product price did not increase after adding another instance.");

        // Decrease product quantity back to the initial
        rozetkaPage.decreaseProductQuantityByOne();
        int decreasedQuantity = rozetkaPage.getProductQuantity();
        int decreasedPrice = rozetkaPage.getSingleProductPrice() * rozetkaPage.getProductQuantity();

        // Verify that product quantity was decreased by one and that total product price was decreased proportionally
        Assert.assertEquals(decreasedQuantity, initialQuantity, "Product quantity did not decrease by 1.");
        Assert.assertEquals(decreasedPrice, initialQuantity * singleProductPrice, "Product price did not return to the initial value after removing an instance.");

        // Increase product quantity by three
        rozetkaPage.increaseProductQuantityByOne();
        rozetkaPage.increaseProductQuantityByOne();
        rozetkaPage.increaseProductQuantityByOne();
        int increasedQuantityByThree = rozetkaPage.getProductQuantity();
        int increasedPriceByThree = rozetkaPage.getSingleProductPrice() * rozetkaPage.getProductQuantity();

        // Verify that product quantity was increased by three and that total product price was increased proportionally
        Assert.assertEquals(increasedQuantityByThree, 4);
        Assert.assertEquals(increasedPriceByThree, singleProductPrice * increasedQuantityByThree);

//        // Set product quantity to 7
        rozetkaPage.setProductQuantity(7);
        int setQuantity = rozetkaPage.getProductQuantity();
        int setPrice = rozetkaPage.getSingleProductPrice() * rozetkaPage.getProductQuantity();

        // Verify that product quantity was set to 7 and that total product price was updated accordingly
        Assert.assertEquals(setQuantity, 7);
        Assert.assertEquals(setPrice, singleProductPrice * 7);
    }

    @AfterSuite
    public void tearDown() {
        // Tear down the driver after the test suite is finished
        pageFactory.tearDown();
    }
}