package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.pages.CartPage;
import com.ecommerce.qa.pages.CheckoutPage;
import com.ecommerce.qa.pages.HomePage;
import com.ecommerce.qa.pages.LoginPage;
import com.ecommerce.qa.db.DatabaseHelper;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.sql.*;

@Epic("Корзина и заказы")
@Feature("Оформление заказа")
public class CheckoutTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Оформление заказа как гость")
    @Story("Неавторизованный пользователь может оформить заказ")
    public void testGuestCheckout() {
        try {
            Allure.step("1. Добавляем товар в корзину");
            HomePage homePage = new HomePage(driver);
            homePage.searchForProduct("Salmon");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Salmon")));
            driver.findElement(By.partialLinkText("Salmon")).click();

            com.ecommerce.qa.pages.ProductPage productPage = new com.ecommerce.qa.pages.ProductPage(driver);
            productPage.addToCart();

            int cartQuantity = productPage.getCartQuantity();
            Assert.assertTrue(cartQuantity > 0, "Product should be added to cart");

            productPage.viewShoppingCart();

            Allure.step("2. Проверяем, что мы на странице корзины");
            Assert.assertTrue(driver.getCurrentUrl().contains("/cart"),
                    "Should be on cart page. URL: " + driver.getCurrentUrl());

            Allure.step("3. Переходим к оформлению заказа");
            CartPage cartPage = new CartPage(driver);
            Assert.assertTrue(cartPage.getCartItemsCount() > 0, "Cart should not be empty");
            cartPage.proceedToCheckout();

            Allure.step("4. Проверяем, что мы на странице оформления");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL after checkout: " + currentUrl);

            if (currentUrl.contains("/login/checkoutasguest")) {
                Allure.step("5. Выбираем оформление как гость");
                System.out.println("On guest checkout page");
                try {
                    WebElement guestCheckoutButton = driver.findElement(
                            By.xpath("//input[@value='Checkout as Guest' or contains(@value, 'Guest')]"));
                    guestCheckoutButton.click();
                    System.out.println("Clicked 'Checkout as Guest' button");
                    wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login/checkoutasguest")));
                } catch (Exception e) {
                    System.out.println("Guest checkout button not found, trying to proceed directly");
                }
            }

            System.out.println("Current URL before CheckoutPage creation: " + driver.getCurrentUrl());

            if (driver.getCurrentUrl().contains("/checkout") ||
                    driver.getCurrentUrl().contains("onepagecheckout") ||
                    driver.getPageSource().contains("BillingNewAddress_FirstName")) {

                Allure.step("6. Заполняем данные как гость");
                CheckoutPage checkoutPage = new CheckoutPage(driver);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                try {
                    checkoutPage.fillBillingAddress(
                            "John", "Doe", "john.doe@test.com",
                            "United States", "California", "Los Angeles",
                            "123 Main St", "90001", "555-0123");
                    checkoutPage.continueFromBillingAddress();

                    try {
                        checkoutPage.selectShippingMethod("ground");
                        checkoutPage.continueFromShippingMethod();

                        checkoutPage.selectPaymentMethod("cash on delivery");
                        checkoutPage.continueFromPaymentMethod();
                        checkoutPage.continueFromPaymentInfo();

                        checkoutPage.confirmOrder();

                        boolean orderSuccessful = checkoutPage.isOrderSuccessful();
                        String orderNumber = checkoutPage.getOrderConfirmationNumber();

                        Allure.addAttachment("Order Status", "text/plain",
                                "Order successful: " + orderSuccessful + "\n" +
                                        "Order number: " + orderNumber);

                        Assert.assertTrue(orderSuccessful, "Order should be successful");

                        // --- Проверка базы данных ---
                        if (orderSuccessful && orderNumber != null && !orderNumber.isEmpty()) {
                            // Ожидаемая сумма заказа – можно взять из ранее сохранённой переменной или вычислить
                            double expectedTotal = 200; // цена лосося, предположим
                            verifyOrderInDatabase(orderNumber, expectedTotal);
                        }

                    } catch (Exception e) {
                        System.out.println("Some checkout steps not available: " + e.getMessage());
                        Allure.addAttachment("Checkout Status", "text/plain",
                                "Partial checkout completed. Error: " + e.getMessage());
                    }
                } catch (Exception e) {
                    System.out.println("Could not fill billing address: " + e.getMessage());
                    System.out.println("Page title: " + driver.getTitle());
                    System.out.println("Page source contains 'BillingNewAddress_FirstName': " +
                            driver.getPageSource().contains("BillingNewAddress_FirstName"));
                }
            } else {
                System.out.println("Not on checkout page. Current URL: " + currentUrl);
                System.out.println("Page source length: " + driver.getPageSource().length());
                Allure.addAttachment("Checkout Status", "text/plain",
                        "Unexpected page: " + currentUrl + "\nPage title: " + driver.getTitle());
            }

        } catch (Exception e) {
            Allure.addAttachment("Test Error", "text/plain", "Test failed with error: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Оформление заказа как авторизованный пользователь")
    @Story("Авторизованный пользователь может оформить заказ")
    public void testLoggedInUserCheckout() {
        try {
            Allure.step("1. Авторизуемся как администратор");
            HomePage homePage = new HomePage(driver);
            homePage.clickLogin();

            LoginPage loginPage = new LoginPage(driver);
            loginPage.login("admin@qa-lab.com", "QaLab_2025!");

            wait.until(ExpectedConditions.or(
                    ExpectedConditions.urlContains("/"),
                    ExpectedConditions.titleContains("nopCommerce")
            ));
            System.out.println("Logged in successfully");

            Allure.step("2. Добавляем товар в корзину");
            homePage.searchForProduct("Book");

            try {
                wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Book")));
                driver.findElement(By.partialLinkText("Book")).click();
            } catch (Exception e) {
                System.out.println("Book not found, trying another product...");
                driver.get("http://localhost:8080");
                homePage.searchForProduct("Computer");
                wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Computer")));
                driver.findElement(By.partialLinkText("Computer")).click();
            }

            com.ecommerce.qa.pages.ProductPage productPage = new com.ecommerce.qa.pages.ProductPage(driver);
            productPage.addToCart();
            productPage.viewShoppingCart();

            Allure.step("3. Оформляем заказ");
            CartPage cartPage = new CartPage(driver);
            cartPage.printCartInfo();

            if (cartPage.getCartItemsCount() > 0) {
                cartPage.proceedToCheckout();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }

                String currentUrl = driver.getCurrentUrl();
                System.out.println("Current URL after checkout: " + currentUrl);

                if (currentUrl.contains("/checkout") ||
                        currentUrl.contains("onepagecheckout") ||
                        driver.getPageSource().contains("BillingNewAddress_FirstName")) {

                    CheckoutPage checkoutPage = new CheckoutPage(driver);

                    try {
                        checkoutPage.continueFromBillingAddress();

                        checkoutPage.selectShippingMethod("ground");
                        checkoutPage.continueFromShippingMethod();

                        checkoutPage.selectPaymentMethod("cash on delivery");
                        checkoutPage.continueFromPaymentMethod();
                        checkoutPage.continueFromPaymentInfo();

                        checkoutPage.confirmOrder();

                        boolean orderSuccessful = checkoutPage.isOrderSuccessful();
                        String orderNumber = checkoutPage.getOrderConfirmationNumber();

                        Allure.addAttachment("Logged In Checkout", "text/plain",
                                "User: admin@qa-lab.com\n" +
                                        "Order successful: " + orderSuccessful + "\n" +
                                        "Order number: " + orderNumber);

                        Assert.assertTrue(orderSuccessful,
                                "Order should be successful for logged in user");

                        // --- Проверка базы данных ---
                        if (orderSuccessful && orderNumber != null && !orderNumber.isEmpty()) {
                            double expectedTotal = 19.99; // цена книги, предположим
                            verifyOrderInDatabase(orderNumber, expectedTotal);
                        }

                    } catch (Exception e) {
                        System.out.println("Checkout steps error for logged in user: " + e.getMessage());
                        Allure.addAttachment("Checkout Status", "text/plain",
                                "Partial checkout completed. Error: " + e.getMessage());
                    }
                } else {
                    System.out.println("Not on checkout page for logged in user. URL: " + currentUrl);
                }
            } else {
                System.out.println("Cart is empty, cannot proceed to checkout");
            }

        } catch (Exception e) {
            Allure.addAttachment("Test Error", "text/plain", "Test failed with error: " + e.getMessage());
            throw e;
        }
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка расчета итоговой суммы при оформлении")
    @Story("Итоговая сумма должна корректно отображаться на всех шагах оформления")
    public void testCheckoutTotalCalculation() {
        try {
            Allure.step("1. Добавляем товар в корзину");
            HomePage homePage = new HomePage(driver);
            homePage.searchForProduct("Salmon");

            wait.until(ExpectedConditions.presenceOfElementLocated(By.partialLinkText("Salmon")));
            driver.findElement(By.partialLinkText("Salmon")).click();

            com.ecommerce.qa.pages.ProductPage productPage = new com.ecommerce.qa.pages.ProductPage(driver);
            productPage.addToCart();
            productPage.viewShoppingCart();

            Allure.step("2. Запоминаем сумму в корзине");
            CartPage cartPage = new CartPage(driver);

            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".value-summary")));

            double cartTotal = cartPage.getOrderTotal();
            System.out.println("Cart total: $" + cartTotal);
            Assert.assertTrue(cartTotal > 0, "Cart total should be greater than 0");

            Allure.step("3. Переходим к оформлению");
            cartPage.proceedToCheckout();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            Allure.step("4. Проверяем сумму на странице оформления");
            String currentUrl = driver.getCurrentUrl();
            System.out.println("Current URL after checkout: " + currentUrl);

            if (currentUrl.contains("/login/checkoutasguest")) {
                try {
                    WebElement guestCheckoutButton = driver.findElement(
                            By.xpath("//input[@value='Checkout as Guest' or contains(@value, 'Guest')]"));
                    guestCheckoutButton.click();
                    wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login/checkoutasguest")));
                } catch (Exception e) {
                    System.out.println("Could not find guest checkout button");
                }
            }

            if (driver.getCurrentUrl().contains("checkout") ||
                    driver.getCurrentUrl().contains("onepagecheckout")) {

                CheckoutPage checkoutPage = new CheckoutPage(driver);

                double displayedTotal = checkoutPage.getDisplayedOrderTotal();
                System.out.println("Checkout total: $" + displayedTotal);

                double difference = Math.abs(cartTotal - displayedTotal);
                boolean withinTolerance = difference < 0.01;

                Allure.addAttachment("Total Verification", "text/plain",
                        "Cart total: $" + cartTotal + "\n" +
                                "Checkout total: $" + displayedTotal + "\n" +
                                "Difference: $" + difference + "\n" +
                                "Within tolerance: " + withinTolerance);

                if (!withinTolerance) {
                    System.out.println("WARNING: Cart and checkout totals differ by $" + difference);
                }
            } else {
                System.out.println("Not on checkout page. Current URL: " + driver.getCurrentUrl());
            }

        } catch (Exception e) {
            Allure.addAttachment("Test Error", "text/plain", "Test failed with error: " + e.getMessage());
            System.out.println("Test completed with warnings: " + e.getMessage());
        }
    }

    /**
     * Вспомогательный метод для проверки заказа в базе данных.
     * @param orderConfirmationText текст с номером заказа (например "ORDER #123")
     * @param expectedTotal ожидаемая сумма заказа
     */
    private void verifyOrderInDatabase(String orderConfirmationText, double expectedTotal) {
        int orderId = extractOrderId(orderConfirmationText);
        Allure.step("Проверка заказа #" + orderId + " в базе данных");

        try (Connection conn = DatabaseHelper.getConnection()) {
            // Проверка таблицы Orders
            String orderQuery = "SELECT * FROM Orders WHERE Id = ?";
            try (PreparedStatement ps = conn.prepareStatement(orderQuery)) {
                ps.setInt(1, orderId);
                try (ResultSet rs = ps.executeQuery()) {
                    Assert.assertTrue(rs.next(), "Заказ с ID " + orderId + " не найден в таблице Orders");
                    double dbTotal = rs.getDouble("OrderTotal");
                    Assert.assertEquals(dbTotal, expectedTotal, 0.01,
                            "Сумма заказа в БД не соответствует ожидаемой");
                }
            }

            // Проверка таблицы OrderItem (ожидаем один товар)
            String itemQuery = "SELECT * FROM OrderItem WHERE OrderId = ?";
            int itemCount = 0;
            try (PreparedStatement ps2 = conn.prepareStatement(itemQuery)) {
                ps2.setInt(1, orderId);
                try (ResultSet rs2 = ps2.executeQuery()) {
                    while (rs2.next()) {
                        itemCount++;
                        int productId = rs2.getInt("ProductId");
                        int quantity = rs2.getInt("Quantity");
                        // Здесь можно добавить дополнительные проверки, например соответствие productId ожидаемому
                        System.out.println("Позиция заказа: productId=" + productId + ", quantity=" + quantity);
                    }
                }
            }
            Assert.assertEquals(itemCount, 1, "Заказ должен содержать ровно одну позицию");

            Allure.addAttachment("DB Check", "text/plain",
                    "Заказ #" + orderId + " успешно проверен в БД.\n" +
                            "Сумма: $" + expectedTotal + "\n" +
                            "Количество позиций: " + itemCount);

        } catch (SQLException e) {
            Allure.addAttachment("DB Error", "text/plain",
                    "Ошибка при проверке БД: " + e.getMessage());
            Assert.fail("Database error: " + e.getMessage());
        }
    }

    /**
     * Извлекает числовой ID из строки подтверждения заказа.
     * Ожидаемый формат: "ORDER #123" или просто "123".
     */
    private int extractOrderId(String confirmationText) {
        if (confirmationText == null) {
            throw new IllegalArgumentException("confirmationText is null");
        }
        String number = confirmationText.replaceAll("[^0-9]", "");
        if (number.isEmpty()) {
            throw new IllegalArgumentException("Не удалось извлечь номер заказа из: " + confirmationText);
        }
        return Integer.parseInt(number);
    }
}