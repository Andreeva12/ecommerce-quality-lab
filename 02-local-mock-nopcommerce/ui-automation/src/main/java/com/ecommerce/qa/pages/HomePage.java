package com.ecommerce.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {
    private WebDriver driver;

    @FindBy(className = "header-logo")
    private WebElement logo;

    @FindBy(className = "ico-register")
    private WebElement registerLink;

    @FindBy(className = "ico-login")
    private WebElement loginLink;

    @FindBy(className = "ico-cart")
    private WebElement cartLink;

    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(className = "search-box-button")
    private WebElement searchButton;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public boolean isLogoDisplayed() {
        return logo.isDisplayed();
    }

    public void clickRegister() {
        registerLink.click();
    }

    public void clickLogin() {
        loginLink.click();
    }

    public void clickCart() {
        cartLink.click();
    }

    public void searchForProduct(String productName) {
        searchBox.clear();
        searchBox.sendKeys(productName);
        searchButton.click();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}