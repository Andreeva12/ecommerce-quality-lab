package com.ecommerce.qa.api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API Testing")
@Feature("Health Check API")
public class HealthCheckAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Severity(SeverityLevel.BLOCKER)
    @Description("Проверка доступности главной страницы")
    @Story("Главная страница должна отвечать со статусом 200")
    public void testHomePageAvailable() {
        Response response = given()
                .header("Accept", "text/html")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(not(emptyString()))
                .extract()
                .response();

        Allure.addAttachment("Home Page Response", "text/plain",
                "Status Code: " + response.getStatusCode() + "\n" +
                        "Response Body Length: " + response.asString().length() + "\n" +
                        "Contains 'nopCommerce': " + response.asString().contains("nopCommerce"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка доступности страницы логина")
    @Story("Страница логина должна быть доступна")
    public void testLoginPageAvailable() {
        given()
                .when()
                .get("/login")
                .then()
                .statusCode(200)
                .body(containsStringIgnoringCase("Login"))
                .body(containsStringIgnoringCase("Email"))
                .body(containsStringIgnoringCase("Password"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка доступности страницы регистрации")
    @Story("Страница регистрации должна быть доступна")
    public void testRegisterPageAvailable() {
        given()
                .when()
                .get("/register")
                .then()
                .statusCode(200)
                .body(containsStringIgnoringCase("Register"))
                .body(containsStringIgnoringCase("First Name"))
                .body(containsStringIgnoringCase("Last Name"));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка поиска через API")
    @Story("Поиск должен возвращать результаты")
    public void testSearchFunctionality() {
        Response response = given()
                .param("q", "computer")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .extract()
                .response();

        // Проверяем, что ответ содержит хотя бы один из ожидаемых терминов
        String responseBody = response.asString().toLowerCase();
        boolean hasSearchTerm = responseBody.contains("search") ||
                responseBody.contains("computer") ||
                responseBody.contains("product");

        Allure.addAttachment("Search Results", "text/plain",
                "Response contains 'search': " + responseBody.contains("search") + "\n" +
                        "Response contains 'computer': " + responseBody.contains("computer") + "\n" +
                        "Response contains 'product': " + responseBody.contains("product"));

        if (!hasSearchTerm) {
            throw new AssertionError("Search response doesn't contain expected terms");
        }
    }
}