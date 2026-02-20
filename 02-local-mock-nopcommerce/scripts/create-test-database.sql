-- 1. Создаём тестовую базу данных
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'nopCommerce_test')
BEGIN
    CREATE DATABASE nopCommerce_test;
END
GO

USE nopCommerce_test;
GO

-- 2. Таблица продуктов
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Product')
BEGIN
CREATE TABLE Product (
                         Id INT PRIMARY KEY IDENTITY(1,1),
                         Name NVARCHAR(255) NOT NULL,
                         Price DECIMAL(18,2) NOT NULL,
                         SKU NVARCHAR(100),
                         StockQuantity INT DEFAULT 100,
                         Published BIT DEFAULT 1,               -- добавили поле Published
                         CreatedOnUtc DATETIME DEFAULT GETUTCDATE()
);

INSERT INTO Product (Name, Price, SKU, StockQuantity, Published) VALUES
                                                                     ('Salmon Fish', 24.99, 'SAL001', 50, 1),
                                                                     ('A Court of Thorns and Roses', 19.99, 'BOOK001', 100, 1),
                                                                     ('Gaming Mouse', 49.99, 'GM001', 30, 1),
                                                                     ('Wireless Headphones', 129.99, 'WH001', 25, 1),
                                                                     ('USB-C Cable', 12.99, 'CABLE001', 200, 1);
END
GO

-- 3. Таблица заказов
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Orders')
BEGIN
CREATE TABLE Orders (
                        Id INT PRIMARY KEY IDENTITY(1,1),
                        CustomOrderNumber NVARCHAR(50) UNIQUE NOT NULL,
                        CustomerEmail NVARCHAR(255),
                        OrderTotal DECIMAL(18,2) NOT NULL,
                        OrderStatus NVARCHAR(50) DEFAULT 'Pending',
                        PaymentStatus NVARCHAR(50) DEFAULT 'Pending',
                        ShippingStatus NVARCHAR(50) DEFAULT 'Not Yet Shipped',
                        CreatedOnUtc DATETIME DEFAULT GETUTCDATE()
);
END
GO

-- 4. Таблица позиций заказа
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'OrderItem')
BEGIN
CREATE TABLE OrderItem (
                           Id INT PRIMARY KEY IDENTITY(1,1),
                           OrderId INT NOT NULL FOREIGN KEY REFERENCES Orders(Id),
                           ProductId INT NOT NULL FOREIGN KEY REFERENCES Product(Id),
                           Quantity INT NOT NULL,
                           UnitPrice DECIMAL(18,2) NOT NULL,
                           DiscountAmount DECIMAL(18,2) DEFAULT 0
);
END
GO