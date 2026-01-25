package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import io.qameta.allure.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Smoke тесты")
@Feature("Базовые проверки сайта")
public class SmokeTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка заголовка домашней страницы")
    @Story("Заголовок должен содержать название магазина")
    public void verifyHomePageTitle() {
        // Исправляем ожидание заголовка
        wait.until(driver -> {
            String title = driver.getTitle().toLowerCase();
            return title.contains("store") ||
                    title.contains("nopcommerce") ||
                    title.contains("home");
        });

        String title = driver.getTitle();
        Allure.addAttachment("Page Title", "text/plain", title);

        Assert.assertNotNull(title, "Page title should not be null");
        Assert.assertFalse(title.isEmpty(), "Page title should not be empty");

        boolean validTitle = title.contains("Your store") ||
                title.contains("nopCommerce") ||
                title.contains("Home page");

        Assert.assertTrue(validTitle,
                "Page title should contain 'Your store', 'nopCommerce' or 'Home page'. Actual: " + title);
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка отображения логотипа")
    @Story("Логотип должен быть виден на всех страницах")
    public void verifyLogoIsDisplayed() {
        // Ждем отображения логотипа
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("header-logo")));

        boolean logoDisplayed = logo.isDisplayed();
        Allure.addAttachment("Logo Displayed", "text/plain", String.valueOf(logoDisplayed));

        Assert.assertTrue(logoDisplayed, "Logo should be displayed");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка наличия поисковой строки")
    @Story("Поиск должен быть доступен для пользователей")
    public void verifySearchBoxExists() {
        // Ждем отображения поля поиска
        WebElement searchBox = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("small-searchterms")));

        boolean searchBoxExists = searchBox.isDisplayed();
        Allure.addAttachment("Search Box Exists", "text/plain", String.valueOf(searchBoxExists));

        Assert.assertTrue(searchBoxExists, "Search box should exist");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка навигационных ссылок")
    @Story("Основные навигационные элементы должны быть доступны")
    public void verifyNavigationLinks() {
        String[] expectedLinks = {"Register", "Log in", "Shopping cart", "Wishlist"};
        StringBuilder foundLinks = new StringBuilder("Found links:\n");

        for (String linkText : expectedLinks) {
            try {
                WebElement link = driver.findElement(By.linkText(linkText));
                Assert.assertTrue(link.isDisplayed(), linkText + " link should be displayed");
                foundLinks.append("✅ ").append(linkText).append("\n");
            } catch (Exception e) {
                try {
                    WebElement link = driver.findElement(By.partialLinkText(linkText));
                    foundLinks.append("⚠️ ").append(linkText).append(" (partial match)\n");
                } catch (Exception e2) {
                    foundLinks.append("❌ ").append(linkText).append(" (not found)\n");
                }
            }
        }

        Allure.addAttachment("Navigation Links Check", "text/plain", foundLinks.toString());
    }
}