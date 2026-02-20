package com.ecommerce.qa.api;

import io.qameta.allure.*;
import io.restassured.module.jsv.JsonSchemaValidator;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.*;

@Epic("API Testing")
@Feature("Catalog JSON API")
public class CatalogAPITest {

    @BeforeClass
    public void setup() {
        baseURI = "http://localhost:8080";
        enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    @Severity(SeverityLevel.CRITICAL)
    @Description("Проверка автодополнения поиска (JSON)")
    public void testSearchAutocomplete() {
        given()
                .queryParam("term", "sal")
                .when()
                .get("/catalog/searchtermautocomplete")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/autocomplete-response-schema.json"))
                .body("size()", greaterThan(0));
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Поиск с пустым term")
    public void testAutocompleteEmptyTerm() {
        given()
                .queryParam("term", "")
                .when()
                .get("/catalog/searchtermautocomplete")
                .then()
                .statusCode(200)
                .body(equalTo(""));  // сервер возвращает пустое тело
    }

    @Test
    @Severity(SeverityLevel.NORMAL)
    @Description("Поиск с term, не дающим результатов")
    public void testAutocompleteNoResults() {
        given()
                .queryParam("term", "xyz123nonexistent")
                .when()
                .get("/catalog/searchtermautocomplete")
                .then()
                .statusCode(200)
                .contentType(JSON)
                .body("size()", is(0));
    }

    }