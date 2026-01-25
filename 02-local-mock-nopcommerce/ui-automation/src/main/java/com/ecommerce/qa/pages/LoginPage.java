package com.ecommerce.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "Email")
    private WebElement emailInput;

    @FindBy(id = "Password")
    private WebElement passwordInput;

    @FindBy(xpath = "//button[contains(@class,'login-button')]")
    private WebElement loginButton;


    @FindBy(className = "message-error")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        wait.until(ExpectedConditions.elementToBeClickable(loginButton)).click();

        // Обрабатываем возможный алерт после логина
        try {
            driver.switchTo().alert().accept();
            System.out.println("Alert handled after login");
        } catch (Exception ignored) {}
    }

    public String getErrorMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(errorMessage)).getText();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    public boolean isLoginPageDisplayed() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(emailInput)).isDisplayed() &&
                    wait.until(ExpectedConditions.visibilityOf(passwordInput)).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}