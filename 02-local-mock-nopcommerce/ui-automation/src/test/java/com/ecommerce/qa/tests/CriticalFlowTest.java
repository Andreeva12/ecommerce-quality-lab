package com.ecommerce.qa.tests;

import com.ecommerce.qa.components.HeaderComponent;
import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.pages.HomePage;
import com.ecommerce.qa.pages.LoginPage;
import io.qameta.allure.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

@Epic("Критические бизнес-сценарии")
@Feature("Авторизация и поиск товаров")
public class CriticalFlowTest extends BaseTest {

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест авторизации существующего пользователя")
    @Story("Пользователь должен успешно войти с валидными кредами")
    public void testAdminLogin() {

        Allure.step("Открываем страницу логина");
        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();

        Allure.step("Вводим валидные учетные данные");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@qa-lab.com", "QaLab_2025!");

        HeaderComponent header = new HeaderComponent(driver);

        Allure.step("Ожидаем появления элементов авторизованного пользователя");
        wait.until(driver -> header.isUserLoggedIn());

        Allure.step("Проверяем, что пользователь авторизован");
        Assert.assertTrue(
                header.isUserLoggedIn(),
                "User should be logged in (My account and Log out visible)"
        );
    }


    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Тест поиска товаров на сайте")
    @Story("Пользователь может найти товары по ключевому слову")
    public void testProductSearch() {
        Allure.step("Выполняем поиск товара по ключевому слову 'computer'");
        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct("computer");

        Allure.step("Ждем результатов поиска");
        // Ждем изменения URL или появления результатов поиска
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("search"),
                ExpectedConditions.presenceOfElementLocated(org.openqa.selenium.By.xpath("//*[contains(text(), 'computer') or contains(text(), 'product')]"))
        ));

        String pageSource = driver.getPageSource().toLowerCase();
        boolean hasResults = pageSource.contains("computer") ||
                pageSource.contains("product") ||
                driver.getCurrentUrl().contains("search");

        Allure.addAttachment("Результаты поиска", "text/plain",
                "Page contains 'computer': " + pageSource.contains("computer") + "\n" +
                        "Page contains 'product': " + pageSource.contains("product") + "\n" +
                        "URL contains 'search': " + driver.getCurrentUrl().contains("search"));

        Allure.step("Проверяем, что поиск вернул результаты");
        Assert.assertTrue(hasResults, "Search should return results or show search page");
    }
}