![Java](https://img.shields.io/badge/Java-17-blue)
![Selenium](https://img.shields.io/badge/Selenium-4.15-green)
![Maven](https://img.shields.io/badge/Maven-3.6-red)
![Docker](https://img.shields.io/badge/Docker-✓-blue)
![Tests](https://img.shields.io/badge/Tests-16%20passing-brightgreen)
![CI/CD Status](https://github.com/Andreeva12/ecommerce-quality-lab/actions/workflows/ci-cd.yml/badge.svg)

# 🧪 eCommerce Quality Lab: Hybrid QA Engineering Portfolio

## 🎯 Project Vision
This project demonstrates my approach to **full-cycle Quality Engineering** for e-commerce systems. It implements a **hybrid strategy**:
- Controlled testing in a local mock environment (nopCommerce)
- Production-like audits (OpenCart demo)
- API testing via RestAssured and Postman/Newman
- CI/CD integration with GitHub Actions + Allure reporting

The goal is to showcase not just technical testing skills, but **product thinking, risk assessment, and engineering practices** bridging development and business impact.

---

## 🚀 Current Status
**✅ Automation Framework is LIVE and WORKING!**
- **UI Tests:** 9+ tests covering smoke, critical flows, cart, checkout (all passing)
- **API Tests:** 7+ tests (HealthCheck, Product API, Category Pages, Catalog JSON) with schema validation and negative scenarios
- **Database Validation:** Post-order checks implemented in `CheckoutTest`
- **Data-Driven Testing:** Excel integration (Apache POI) for product data verification
- **CI/CD:** GitHub Actions workflow with Allure reporting (partial, ongoing)
- **Reporting:** Allure reports integrated, published to GitHub Pages (optional)

---

## 🏗️ Architecture & Strategy

### 🔹 Local Controlled Environment — nopCommerce
- **UI Automation:** Java + Selenium + TestNG + Page Object Model
- **API Testing:** RestAssured + Postman/Newman
- **DB Checks:** JDBC validation after order placement
- **Test Data:** Excel-driven tests for product catalog

### 🔹 Production-like Environment — OpenCart Demo
- **Exploratory Testing** (planned)
- **UX & Usability Analysis** (planned)
- **Risk-Based Testing** (planned)
- **Business Impact Assessment** (planned)

---

## 🧪 Tests Implemented

### ✅ Smoke Tests (4 tests)
1. Home page title verification
2. Logo display check
3. Search box existence
4. Navigation links verification

### ✅ Critical Flow Tests (3 tests)
1. Admin login
2. Product search
3. Complete purchase flow (guest checkout)

### ✅ Cart & Checkout Tests (3 tests)
1. Add product to cart
2. Remove product from cart
3. Cart total calculation
4. Guest checkout with DB verification
5. Logged-in user checkout

### ✅ Data-Driven Product Tests (1 test, parameterized)
- Verifies product name, price, SKU, and image presence for all products from Excel

### ✅ Category Filter Tests (2 tests)
- Animals category contains correct products
- Books category contains correct products

### ✅ API Tests (RestAssured)
- **HealthCheckAPI:** Home, login, register, search (4 tests)
- **ProductAPI:** Home, categories, product pages (6 tests)
- **CategoryPages:** Animals, Book pages (2 tests)
- **CatalogJSON:** Autocomplete endpoint with schema validation and negative tests (3 tests)

### ✅ API Tests (Postman/Newman)
- Login endpoint
- Search products
- Health check
- All assertions passing (except expected 404 for missing category)

---

## 📁 Project Structure

```
ecommerce-quality-lab/
├── 01-qa-strategy/ # Strategy docs (quality vision, risk matrix)
├── 02-local-mock-nopcommerce/ # Main automation project
│ ├── docker-compose.yml # nopCommerce + MSSQL
│ ├── ui-automation/ # Selenium + TestNG tests
│ │ ├── pom.xml
│ │ ├── testng-allure.xml
│ │ ├── src/main/java/com/ecommerce/qa/
│ │ │ ├── components/
│ │ │ ├── pages/
│ │ │ └── utils/ # ExcelReader, ProductData
│ │ └── src/test/java/com/ecommerce/qa/
│ │ ├── api/ # RestAssured tests (moved to api‑tests module)
│ │ ├── framework/ # BaseTest
│ │ └── tests/ # UI test classes
│ ├── api-tests/ # Dedicated API tests module
│ │ ├── pom.xml
│ │ ├── api-tests-suite.xml
│ │ ├── src/test/java/com/ecommerce/qa/api/
│ │ │ ├── HealthCheckAPITest.java
│ │ │ ├── ProductAPITest.java
│ │ │ ├── CategoryPagesTest.java
│ │ │ └── CatalogAPITest.java # JSON autocomplete tests
│ │ └── src/test/resources/schemas/ # JSON schemas
│ │ ├── autocomplete-response-schema.json
│ │ └── add-to-cart-response-schema.json
│ ├── db-validation/ # DB helpers and SQL scripts
│ │ ├── pom.xml
│ │ ├── src/main/java/com/ecommerce/qa/db/DatabaseHelper.java
│ │ └── sql-checks.md
│ └── postman/ # Postman collections
│ ├── nopcommerce-collection.json
│ └── nopcommerce-environment.json
├── 03-production-audit-opencart/ # Planned
│ └── risk-matrix.md
├── .github/workflows/ci-cd.yml # CI/CD pipeline
└── README.md
```


## 🛠️ Tech Stack
```
| Category | Tools & Technologies |
|----------|---------------------|
| **Automation** | Java 17+, Selenium 4.20, TestNG 7.9, Maven, Page Object Model |
| **Driver Management** | WebDriverManager 5.8.0 |
| **API Testing** | RestAssured 5.4.0, Postman, Newman |
| **Data Handling** | Apache POI 5.2.5 (Excel), JDBC (SQL Server) |
| **Reporting** | Allure 2.25.0 |
| **Local Environment** | Docker, Docker Compose, nopCommerce, SQL Server |
| **CI/CD** | GitHub Actions |
| **Methodologies** | Risk-Based Testing, Shift-Left, Exploratory Testing |
```
## 🚀 Quick Start

### 1. Prerequisites
- Java 17+
- Maven 3.6+
- Docker & Docker Compose
- Git
- Node.js (for Newman)

### 2. Clone and Setup
```
bash
git clone https://github.com/Andreeva12/ecommerce-quality-lab.git
cd ecommerce-quality-lab
```
3. Start Local nopCommerce
  ```
bash
   cd 02-local-mock-nopcommerce
   docker-compose up -d
   ```
# Wait 1-2 minutes
# Access: http://localhost:8080
# Admin: admin@qa-lab.com / QaLab_2025!
4. Run All Tests
  ``` bash
   cd 02-local-mock-nopcommerce/ui-automation
   mvn clean test
   ```
5. Run API Tests Only
   ``` bash
   mvn test -DsuiteXmlFile=testng-api.xml
    ```
6. Generate Allure Report
   ```   bash
   mvn allure:serve
    ```
7. Run Postman Collection (optional)
   ``` bash
   cd ../api-tests/postman
   newman run nopcommerce-collection.json --environment nopcommerce-environment.json
   ```
📊 CI/CD Pipeline
GitHub Actions workflow (.github/workflows/ci-cd.yml) runs on:

push to main / develop

pull_request to main

nightly schedule (22:00 UTC)

Steps:

Start MSSQL service container

Checkout code

Setup JDK 17

Launch nopCommerce via Docker Compose

Run UI tests, API tests, Postman/Newman

Generate Allure report

Upload Allure results and Postman report as artifacts

Deploy Allure report to GitHub Pages (if configured)

📈 Key Metrics
Test stability: 100% (all tests pass on local & CI)

Coverage: 20+ UI scenarios, 10+ API endpoints

Execution time: UI ~30s, API ~5s (parallel)

Defect prevention: data integrity checks in checkout flow


📬 Connect & Feedback
This is a living portfolio project. Feel free to explore, raise issues, or connect for discussion via LinkedIn.

Last Updated: February 2026 | Status: Automation Framework ✅ Working
"Quality is not an act, it is a habit." — Aristotle