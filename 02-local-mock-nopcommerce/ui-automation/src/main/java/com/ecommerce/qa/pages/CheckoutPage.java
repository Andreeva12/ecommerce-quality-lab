package com.ecommerce.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class CheckoutPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(id = "BillingNewAddress_FirstName")
    private WebElement firstNameInput;

    @FindBy(id = "BillingNewAddress_LastName")
    private WebElement lastNameInput;

    @FindBy(id = "BillingNewAddress_Email")
    private WebElement emailInput;

    @FindBy(id = "BillingNewAddress_Company")
    private WebElement companyInput;

    @FindBy(id = "BillingNewAddress_CountryId")
    private WebElement countrySelect;

    @FindBy(id = "BillingNewAddress_StateProvinceId")
    private WebElement stateSelect;

    @FindBy(id = "BillingNewAddress_City")
    private WebElement cityInput;

    @FindBy(id = "BillingNewAddress_Address1")
    private WebElement address1Input;

    @FindBy(id = "BillingNewAddress_Address2")
    private WebElement address2Input;

    @FindBy(id = "BillingNewAddress_ZipPostalCode")
    private WebElement zipCodeInput;

    @FindBy(id = "BillingNewAddress_PhoneNumber")
    private WebElement phoneNumberInput;

    @FindBy(id = "BillingNewAddress_FaxNumber")
    private WebElement faxNumberInput;

    @FindBy(css = "input.button-1.new-address-next-step-button")
    private WebElement billingAddressContinueButton;

    @FindBy(css = "input.button-1.shipping-method-next-step-button")
    private WebElement shippingMethodContinueButton;

    @FindBy(css = "input.button-1.payment-method-next-step-button")
    private WebElement paymentMethodContinueButton;

    @FindBy(css = "input.button-1.payment-info-next-step-button")
    private WebElement paymentInfoContinueButton;

    @FindBy(css = "input.button-1.confirm-order-next-step-button")
    private WebElement confirmOrderButton;

    @FindBy(id = "paymentmethod_0")
    private WebElement cashOnDeliveryRadio;

    @FindBy(id = "paymentmethod_1")
    private WebElement checkMoneyOrderRadio;

    @FindBy(id = "paymentmethod_2")
    private WebElement creditCardRadio;

    @FindBy(id = "shippingoption_0")
    private WebElement groundShippingRadio;

    @FindBy(id = "shippingoption_1")
    private WebElement nextDayAirRadio;

    @FindBy(id = "shippingoption_2")
    private WebElement secondDayAirRadio;

    @FindBy(css = ".order-total .value-summary")
    private WebElement orderTotalElement;

    @FindBy(css = ".page-title")
    private WebElement pageTitle;

    @FindBy(css = ".order-number strong")
    private WebElement orderNumber;

    @FindBy(css = ".order-completed-continue-button")
    private WebElement continueAfterOrderButton;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void fillBillingAddress(String firstName, String lastName, String email,
                                   String country, String state, String city,
                                   String address, String zipCode, String phone) {
        try {
            wait.until(ExpectedConditions.visibilityOf(firstNameInput));
        } catch (Exception e) {
            System.out.println("First name input not visible, trying with alternative approach...");
            waitForPageToLoad();
        }
        try {
            firstNameInput.clear();
            firstNameInput.sendKeys(firstName);
            lastNameInput.clear();
            lastNameInput.sendKeys(lastName);
            emailInput.clear();
            emailInput.sendKeys(email);
            try {
                Select countryDropdown = new Select(countrySelect);
                countryDropdown.selectByVisibleText(country);
            } catch (Exception e) {
                System.out.println("Country dropdown not found or not selectable");
            }
            try {
                wait.until(ExpectedConditions.elementToBeClickable(stateSelect));
                Select stateDropdown = new Select(stateSelect);
                stateDropdown.selectByVisibleText(state);
            } catch (Exception e) {
                System.out.println("State selection failed, may not be available");
            }
            cityInput.clear();
            cityInput.sendKeys(city);
            address1Input.clear();
            address1Input.sendKeys(address);
            zipCodeInput.clear();
            zipCodeInput.sendKeys(zipCode);
            phoneNumberInput.clear();
            phoneNumberInput.sendKeys(phone);
            System.out.println("Filled billing address for: " + firstName + " " + lastName);
        } catch (Exception e) {
            System.out.println("Error filling billing address: " + e.getMessage());
            throw e;
        }
    }

    public void continueFromBillingAddress() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(billingAddressContinueButton)).click();
            System.out.println("Continued from billing address");
        } catch (Exception e) {
            System.out.println("Error continuing from billing address: " + e.getMessage());
            throw e;
        }
    }

    public void selectShippingMethod(String method) {
        try {
            switch (method.toLowerCase()) {
                case "ground":
                    groundShippingRadio.click();
                    break;
                case "next day":
                    nextDayAirRadio.click();
                    break;
                case "second day":
                    secondDayAirRadio.click();
                    break;
                default:
                    groundShippingRadio.click();
            }
            System.out.println("Selected shipping method: " + method);
        } catch (Exception e) {
            System.out.println("Error selecting shipping method: " + e.getMessage());
        }
    }

    public void continueFromShippingMethod() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(shippingMethodContinueButton)).click();
            System.out.println("Continued from shipping method");
        } catch (Exception e) {
            System.out.println("Error continuing from shipping method: " + e.getMessage());
            throw e;
        }
    }

    public void selectPaymentMethod(String method) {
        try {
            switch (method.toLowerCase()) {
                case "cash on delivery":
                    cashOnDeliveryRadio.click();
                    break;
                case "check / money order":
                    checkMoneyOrderRadio.click();
                    break;
                case "credit card":
                    creditCardRadio.click();
                    break;
                default:
                    cashOnDeliveryRadio.click();
            }
            System.out.println("Selected payment method: " + method);
        } catch (Exception e) {
            System.out.println("Error selecting payment method: " + e.getMessage());
        }
    }

    public void continueFromPaymentMethod() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(paymentMethodContinueButton)).click();
            System.out.println("Continued from payment method");
        } catch (Exception e) {
            System.out.println("Error continuing from payment method: " + e.getMessage());
            throw e;
        }
    }

    public void continueFromPaymentInfo() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(paymentInfoContinueButton)).click();
            System.out.println("Continued from payment info");
        } catch (Exception e) {
            System.out.println("Error continuing from payment info: " + e.getMessage());
            throw e;
        }
    }

    public void confirmOrder() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(confirmOrderButton)).click();
            System.out.println("Order confirmed");
        } catch (Exception e) {
            System.out.println("Error confirming order: " + e.getMessage());
            throw e;
        }
    }

    public String getOrderConfirmationNumber() {
        try {
            wait.until(ExpectedConditions.visibilityOf(orderNumber));
            return orderNumber.getText();
        } catch (Exception e) {
            return "Order number not found";
        }
    }

    public boolean isOrderSuccessful() {
        try {
            wait.until(ExpectedConditions.visibilityOf(pageTitle));
            String title = pageTitle.getText().toLowerCase();
            return title.contains("thank you") || title.contains("successful") || title.contains("заказ подтвержден");
        } catch (Exception e) {
            return false;
        }
    }

    public double getDisplayedOrderTotal() {
        try {
            String totalText = orderTotalElement.getText().replace("$", "").trim();
            return Double.parseDouble(totalText);
        } catch (Exception e) {
            System.out.println("Error getting displayed order total: " + e.getMessage());
            return 0.0;
        }
    }

    public void waitForPageToLoad() {
        wait.until(driver -> {
            String state = (String) ((JavascriptExecutor) driver).executeScript("return document.readyState");
            return state.equals("complete");
        });
    }

    public void continueAfterOrder() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueAfterOrderButton)).click();
            System.out.println("Continued after order completion");
        } catch (Exception e) {
            System.out.println("Continue button not found or not clickable");
        }
    }

    public void checkoutAsGuest() {
        try {
            if (driver.getCurrentUrl().contains("/login/checkoutasguest")) {
                System.out.println("On guest checkout page");
                try {
                    WebElement guestCheckoutButton = driver.findElement(
                            By.xpath("//input[contains(@value, 'Guest') or contains(@value, 'guest')]"));
                    guestCheckoutButton.click();
                    System.out.println("Clicked 'Checkout as Guest' button");
                    wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login/checkoutasguest")));
                } catch (Exception e) {
                    System.out.println("Could not find guest checkout button: " + e.getMessage());
                    WebElement continueButton = driver.findElement(
                            By.xpath("//input[@type='submit' and contains(@value, 'Continue')]"));
                    continueButton.click();
                    System.out.println("Clicked continue button on guest checkout page");
                }
            }
        } catch (Exception e) {
            System.out.println("Error during guest checkout: " + e.getMessage());
            throw e;
        }
    }
}