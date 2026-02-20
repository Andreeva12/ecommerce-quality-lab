package com.ecommerce.qa.tests;

import com.ecommerce.qa.framework.BaseTest;
import com.ecommerce.qa.pages.ProductPage;
import com.ecommerce.qa.utils.ExcelReader;
import com.ecommerce.qa.utils.ProductData;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ProductDataDrivenTest extends BaseTest {

    @DataProvider(name = "productData")
    public Object[][] getProductData() {
        return ExcelReader.readProducts().stream()
                .map(p -> new Object[]{p})
                .toArray(Object[][]::new);
    }

    @Test(dataProvider = "productData")
    public void verifyProductPage(ProductData expectedProduct) {
        // Переход на страницу товара по ID
        driver.get("http://localhost:8080/product/" + expectedProduct.getProductId());
        ProductPage productPage = new ProductPage(driver);

        // Проверка названия
        String actualName = productPage.getProductName().trim();
        Assert.assertEquals(actualName, expectedProduct.getName().trim(),
                "Product name mismatch for ID: " + expectedProduct.getProductId());

        // Проверка цены
        double actualPrice = productPage.getProductPrice();
        Assert.assertEquals(actualPrice, expectedProduct.getPrice(), 0.01,
                "Product price mismatch for ID: " + expectedProduct.getProductId());

        // Проверка SKU
        String actualSku = productPage.getSKU();
        Assert.assertEquals(actualSku, expectedProduct.getSku(),
                "Product SKU mismatch for ID: " + expectedProduct.getProductId());
    }
}