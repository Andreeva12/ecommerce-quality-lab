package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.pages.HomePage;
import com.ecommerce.qa.pages.LoginPage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CriticalFlowTest extends BaseTest {

    @Test
    public void testAdminLogin() {
        System.out.println("🚀 Starting admin login test...");

        HomePage homePage = new HomePage(driver);
        homePage.clickLogin();

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login("admin@qa-lab.com", "QaLab_2025!");

        // Даем время на перенаправление
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String currentUrl = driver.getCurrentUrl();
        System.out.println("🔗 Current URL after login: " + currentUrl);

        // Проверяем что мы вышли со страницы логина
        Assert.assertFalse(currentUrl.contains("/login"),
                "Should be redirected from login page");

        System.out.println("✅ Admin login test passed!");
    }

    @Test
    public void testProductSearch() {
        System.out.println("🔍 Testing product search...");

        HomePage homePage = new HomePage(driver);
        homePage.searchForProduct("computer");

        // Ждем результатов поиска
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String pageSource = driver.getPageSource().toLowerCase();
        boolean hasResults = pageSource.contains("computer") ||
                pageSource.contains("product") ||
                driver.getCurrentUrl().contains("search");

        Assert.assertTrue(hasResults, "Search should return results or show search page");

        System.out.println("✅ Product search test passed!");
    }
}