package com.ecommerce.qa.api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
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
    @Description("Check if nopCommerce is up and running")
    @Story("Application should respond to health check")
    public void testHealthCheck() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(not(emptyString()))
                .time(lessThan(5000L)); // Ответ должен быть менее 5 секунд
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check login page availability")
    @Story("Login page should be accessible")
    public void testLoginPageAvailable() {
        given()
                .when()
                .get("/login")
                .then()
                .statusCode(200)
                .body(containsString("Login"))
                .body(containsString("Email"))
                .body(containsString("Password"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Check registration page availability")
    @Story("Registration page should be accessible")
    public void testRegisterPageAvailable() {
        given()
                .when()
                .get("/register")
                .then()
                .statusCode(200)
                .body(containsString("Register"))
                .body(containsString("First Name"))
                .body(containsString("Last Name"));
    }
}