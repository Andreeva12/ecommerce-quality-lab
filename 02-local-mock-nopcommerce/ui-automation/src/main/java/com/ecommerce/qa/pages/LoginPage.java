package com.ecommerce.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    private WebDriver driver;

    @FindBy(id = "Email")
    private WebElement emailInput;

    @FindBy(id = "Password")
    private WebElement passwordInput;

    @FindBy(css = "button[type='submit']")
    private WebElement loginButton;

    @FindBy(className = "message-error")
    private WebElement errorMessage;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void login(String email, String password) {
        emailInput.clear();
        emailInput.sendKeys(email);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        loginButton.click();
    }

    public String getErrorMessage() {
        try {
            return errorMessage.getText();
        } catch (Exception e) {
            return "No error message found";
        }
    }

    public boolean isLoginPageDisplayed() {
        return emailInput.isDisplayed() && passwordInput.isDisplayed();
    }
}