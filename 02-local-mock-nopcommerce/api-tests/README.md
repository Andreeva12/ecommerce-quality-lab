# API Testing for nopCommerce

## 📋 Overview
This directory contains API tests for the nopCommerce e-commerce platform using multiple approaches:
1. **RestAssured** – Java-based API automation tests (integrated in `ui-automation` module)
2. **Postman** – Manual/exploratory API testing with collections

## 🏗️ Structure
``` 
api-tests/
├── postman/
│ ├── nopcommerce-collection.json # Postman collection
│ └── nopcommerce-environment.json # Postman environment variables
└── README.md # This file
```

## 🚀 Quick Start

### Prerequisites
- Java 17+ installed
- Maven installed
- Docker installed (for running nopCommerce)
- Node.js installed (for Newman)

### Running RestAssured Tests
```bash
# 1. Start nopCommerce
cd 02-local-mock-nopcommerce
docker-compose up -d

# 2. Run API tests (they are part of ui-automation module)
cd ui-automation
mvn test -DsuiteXmlFile=testng-api.xml

# 3. Generate Allure report
mvn allure:serve
Running Postman Collection
Option 1: Using Postman GUI

Open Postman

Import nopcommerce-collection.json

Import nopcommerce-environment.json

Select "nopCommerce Local" environment

Run the collection

Option 2: Using Newman (CLI)
``` # Install Newman
npm install -g newman

# Run collection
cd api-tests/postman
newman run nopcommerce-collection.json \
  --environment nopcommerce-environment.json \
  --reporters cli,html \
  --reporter-html-export report.html
```

🧪 Test Coverage
Health Checks
✅ Home page availability

✅ Login page accessibility

✅ Register page accessibility

✅ Search functionality

Product & Category Testing
✅ Category pages (/animals, /book-2) – HTML validation

✅ JSON autocomplete endpoint (/catalog/searchtermautocomplete) – schema validation, negative tests

JSON API Tests (RestAssured)
✅ Autocomplete with valid term – returns non-empty array matching schema

✅ Autocomplete with empty term – returns empty body

✅ Autocomplete with non-existent term – returns empty JSON array

📊 Reporting
Allure Reports (RestAssured)
```
mvn allure:report     # generate report
mvn allure:serve      # open in browser 
```

Newman Reports
```
newman run collection.json --reporters cli,json,html
```

🔧 CI/CD Integration
API tests run on every push to main/develop

Nightly regression at 22:00 UTC

Allure and Postman reports uploaded as artifacts

GitHub Pages hosts the latest Allure report: https://<your-username>.github.io/ecommerce-quality-lab/allure-report/

🐛 Troubleshooting
Common Issues
nopCommerce not running – ensure Docker containers are up (docker-compose ps)

Connection refused – check if port 8080 is available

Test failures – verify nopCommerce is fully started (wait 45+ seconds)

Debug Mode
```
mvn test -DsuiteXmlFile=testng-api.xml -X
```
📈 Next Steps
Add more JSON endpoints (add to cart, wishlist)

Implement data-driven testing with Excel

Add performance testing (response time thresholds)

Integrate with monitoring tools


---

## 📁 02-local-mock-nopcommerce/db-validation/sql-checks.md (обновлённый)

```markdown
# Database Validation Scripts

## Purpose
Validate data integrity between:
1. UI displayed values
2. API response data  
3. Database stored values
```

## Key Validation Areas
- Product pricing consistency (Excel ↔ UI ↔ DB)
- Order creation after checkout (Orders & OrderItem tables)
- Stock quantity updates after purchase (planned)

## Implemented Checks
- **Post-order validation:** After successful checkout, the test queries the database to verify:
  - Order exists in `[Order]` table with correct total
  - Corresponding `OrderItem` records exist with correct product IDs and quantities
- **Excel-driven product tests:** Product name, price, SKU are validated against the database (via UI layer)

## Scripts
The following SQL script initializes test data and tables (if not using nopCommerce's own schema).  
**Note:** In production-like environment (OpenCart), separate validation scripts will be created.

```sql
-- 1. Создаём тестовую базу данных (если используется отдельная)
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'nopCommerce_test')
BEGIN
    CREATE DATABASE nopCommerce_test;
END
GO

USE nopCommerce_test;
GO
```

-- 2. Таблица продуктов (пример)
```
IF NOT EXISTS (SELECT * FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_NAME = 'Product')
BEGIN
CREATE TABLE Product (
    Id INT PRIMARY KEY IDENTITY(1,1),
    Name NVARCHAR(255) NOT NULL,
    Price DECIMAL(18,2) NOT NULL,
    SKU NVARCHAR(100),
    StockQuantity INT DEFAULT 100,
    Published BIT DEFAULT 1,
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
```

-- 3. Таблица заказов
```
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
```
-- 4. Таблица позиций заказа
```
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
```
Scripts are used in automated tests to verify data integrity.

---

## 📁 03-production-audit-opencart/risk-matrix.md (обновлённый)

```markdown
# Production Audit Risk Matrix – OpenCart Demo

## Assessment Criteria
- **Likelihood:** How probable is this issue to occur? (Low/Medium/High)
- **Impact:** What is the business consequence if it occurs? (Minor/Moderate/Critical)

## Risk Rating Scale
- **Critical:** Immediate action required
- **High:** Address in current sprint  
- **Medium:** Plan for next release
- **Low:** Monitor and consider

## Current Findings (To be populated during exploratory testing)
```
| Risk Area | Likelihood | Impact | Priority | Observation | Recommendation |
|-----------|------------|--------|----------|-------------|----------------|
| **Checkout as Guest** | Medium | Critical | High | Guest checkout flow may be unclear; users might abandon | Improve UI hints, test with real users |
| **Product Filtering** | High | Medium | Medium | Filters sometimes return empty results unexpectedly | Verify filter logic, add error messages |
| **Mobile Menu** | High | Moderate | Medium | Hamburger menu not working on some devices | Test on multiple devices, fix responsiveness |
| **Payment Error Handling** | Low | Critical | High | Vague error messages when payment fails | Provide specific error details |
```
*This document will be updated as exploratory testing sessions are conducted.*

