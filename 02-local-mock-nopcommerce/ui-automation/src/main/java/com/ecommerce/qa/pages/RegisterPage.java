package com.ecommerce.qa.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class RegisterPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Поля для регистрации
    @FindBy(id = "FirstName")
    private WebElement firstNameInput;

    @FindBy(id = "LastName")
    private WebElement lastNameInput;

    @FindBy(id = "Email")
    private WebElement emailInput;

    @FindBy(id = "Password")
    private WebElement passwordInput;

    @FindBy(id = "ConfirmPassword")
    private WebElement confirmPasswordInput;

    // Гендер (пол)
    @FindBy(id = "gender-male")
    private WebElement genderMaleRadio;

    @FindBy(id = "gender-female")
    private WebElement genderFemaleRadio;

    // Дата рождения
    @FindBy(name = "DateOfBirthDay")
    private WebElement dayOfBirthSelect;

    @FindBy(name = "DateOfBirthMonth")
    private WebElement monthOfBirthSelect;

    @FindBy(name = "DateOfBirthYear")
    private WebElement yearOfBirthSelect;

    // Компания
    @FindBy(id = "Company")
    private WebElement companyInput;

    // Чекбокс для рассылки
    @FindBy(id = "Newsletter")
    private WebElement newsletterCheckbox;

    // Кнопки
    @FindBy(id = "register-button")
    private WebElement registerButton;

    @FindBy(css = "a[href='/login']")
    private WebElement loginLink;

    // Сообщения
    @FindBy(className = "result")
    private WebElement successMessage;

    @FindBy(className = "message-error")
    private WebElement errorMessage;

    @FindBy(className = "field-validation-error")
    private WebElement fieldValidationError;

    public RegisterPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ========== МЕТОДЫ ДЛЯ ЗАПОЛНЕНИЯ ПОЛЕЙ ==========

    public void enterFirstName(String firstName) {
        wait.until(ExpectedConditions.visibilityOf(firstNameInput)).sendKeys(firstName);
        System.out.println("Entered first name: " + firstName);
    }

    public void enterLastName(String lastName) {
        lastNameInput.sendKeys(lastName);
        System.out.println("Entered last name: " + lastName);
    }

    public void enterEmail(String email) {
        emailInput.sendKeys(email);
        System.out.println("Entered email: " + email);
    }

    public void enterPassword(String password) {
        passwordInput.sendKeys(password);
        System.out.println("Entered password");
    }

    public void enterConfirmPassword(String password) {
        confirmPasswordInput.sendKeys(password);
        System.out.println("Entered confirm password");
    }

    public void enterCompany(String company) {
        companyInput.sendKeys(company);
        System.out.println("Entered company: " + company);
    }

    // ========== МЕТОДЫ ДЛЯ ВЫБОРА ПОЛА ==========

    public void selectGenderMale() {
        genderMaleRadio.click();
        System.out.println("Selected male gender");
    }

    public void selectGenderFemale() {
        genderFemaleRadio.click();
        System.out.println("Selected female gender");
    }

    // ========== МЕТОДЫ ДЛЯ ВЫБОРА ДАТЫ РОЖДЕНИЯ ==========

    public void selectDayOfBirth(String day) {
        Select daySelect = new Select(dayOfBirthSelect);
        daySelect.selectByVisibleText(day);
        System.out.println("Selected day: " + day);
    }

    public void selectMonthOfBirth(String month) {
        Select monthSelect = new Select(monthOfBirthSelect);
        monthSelect.selectByVisibleText(month);
        System.out.println("Selected month: " + month);
    }

    public void selectYearOfBirth(String year) {
        Select yearSelect = new Select(yearOfBirthSelect);
        yearSelect.selectByVisibleText(year);
        System.out.println("Selected year: " + year);
    }

    // ========== МЕТОДЫ ДЛЯ ЧЕКБОКСОВ ==========

    public void checkNewsletter() {
        if (!newsletterCheckbox.isSelected()) {
            newsletterCheckbox.click();
            System.out.println("Checked newsletter subscription");
        }
    }

    public void uncheckNewsletter() {
        if (newsletterCheckbox.isSelected()) {
            newsletterCheckbox.click();
            System.out.println("Unchecked newsletter subscription");
        }
    }

    // ========== МЕТОДЫ ДЛЯ КНОПОК ==========

    public void clickRegisterButton() {
        wait.until(ExpectedConditions.elementToBeClickable(registerButton)).click();
        System.out.println("Clicked Register button");
    }

    public void clickLoginLink() {
        loginLink.click();
        System.out.println("Clicked Login link");
    }

    // ========== УТИЛИТНЫЕ МЕТОДЫ ==========

    public void registerUser(String firstName, String lastName, String email,
                             String password, String confirmPassword) {
        enterFirstName(firstName);
        enterLastName(lastName);
        enterEmail(email);
        enterPassword(password);
        enterConfirmPassword(confirmPassword);
        clickRegisterButton();
    }

    public String getSuccessMessage() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(successMessage)).getText();
        } catch (Exception e) {
            return "";
        }
    }

    public String getErrorMessage() {
        try {
            return errorMessage.getText();
        } catch (Exception e) {
            return "";
        }
    }

    public boolean isRegistrationSuccessful() {
        return getSuccessMessage().contains("Your registration completed");
    }

    public boolean isRegisterPageDisplayed() {
        try {
            return firstNameInput.isDisplayed() &&
                    registerButton.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}