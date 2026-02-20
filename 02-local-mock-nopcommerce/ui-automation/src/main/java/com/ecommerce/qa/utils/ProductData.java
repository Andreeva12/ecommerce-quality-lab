package com.ecommerce.qa.utils;

public class ProductData {
    private int productId;
    private String name;
    private String sku;
    private double price;
    private String categories;

    // Геттеры и сеттеры
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getCategories() { return categories; }
    public void setCategories(String categories) { this.categories = categories; }

    @Override
    public String toString() {
        return "ProductData{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", categories='" + categories + '\'' +
                '}';
    }
}