package com.ecommerce.qa.framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.io.ByteArrayInputStream;
import java.time.Duration;

public class BaseTest {
    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // Автоматически скачивает и настраивает chromedriver
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--headless");
        //options.addArguments("--start-maximized");
        options.addArguments("--disable-notifications");
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        Allure.step("Открываем главную страницу nopCommerce");
        driver.get("http://localhost:8080");

        // Ждем загрузки страницы
        waitForPageToLoad();

        // Обработка возможных алертов при загрузке
        handleAlertIfPresent();

        Allure.addAttachment("URL стартовой страницы", "text/plain", "http://localhost:8080");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            // Делаем скриншот при падении теста
            Allure.addAttachment("Скриншот при падении", new ByteArrayInputStream(
                    ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES)));

            Allure.addAttachment("Ошибка теста", "text/plain",
                    "Тест: " + result.getName() + "\n" +
                            "Статус: FAILED\n" +
                            "Ошибка: " + result.getThrowable().getMessage());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    // Метод для обработки алертов
    protected void handleAlertIfPresent() {
        try {
            // Проверяем наличие алерта без ожидания
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            Allure.addAttachment("Обнаружен Alert", "text/plain",
                    "Текст алерта: " + alertText + "\n" +
                            "Алерт закрыт автоматически");
            alert.accept(); // Закрываем алерт
            System.out.println("⚠️ Alert handled: " + alertText);
        } catch (NoAlertPresentException e) {
            // Алерта нет - это нормально
        }
    }

    // Метод для ожидания загрузки страницы
    protected void waitForPageToLoad() {
        wait.until(webDriver -> {
            String state = (String) ((JavascriptExecutor) webDriver)
                    .executeScript("return document.readyState");
            return state.equals("complete");
        });
    }

    // Метод для безопасного ожидания
    protected void safeWait(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
            System.out.println("Thread was interrupted: " + e.getMessage());
        }
    }
}