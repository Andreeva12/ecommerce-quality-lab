package com.ecommerce.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class CartPage {

    private WebDriver driver;
    private WebDriverWait wait;

    // ===== ЭЛЕМЕНТЫ СТРАНИЦЫ =====

    @FindBy(css = ".page-title h1")
    private WebElement pageTitle;

    @FindBy(css = "a.product-name")
    private List<WebElement> productNames;

    @FindBy(css = ".value-summary")
    private WebElement orderTotalValue;

    @FindBy(css = "span.product-unit-price")
    private List<WebElement> productPrices;

    @FindBy(css = "input.qty-input")
    private List<WebElement> quantityInputs;

    @FindBy(css = "button.remove-btn")
    private List<WebElement> removeButtons;

    @FindBy(css = "input[name='updatecart'], button[name='updatecart']")
    private WebElement updateCartButton;

    @FindBy(css = "button.continue-shopping-button")
    private WebElement continueShoppingButton;

    @FindBy(css = ".order-summary-content")
    private WebElement emptyCartMessage;

    @FindBy(id = "termsofservice")
    private WebElement termsOfServiceCheckbox;

    @FindBy(id = "checkout")
    private WebElement checkoutButton;

    // ===== КОНСТРУКТОР =====

    public CartPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    // ===== МЕТОДЫ (без изменений, только для полноты оставлены сигнатуры) =====

    public boolean isCartEmpty() {
        try {
            if (pageTitle.isDisplayed() && pageTitle.getText().contains("Shopping cart")) {
                if (emptyCartMessage.isDisplayed()) {
                    String message = emptyCartMessage.getText().toLowerCase();
                    return message.contains("empty") || message.contains("no items") || message.contains("ваша корзина пуста");
                }
                return getCartItemsCount() == 0;
            }
            return true;
        } catch (Exception e) {
            return true;
        }
    }

    public int getCartItemsCount() {
        return productNames.size();
    }

    public String getProductName(int index) {
        if (index >= 0 && index < productNames.size()) {
            return productNames.get(index).getText().trim();
        }
        return "";
    }

    public boolean isProductInCart(String productName) {
        for (WebElement product : productNames) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    public int getProductIndex(String productName) {
        for (int i = 0; i < productNames.size(); i++) {
            if (productNames.get(i).getText().trim().equalsIgnoreCase(productName.trim())) {
                return i;
            }
        }
        return -1;
    }

    public double getProductPrice(int index) {
        if (index >= 0 && index < productPrices.size()) {
            String priceText = productPrices.get(index).getText().replace("$", "").trim();
            return Double.parseDouble(priceText);
        }
        return 0.0;
    }

    public void setProductQuantity(int index, int quantity) {
        if (index >= 0 && index < quantityInputs.size()) {
            WebElement qtyInput = quantityInputs.get(index);
            qtyInput.clear();
            qtyInput.sendKeys(String.valueOf(quantity));
            System.out.println("Set quantity for item " + index + " to: " + quantity);
        } else {
            System.out.println("Quantity input not found for index: " + index);
        }
    }

    public int getProductQuantity(int index) {
        if (index >= 0 && index < quantityInputs.size()) {
            String value = quantityInputs.get(index).getAttribute("value");
            if (value != null && !value.isEmpty()) {
                return Integer.parseInt(value);
            }
        }
        return 1;
    }

    public void updateCart() {
        try {
            if (updateCartButton.isDisplayed() && updateCartButton.isEnabled()) {
                updateCartButton.click();
                System.out.println("Cart updated");
                wait.until(ExpectedConditions.stalenessOf(productNames.get(0)));
            }
        } catch (Exception e) {
            System.out.println("Error updating cart: " + e.getMessage());
        }
    }

    public void removeProduct(int index) {
        if (index >= 0 && index < removeButtons.size()) {
            removeButtons.get(index).click();
            System.out.println("Clicked remove button for product at index: " + index);
            try {
                Thread.sleep(1000);
                updateCart();
            } catch (Exception e) {
                System.out.println("Error after clicking remove: " + e.getMessage());
            }
        } else {
            System.out.println("Remove button not found for index: " + index);
        }
    }

    public double getOrderTotal() {
        try {
            // Явно ждём, пока элемент суммы станет видимым
            wait.until(ExpectedConditions.visibilityOf(orderTotalValue));
            String totalText = orderTotalValue.getText()
                    .replace("$", "")
                    .replace(",", "")  // убираем запятые (для сумм типа 2,000.00)
                    .trim();
            if (!totalText.isEmpty()) {
                return Double.parseDouble(totalText);
            }
        } catch (Exception e) {
            System.out.println("Error getting order total from .value-summary: " + e.getMessage());
        }

        // Запасные варианты (если первый не сработал)
        try {
            WebElement orderTotal = driver.findElement(By.cssSelector(".order-total"));
            String totalText = orderTotal.getText().replaceAll("[^\\d.]", "");
            if (!totalText.isEmpty()) {
                return Double.parseDouble(totalText);
            }
        } catch (Exception e) {
            // игнорируем
        }

        try {
            List<WebElement> totals = driver.findElements(By.xpath("//*[contains(text(), 'Total') or contains(text(), 'Итого')]"));
            for (WebElement total : totals) {
                String text = total.getText();
                if (text.contains("$")) {
                    String priceText = text.replaceAll("[^\\d.]", "");
                    if (!priceText.isEmpty()) {
                        return Double.parseDouble(priceText);
                    }
                }
            }
            totals = driver.findElements(By.cssSelector(".order-total, .total, .cart-total"));
            for (WebElement total : totals) {
                String text = total.getText();
                if (text.contains("$")) {
                    String priceText = text.replaceAll("[^\\d.]", "");
                    if (!priceText.isEmpty()) {
                        return Double.parseDouble(priceText);
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting order total: " + e.getMessage());
        }
        return 0.0;
    }

    public void continueShopping() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
            System.out.println("Clicked 'Continue shopping' button");
        } catch (Exception e) {
            System.out.println("Continue shopping button not found or not clickable");
        }
    }

    public boolean verifyCartCalculations() {
        double calculatedTotal = 0.0;
        for (int i = 0; i < getCartItemsCount(); i++) {
            double price = getProductPrice(i);
            int quantity = getProductQuantity(i);
            double expectedItemTotal = price * quantity;
            calculatedTotal += expectedItemTotal;
            System.out.println("Item " + (i + 1) + ": " + getProductName(i) + ", Price: $" + price + ", Quantity: " + quantity + ", Item Total: $" + expectedItemTotal);
        }
        double displayedTotal = getOrderTotal();
        System.out.println("Calculated Total: $" + calculatedTotal);
        System.out.println("Displayed Total: $" + displayedTotal);
        double difference = Math.abs(calculatedTotal - displayedTotal);
        boolean isCorrect = difference < 0.01;
        if (!isCorrect) {
            System.out.println("WARNING: Cart calculation mismatch! Difference: $" + difference);
        }
        return isCorrect;
    }

    public String getEmptyCartMessageText() {
        try {
            return emptyCartMessage.getText();
        } catch (Exception e) {
            return "Empty cart message not found";
        }
    }

    public void printCartInfo() {
        System.out.println("\n=== CART PAGE INFO ===");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
        try {
            System.out.println("Page title: " + pageTitle.getText());
        } catch (Exception e) {
            System.out.println("Page title not found");
        }
        System.out.println("Items in cart: " + getCartItemsCount());
        System.out.println("Is cart empty: " + isCartEmpty());
        for (int i = 0; i < getCartItemsCount(); i++) {
            System.out.println("\nItem #" + (i + 1) + ":");
            System.out.println(" Name: " + getProductName(i));
            System.out.println(" Price: $" + getProductPrice(i));
            System.out.println(" Quantity: " + getProductQuantity(i));
        }
        System.out.println("\nOrder total: $" + getOrderTotal());
        System.out.println("Calculations correct: " + verifyCartCalculations());
        System.out.println("\n=== DEBUG INFO ===");
        System.out.println("Found " + productNames.size() + " product names");
        System.out.println("Found " + productPrices.size() + " product prices");
        System.out.println("Found " + quantityInputs.size() + " quantity inputs");
        System.out.println("Found " + removeButtons.size() + " remove buttons");
    }

    public void proceedToCheckout() {
        try {
            acceptTermsOfService();
            wait.until(ExpectedConditions.elementToBeClickable(checkoutButton)).click();
            System.out.println("Proceeded to checkout");
        } catch (Exception e) {
            System.out.println("Error proceeding to checkout: " + e.getMessage());
            throw e;
        }
    }

    public void acceptTermsOfService() {
        try {
            if (termsOfServiceCheckbox != null) {
                if (!termsOfServiceCheckbox.isSelected()) {
                    termsOfServiceCheckbox.click();
                    System.out.println("Accepted terms of service");
                }
            } else {
                System.out.println("Terms of service checkbox not found");
            }
        } catch (Exception e) {
            System.out.println("Could not accept terms of service: " + e.getMessage());
        }
    }
}