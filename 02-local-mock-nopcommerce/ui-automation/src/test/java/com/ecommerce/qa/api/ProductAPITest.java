package com.ecommerce.qa.api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@Epic("API Testing")
@Feature("Product API")
public class ProductAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Test to verify products API returns data")
    @Story("API should return list of products")
    public void testGetProducts() {
        Response response = given()
                .header("Accept", "application/json")
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Allure.addAttachment("API Response", "text/plain", response.asString());

        // Проверяем что страница содержит ключевые элементы
        String body = response.asString();
        assert body.contains("nopCommerce") ||
                body.contains("store") ||
                body.contains("product") :
                "Page should contain e-commerce elements";
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Test search functionality via API")
    @Story("Search API should handle search requests")
    public void testSearchAPI() {
        given()
                .param("q", "computer")
                .when()
                .get("/search")
                .then()
                .statusCode(200);
    }
}