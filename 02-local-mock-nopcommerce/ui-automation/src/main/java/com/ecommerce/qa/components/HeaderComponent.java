package com.ecommerce.qa.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class HeaderComponent {

    private WebDriver driver;
    private WebDriverWait wait;

    public HeaderComponent(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        PageFactory.initElements(driver, this);
    }

    // ===== LOGO =====
    @FindBy(className = "header-logo")
    private WebElement logo;

    // ===== AUTH LINKS =====
    @FindBy(css = "a.ico-register")
    private WebElement registerLink;

    @FindBy(css = "a.ico-login")
    private WebElement loginLink;

    @FindBy(css = "a.ico-account")
    private WebElement myAccountLink;

    @FindBy(css = "a.ico-logout")
    private WebElement logoutLink;

    // ===== SEARCH =====
    @FindBy(id = "small-searchterms")
    private WebElement searchBox;

    @FindBy(css = "button.search-box-button")
    private WebElement searchButton;

    // ===== ACTIONS =====

    public void clickLogo() {
        wait.until(ExpectedConditions.elementToBeClickable(logo)).click();
    }

    public void clickLogin() {
        wait.until(ExpectedConditions.elementToBeClickable(loginLink)).click();
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(registerLink)).click();
    }

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(logoutLink)).click();
    }

    public void searchFor(String keyword) {
        wait.until(ExpectedConditions.visibilityOf(searchBox));
        searchBox.clear();
        searchBox.sendKeys(keyword);
        searchButton.click();
    }

    // ===== CHECKS =====

    public boolean isUserLoggedIn() {
        try {
            wait.until(ExpectedConditions.visibilityOf(myAccountLink));
            wait.until(ExpectedConditions.visibilityOf(logoutLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUserLoggedOut() {
        try {
            wait.until(ExpectedConditions.visibilityOf(loginLink));
            wait.until(ExpectedConditions.visibilityOf(registerLink));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
