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
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка доступности главной страницы")
    @Story("Главная страница должна возвращать статус 200")
    public void testHomePage() {
        given()
                .when()
                .get("/")
                .then()
                .statusCode(200)
                .body(containsString("Your store"))
                .body(containsString("Home page"));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка категории Animals")
    @Story("Страница категории Animals должна быть доступна и содержать продукты")
    public void testAnimalsCategory() {
        Response response = given()
                .when()
                .get("/animals")
                .then()
                .statusCode(200)
                .body(containsString("Animals"))
                // Используем anyOf вместо ||
                .body(anyOf(containsString("Salmon"), containsString("Broccoli")))
                .extract()
                .response();

        String responseBody = response.asString();
        Allure.addAttachment("Animals Category Page", "text/plain",
                "URL: /animals\n" +
                        "Status Code: " + response.getStatusCode() + "\n" +
                        "Contains 'Animals': " + responseBody.contains("Animals") + "\n" +
                        "Contains 'Salmon': " + responseBody.contains("Salmon") + "\n" +
                        "Contains 'Broccoli': " + responseBody.contains("Broccoli") + "\n" +
                        "Page Length: " + responseBody.length() + " characters");
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка категории Book")
    @Story("Страница категории Book должна быть доступна")
    public void testBookCategory() {
        Response response = given()
                .when()
                .get("/book")
                .then()
                .statusCode(200)
                .body(containsString("Book"))
                .extract()
                .response();

        Allure.addAttachment("Book Category Page", "text/plain",
                "URL: /book\n" +
                        "Status Code: " + response.getStatusCode() + "\n" +
                        "Contains 'Book': " + response.asString().contains("Book") + "\n" +
                        "Page Length: " + response.asString().length() + " characters");
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка конкретного продукта Salmon")
    @Story("Страница продукта Salmon должна быть доступна")
    public void testSalmonProductPage() {
        given()
                .when()
                .get("/salmon-2-2")
                .then()
                .statusCode(200)
                .body(containsString("Salmon"))
                // Используем anyOf вместо ||
                .body(anyOf(containsString("product"), containsString("price")));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Проверка конкретного продукта Broccoli")
    @Story("Страница продукта Broccoli должна быть доступна")
    public void testBroccoliProductPage() {
        given()
                .when()
                .get("/broccoli-2")
                .then()
                .statusCode(200)
                .body(containsString("Broccoli"))
                // Используем anyOf вместо ||
                .body(anyOf(containsString("product"), containsString("price")));
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка поиска по сайту")
    @Story("Поиск должен возвращать результаты")
    public void testSearchFunctionality() {
        Response response = given()
                .param("q", "salmon")
                .when()
                .get("/search")
                .then()
                .statusCode(200)
                .extract()
                .response();

        Allure.addAttachment("Search Results", "text/plain",
                "Search Query: salmon\n" +
                        "Status Code: " + response.getStatusCode() + "\n" +
                        "Contains 'salmon': " + response.asString().toLowerCase().contains("salmon") + "\n" +
                        "Contains 'search': " + response.asString().toLowerCase().contains("search"));
    }
}