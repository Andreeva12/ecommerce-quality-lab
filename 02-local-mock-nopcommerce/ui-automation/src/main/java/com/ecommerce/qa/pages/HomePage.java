package com.ecommerce.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HomePage {
    private WebDriver driver;
    private WebDriverWait wait;

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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public boolean isLogoDisplayed() {
        return logo.isDisplayed();
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
        System.out.println("Clicked on Register button");
    }

    public void clickLogin() {
        try {
            // Обрабатываем возможный алерт перед кликом
            try {
                driver.switchTo().alert().accept();
                System.out.println("Alert handled before login click");
            } catch (Exception ignored) {}

            wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
            System.out.println("Clicked on Log in button");

        } catch (Exception e) {
            System.out.println("Error clicking login: " + e.getMessage());
            throw e;
        }
    }

    public void clickCart() {
        wait.until(ExpectedConditions.elementToBeClickable(cartLink)).click();
        System.out.println("Clicked on Shopping cart");
    }

    public void searchForProduct(String productName) {
        wait.until(ExpectedConditions.visibilityOf(searchBox)).clear();
        searchBox.sendKeys(productName);
        System.out.println("Entered search term: " + productName);

        wait.until(ExpectedConditions.elementToBeClickable(searchButton)).click();
        System.out.println("Clicked search button");

        // Обрабатываем возможный алерт после поиска
        try {
            driver.switchTo().alert().accept();
            System.out.println("Alert handled after search");
        } catch (Exception ignored) {}
    }

    public boolean isSearchBoxVisible() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(searchBox)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }
}