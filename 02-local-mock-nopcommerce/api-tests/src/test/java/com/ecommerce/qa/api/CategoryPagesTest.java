package com.ecommerce.qa.api;

import io.qameta.allure.*;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@Epic("API Testing")
@Feature("Category Pages")
public class CategoryPagesTest {

    @BeforeClass
    public void setup() {
        baseURI = "http://localhost:8080";
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка страницы категории Animals")
    public void testAnimalsCategoryPage() {
        given()
                .when()
                .get("/animals")
                .then()
                .statusCode(200)
                .body(containsString("Animals"))
                .body(containsString("product-item"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка страницы категории Book")
    public void testBookCategoryPage() {
        given()
                .when()
                .get("/book-2")
                .then()
                .statusCode(200)
                .body(containsString("Book"))
                .body(containsString("product-item"));
    }
}