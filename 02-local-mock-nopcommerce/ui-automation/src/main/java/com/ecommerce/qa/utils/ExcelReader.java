package com.ecommerce.qa.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelReader {

    private static final String FILE_PATH = "src/test/resources/productss.xlsx";

    public static List<ProductData> readProducts() {
        List<ProductData> products = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(FILE_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row header = sheet.getRow(0);

            int productIdCol = -1, nameCol = -1, skuCol = -1, priceCol = -1, categoriesCol = -1;
            for (Cell cell : header) {
                String headerValue = cell.getStringCellValue();
                switch (headerValue) {
                    case "ProductId": productIdCol = cell.getColumnIndex(); break;
                    case "Name": nameCol = cell.getColumnIndex(); break;
                    case "SKU": skuCol = cell.getColumnIndex(); break;
                    case "Price": priceCol = cell.getColumnIndex(); break;
                    case "Categories": categoriesCol = cell.getColumnIndex(); break;
                }
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                ProductData product = new ProductData();
                if (productIdCol != -1) product.setProductId((int) row.getCell(productIdCol).getNumericCellValue());
                if (nameCol != -1) product.setName(row.getCell(nameCol).getStringCellValue());
                if (skuCol != -1) product.setSku(row.getCell(skuCol).getStringCellValue());
                if (priceCol != -1) product.setPrice(row.getCell(priceCol).getNumericCellValue());
                if (categoriesCol != -1) product.setCategories(row.getCell(categoriesCol).getStringCellValue());

                products.add(product);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return products;
    }
}