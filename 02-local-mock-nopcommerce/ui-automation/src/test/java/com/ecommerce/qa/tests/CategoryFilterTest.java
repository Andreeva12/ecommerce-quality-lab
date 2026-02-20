package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.utils.ExcelReader;
import com.ecommerce.qa.utils.ProductData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryFilterTest extends BaseTest {

    @Test
    public void testAnimalsCategory() {
        driver.get("http://localhost:8080/animals");
        List<WebElement> productTitles = driver.findElements(By.cssSelector(".product-title a"));
        List<String> actualNames = productTitles.stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());

        List<String> expectedNames = ExcelReader.readProducts().stream()
                .filter(p -> p.getCategories() != null && p.getCategories().contains("Animals"))
                .map(ProductData::getName)
                .map(String::trim)
                .collect(Collectors.toList());

        Assert.assertEquals(actualNames, expectedNames,
                "Products in Animals category do not match Excel data");
    }

    @Test
    public void testBooksCategory() {
        driver.get("http://localhost:8080/book");
        List<WebElement> productTitles = driver.findElements(By.cssSelector(".product-title a"));
        List<String> actualNames = productTitles.stream()
                .map(el -> el.getText().trim())
                .collect(Collectors.toList());

        List<String> expectedNames = ExcelReader.readProducts().stream()
                .filter(p -> p.getCategories() != null && p.getCategories().contains("Book"))
                .map(ProductData::getName)
                .map(String::trim)
                .collect(Collectors.toList());

        Assert.assertEquals(actualNames, expectedNames,
                "Products in Book category do not match Excel data");
    }
}