package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.pages.CartPage;
import com.ecommerce.qa.pages.HomePage;
import com.ecommerce.qa.pages.ProductPage;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Корзина и заказы")
@Feature("Управление корзиной")
public class CartTest extends BaseTest {

    /* =========================
       HELPERS
     ========================= */

    private void addProductToCart(String productName) {
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct(productName);

        // Ждем и кликаем по ссылке товара
        try {
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.xpath("//a[contains(text(), '" + productName + "')]")
            )).click();
        } catch (Exception e) {
            // Если не найдено по полному названию, ищем частично
            wait.until(ExpectedConditions.elementToBeClickable(
                    By.partialLinkText(productName)
            )).click();
        }

        ProductPage productPage = new ProductPage(driver);
        productPage.addToCart();

        // Ждем, пока товар добавится в корзину
        wait.until(driver -> {
            try {
                return productPage.getCartQuantity() > 0;
            } catch (Exception ex) {
                return false;
            }
        });

        System.out.println("Product '" + productName + "' added to cart. Cart quantity: " + productPage.getCartQuantity());
    }

    // УПРОЩЕННЫЙ МЕТОД: добавляем товар из результатов поиска
    private void addProductFromSearchResults(String productName) {
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct(productName);

        System.out.println("Searching for product: " + productName);

        // Ждем появления результатов поиска
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".search-results, .product-grid, .item-box")));
            System.out.println("Search results page loaded");
        } catch (Exception e) {
            System.out.println("Search results not found, waiting for any product to appear");
        }

        // Подождем еще немного для полной загрузки
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // УПРОЩЕННЫЙ ПОДХОД: ищем первую кнопку "Add to cart" на странице результатов
        // или кликаем по самому товару, чтобы перейти на его страницу
        try {
            // Пытаемся найти кнопку Add to cart на странице результатов
            WebElement addToCartButton = driver.findElement(By.cssSelector(".button-2.product-box-add-to-cart-button"));
            System.out.println("Found 'Add to cart' button on search results page");
            addToCartButton.click();
            System.out.println("Clicked 'Add to cart' button on search results");

            // Ждем, пока товар добавится в корзину
            ProductPage productPage = new ProductPage(driver);
            wait.until(driver -> {
                try {
                    return productPage.getCartQuantity() > 0;
                } catch (Exception ex) {
                    return false;
                }
            });

            System.out.println("Product added to cart from search results");
        } catch (Exception e) {
            System.out.println("Could not find 'Add to cart' button on search results, trying to click product link");

            // Если не нашли кнопку, ищем ссылку на товар и кликаем по ней
            WebElement productLink = driver.findElement(By.xpath("//a[contains(text(), '" + productName + "')]"));
            productLink.click();

            // Теперь добавляем товар со страницы товара
            ProductPage productPage = new ProductPage(driver);
            productPage.addToCart();

            // Ждем, пока товар добавится в корзину
            wait.until(driver -> {
                try {
                    return productPage.getCartQuantity() > 0;
                } catch (Exception ex) {
                    return false;
                }
            });

            System.out.println("Product added to cart from product page");
        }
    }

    private CartPage openCart() {
        // Просто переходим по прямой ссылке в корзину
        driver.get("http://localhost:8080/cart");
        wait.until(ExpectedConditions.urlContains("/cart"));

        // Даем время на загрузку
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted in openCart");
        }

        return new CartPage(driver);
    }

    /* =========================
       TESTS
     ========================= */

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Добавление товара в корзину")
    public void testAddProductToCart() {
        System.out.println("=== Starting testAddProductToCart ===");
        addProductToCart("Salmon");

        CartPage cartPage = openCart();

        // Отладочная информация
        cartPage.printCartInfo();

        Assert.assertTrue(cartPage.isProductInCart("Salmon"),
                "Salmon should be present in cart");

        Assert.assertEquals(cartPage.getCartItemsCount(), 1,
                "Cart should contain exactly 1 item");
    }


    @Test
    @Severity(SeverityLevel.NORMAL)
    @Story("Удаление товара из корзины")
    public void testRemoveProductFromCart() {
        System.out.println("=== Starting testRemoveProductFromCart ===");

        // Очищаем корзину перед тестом
        driver.get("http://localhost:8080/cart");
        CartPage cartPage = new CartPage(driver);
        if (!cartPage.isCartEmpty()) {
            cartPage.removeProduct(0);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        addProductToCart("Salmon");
        cartPage = openCart();

        // Отладочная информация
        cartPage.printCartInfo();

        Assert.assertEquals(cartPage.getCartItemsCount(), 1,
                "Cart should have 1 item before removal");

        cartPage.removeProduct(0);

        // Даем время на удаление
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Thread was interrupted during remove test");
        }

        // Перезагружаем страницу
        driver.navigate().refresh();
        cartPage = new CartPage(driver);

        // Отладочная информация
        cartPage.printCartInfo();

        Assert.assertTrue(cartPage.isCartEmpty(),
                "Cart must be empty after removal");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Story("Проверка общей суммы")
    public void testCartTotalCalculation() {
        System.out.println("=== Starting testCartTotalCalculation ===");

        // Очищаем корзину перед началом теста
        driver.get("http://localhost:8080/cart");
        CartPage cartPage = new CartPage(driver);
        if (!cartPage.isCartEmpty()) {
            // Удаляем все товары из корзины
            int itemsCount = cartPage.getCartItemsCount();
            for (int i = itemsCount - 1; i >= 0; i--) {
                cartPage.removeProduct(i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            driver.navigate().refresh();
        }

        // Добавляем первый товар через страницу товара
        addProductToCart("Salmon");

        // Проверяем количество после первого добавления
        ProductPage productPage = new ProductPage(driver);
        System.out.println("Cart quantity after first product: " + productPage.getCartQuantity());

        // Переходим на главную
        driver.get("http://localhost:8080");

        // Даем время на загрузку главной страницы
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // ВТОРОЙ ТОВАР добавляем через упрощенный метод
        addProductFromSearchResults("A Court of Thorns and Roses");

        // Переходим в корзину
        cartPage = openCart();

        // Отладочная информация
        cartPage.printCartInfo();

        // Проверяем названия товаров в корзине
        System.out.println("Products in cart:");
        for (int i = 0; i < cartPage.getCartItemsCount(); i++) {
            System.out.println("  - " + cartPage.getProductName(i));
        }

        Assert.assertTrue(cartPage.getCartItemsCount() >= 2,
                "Cart must contain at least 2 items. Found: " + cartPage.getCartItemsCount());

        Assert.assertTrue(
                cartPage.verifyCartCalculations(),
                "Total cart calculation must be correct"
        );
    }
}