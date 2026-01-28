package com.ecommerce.qa.api;

import io.qameta.allure.*;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;

@Epic("API Testing")
@Feature("Product API")
public class ProductAPITest {

    @BeforeClass
    public void setup() {
        RestAssured.baseURI = "http://localhost:8080";
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка доступности страницы продуктов")
    @Story("Страница с продуктами должна быть доступна")
    public void testProductsPageAvailable() {
        Response response = given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .extract()
                .response();

        String responseBody = response.asString();

        // Проверяем наличие ключевых элементов e-commerce
        boolean hasProducts = responseBody.toLowerCase().contains("product") ||
                responseBody.toLowerCase().contains("item") ||
                responseBody.toLowerCase().contains("shop");

        Allure.addAttachment("Products Page Check", "text/plain",
                "Status Code: " + response.getStatusCode() + "\n" +
                        "Contains 'product': " + responseBody.toLowerCase().contains("product") + "\n" +
                        "Contains 'item': " + responseBody.toLowerCase().contains("item") + "\n" +
                        "Contains 'shop': " + responseBody.toLowerCase().contains("shop") + "\n" +
                        "Page has e-commerce elements: " + hasProducts);
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка категорий продуктов")
    @Story("Категории продуктов должны быть доступны")
    public void testProductCategories() {
        String[] categories = {"computers", "electronics", "apparel", "digital-downloads"};

        for (String category : categories) {
            Response response = given()
                    .when()
                    .get("/" + category)
                    .then()
                    .statusCode(200)
                    .extract()
                    .response();

            Allure.addAttachment("Category: " + category, "text/plain",
                    "Status: " + response.getStatusCode() + "\n" +
                            "URL: " + response.getHeader("Location") + "\n" +
                            "Body contains category: " + response.asString().toLowerCase().contains(category));
        }
    }
}