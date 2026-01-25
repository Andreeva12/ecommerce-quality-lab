# API Testing for nopCommerce

## 📋 Overview
This directory contains API tests for the nopCommerce e-commerce platform using multiple approaches:
1. **RestAssured** - Java-based API automation tests
2. **Postman** - Manual/exploratory API testing with collections

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
1. Java 17+ installed
2. Maven installed
3. Docker installed (for running nopCommerce)
4. Node.js installed (for Newman)

### Running RestAssured Tests
```bash
# 1. Start nopCommerce
cd 02-local-mock-nopcommerce
docker-compose up -d

# 2. Run API tests
cd ui-automation
mvn test -DsuiteXmlFile=testng-api.xml

# 3. Generate Allure report
mvn allure:serve
```

### Running Postman Collection
Option 1: Using Postman GUI
1. Open Postman
2. Import nopcommerce-collection.json
3. Import nopcommerce-environment.json
4. Select "nopCommerce Local" environment
5. Run the collection

Option 2: Using Newman (CLI)
```bash
# Install Newman
npm install -g newman

# Run collection
newman run nopcommerce-collection.json \
  --environment nopcommerce-environment.json \
  --reporters cli,html \
  --reporter-html-export report.html
```
### 🧪 Test Coverage
Health Checks
```
✅ Home page availability
✅ Login page accessibility
✅ Register page accessibility
✅ Search functionality
```
### Product Testing
```
✅ Products page availability
✅ Category navigation
```
### Authentication
```
✅ Login form submission
✅ Registration flow
```
### 📊 Reporting
Allure Reports (RestAssured)
```bash
mvn allure:report
mvn allure:serve
```

### Newman Reports
```bash
newman run collection.json --reporters cli,json,html
```
### 🔧 CI/CD Integration
```
-The API tests are integrated into GitHub Actions workflow:
-Runs on every push to main/develop
-Nightly regression tests at 22:00
-Uploads Allure and Postman reports as artifacts
```

### 🐛 Troubleshooting
Common Issues
1. nopCommerce not running: Ensure Docker containers are up
2. Connection refused: Check if port 8080 is available
3. Test failures: Verify nopCommerce is fully started (wait 45+ seconds)

### Debug Mode
```bash
# Run tests with detailed logging
mvn test -DsuiteXmlFile=testng-api.xml -X
```
### 📈 Next Steps
```
-Add more API endpoints
-Implement data-driven testing
-Add performance testing
-Integrate with monitoring tools
```