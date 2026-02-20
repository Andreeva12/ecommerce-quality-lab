package com.ecommerce.qa.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class ProductPage {
    private WebDriver driver;
    private WebDriverWait wait;

    @FindBy(tagName = "h1")
    private WebElement productName;

    @FindBy(css = "button.button-1.add-to-cart-button")
    private WebElement addToCartButton;

    @FindBy(css = "input.qty-input, input.quantity")
    private WebElement quantityInput;

    @FindBy(css = "a.ico-cart")
    private WebElement cartLink;

    @FindBy(css = "span.cart-qty")
    private WebElement cartQuantity;

    @FindBy(css = ".product-price, .price.actual-price, span.price-value")
    private WebElement productPrice;

    @FindBy(css = ".sku, .product-sku, .sku-number, .product-code")
    private WebElement skuElement;

    @FindBy(css = ".bar-notification.success, .success, .notification")
    private WebElement successNotification;

    @FindBy(css = ".bar-notification a, .success a")
    private WebElement notificationCartLink;

    @FindBy(css = "button.continue-shopping-button")
    private WebElement continueShoppingButton;

    public ProductPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        PageFactory.initElements(driver, this);
    }

    public void addToCart() {
        System.out.println("Clicking 'Add to cart' button...");
        wait.until(ExpectedConditions.elementToBeClickable(addToCartButton)).click();
        System.out.println("Successfully clicked 'Add to cart' button");
        wait.until(driver -> {
            try {
                return getCartQuantity() > 0;
            } catch (Exception e) {
                return false;
            }
        });
    }

    public void addToCartWithQuantity(int quantity) {
        if (isQuantityInputVisible()) {
            setQuantity(quantity);
        }
        addToCart();
    }

    public void setQuantity(int quantity) {
        try {
            if (isQuantityInputVisible()) {
                quantityInput.clear();
                quantityInput.sendKeys(String.valueOf(quantity));
                System.out.println("Set quantity to: " + quantity);
            }
        } catch (Exception e) {
            System.out.println("Could not set quantity: " + e.getMessage());
        }
    }

    public void viewShoppingCart() {
        try {
            WebElement notification = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            By.cssSelector(".bar-notification, .success, div[class*='success']")
                    )
            );
            WebElement link = notification.findElement(By.tagName("a"));
            link.click();
            System.out.println("Clicked cart link in notification");
        } catch (Exception e) {
            System.out.println("Notification not found, using header cart link");
            cartLink.click();
        }
        wait.until(ExpectedConditions.urlContains("/cart"));
    }

    public String getSuccessMessage() {
        try {
            wait.until(ExpectedConditions.visibilityOf(successNotification));
            return successNotification.getText();
        } catch (Exception e) {
            try {
                return driver.findElement(By.cssSelector(".success, .notification, .bar-notification")).getText();
            } catch (Exception ex) {
                return "No success message found";
            }
        }
    }

    public String getProductName() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(productName)).getText().trim();
        } catch (Exception e) {
            System.out.println("Error getting product name: " + e.getMessage());
            return "Product name not found";
        }
    }

    public double getProductPrice() {
        try {
            String priceText = productPrice.getText().replace("$", "").trim();
            return Double.parseDouble(priceText);
        } catch (Exception e) {
            try {
                String priceText = driver.findElement(By.cssSelector(".price, .product-price")).getText();
                priceText = priceText.replace("$", "").trim();
                return Double.parseDouble(priceText);
            } catch (Exception ex) {
                System.out.println("Error getting product price: " + ex.getMessage());
                return 0.0;
            }
        }
    }

    public String getSKU() {
        try {
            if (skuElement != null && skuElement.isDisplayed()) {
                String skuText = skuElement.getText().trim();
                if (skuText.contains("SKU:")) {
                    skuText = skuText.replace("SKU:", "").trim();
                }
                return skuText;
            }
            List<WebElement> possibleSkuElements = driver.findElements(By.cssSelector(
                    ".sku, .product-sku, .sku-number, .product-code, [data-product-sku], .value[itemprop='sku']"
            ));
            for (WebElement element : possibleSkuElements) {
                if (element.isDisplayed()) {
                    String skuText = element.getText().trim();
                    skuText = skuText.replaceAll("(?i)SKU\\s*:?", "").trim();
                    skuText = skuText.replaceAll("(?i)Product\\s*Code\\s*:?", "").trim();
                    if (!skuText.isEmpty()) {
                        return skuText;
                    }
                }
            }
            List<WebElement> tableRows = driver.findElements(By.cssSelector("table.product-details tr"));
            for (WebElement row : tableRows) {
                List<WebElement> cells = row.findElements(By.tagName("td"));
                if (cells.size() >= 2) {
                    String label = cells.get(0).getText().trim().toLowerCase();
                    if (label.contains("sku") || label.contains("артикул") || label.contains("code")) {
                        String value = cells.get(1).getText().trim();
                        if (!value.isEmpty()) {
                            return value;
                        }
                    }
                }
            }
            System.out.println("SKU not found on product page");
            return null;
        } catch (Exception e) {
            System.out.println("Error getting SKU: " + e.getMessage());
            return null;
        }
    }

    public int getCartQuantity() {
        try {
            String qtyText = cartQuantity.getText();
            qtyText = qtyText.replaceAll("[^0-9]", "");
            if (!qtyText.isEmpty()) {
                return Integer.parseInt(qtyText);
            }
            return 0;
        } catch (Exception e) {
            try {
                String qtyText = driver.findElement(By.cssSelector("span.cart-qty")).getText();
                qtyText = qtyText.replaceAll("[^0-9]", "");
                return Integer.parseInt(qtyText);
            } catch (Exception ex) {
                return 0;
            }
        }
    }

    public boolean isQuantityInputVisible() {
        try {
            return quantityInput.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isProductPageLoaded() {
        try {
            return productName.isDisplayed() &&
                    (addToCartButton.isDisplayed() ||
                            driver.findElements(By.cssSelector("button.add-to-cart-button, input[value='Add to cart']")).size() > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public void continueShopping() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(continueShoppingButton)).click();
            System.out.println("Clicked 'Continue shopping' button");
        } catch (Exception e) {
            System.out.println("Continue shopping button not found or not clickable");
        }
    }

    public void printPageInfo() {
        System.out.println("\n=== PRODUCT PAGE INFO ===");
        System.out.println("URL: " + driver.getCurrentUrl());
        System.out.println("Title: " + driver.getTitle());
        try {
            System.out.println("Product name: " + getProductName());
        } catch (Exception e) {
            System.out.println("Product name: ERROR - " + e.getMessage());
        }
        try {
            System.out.println("Product price: $" + getProductPrice());
        } catch (Exception e) {
            System.out.println("Product price: ERROR - " + e.getMessage());
        }
        try {
            String sku = getSKU();
            System.out.println("Product SKU: " + (sku != null ? sku : "NOT FOUND"));
        } catch (Exception e) {
            System.out.println("Product SKU: ERROR - " + e.getMessage());
        }
        try {
            System.out.println("Add to cart button visible: " +
                    (addToCartButton.isDisplayed() ? "YES" : "NO"));
        } catch (Exception e) {
            System.out.println("Add to cart button: NOT FOUND");
        }
        try {
            System.out.println("Cart quantity in header: " + getCartQuantity());
        } catch (Exception e) {
            System.out.println("Cart quantity: NOT FOUND");
        }
        System.out.println("\nAll buttons on page:");
        var allButtons = driver.findElements(By.cssSelector("button, input[type='button'], input[type='submit']"));
        for (var btn : allButtons) {
            String text = btn.getText();
            String value = btn.getAttribute("value");
            String id = btn.getAttribute("id");
            String className = btn.getAttribute("class");
            if (!text.isEmpty() || value != null) {
                System.out.println("  - Text: '" + text + "', Value: '" + value + "', ID: " + id + ", Class: " + className);
            }
        }
    }
}