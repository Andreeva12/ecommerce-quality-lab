# 🧪 eCommerce Quality Lab: Hybrid QA Engineering Portfolio

## 🎯 Project Vision
This project demonstrates my approach to **full-cycle Quality Engineering** for e-commerce systems. It implements a **hybrid strategy**: starting with controlled testing in a local mock environment (nopCommerce) and extending to production-like audits (OpenCart demo).

**The goal is to showcase:** not just technical testing skills, but **product thinking, risk assessment, and engineering practices** that bridge development and business impact.

## 🚀 Current Status
**✅ Automation Framework is LIVE and WORKING!**
- 4 smoke tests passing successfully
- Local nopCommerce environment running via Docker
- Complete Page Object Model implementation
- Automated test execution via Maven

## 🏗️ Architecture & Strategy
### 🔧 Phase 1: Local Controlled Environment (`/02-local-mock-nopcommerce`)
**Status:** ✅ COMPLETE & WORKING
- **Purpose:** Shift-Left testing in isolation
- **Focus:** UI automation, critical user flows, data validation
- **Tech Stack:** Java 11, Selenium 4.15, TestNG, WebDriverManager, Maven
- **Tests Implemented:**
  - Home page load validation
  - Logo and navigation verification
  - Search functionality testing
  - Critical link availability check

### 🎭 Phase 2: Production-like Audit (`/03-production-audit-opencart`)
**Status:** 📋 PLANNED
- **Purpose:** Real-world exploratory testing
- **Focus:** UX, cross-browser compatibility, real-user scenarios
- **Deliverables:** Bug reports, risk analysis, usability findings

### 🔄 Phase 3: Continuous Quality (`/.github/workflows`)
**Status:** 🏗️ IN PROGRESS
- **Purpose:** Engineering mindset demonstration
- **Focus:** CI/CD integration, automated reporting
- **Tech:** GitHub Actions, Allure Reports

## 📁 Project Structure
```
ecommerce-quality-lab/
├── 01-qa-strategy/ # Strategic documentation
│ ├── quality-vision.md
│ ├── risk-based-testing.md
│ ├── test-pyramid.md
│ └── test-strategy-executive-summary.md
│
├── 02-local-mock-nopcommerce/ # ✅ WORKING AUTOMATION
│ ├── docker-compose.yml # Local nopCommerce setup
│ ├── ui-automation/ # ✅ COMPLETE FRAMEWORK
│ │ ├── pom.xml # Maven configuration
│ │ ├── testng.xml # TestNG configuration
│ │ ├── src/main/java/com/ecommerce/qa/
│ │ │ ├── framework/ # BaseTest, DriverManager
│ │ │ └── pages/ # Page Objects (HomePage, LoginPage)
│ │ └── src/test/java/com/ecommerce/qa/tests/
│ │ ├── SmokeTest.java # ✅ 4 passing tests
│ │ └── CriticalFlowTest.java
│ ├── api-tests/ # 📋 Planned
│ ├── db-validation/ # 📋 Planned
│ └── test-data/ # 📋 Planned
│
├── 03-production-audit-opencart/# 📋 Planned
│ ├── exploratory-testing/
│ ├── ux-usability-findings/
│ └── bug-reports/
│
├── .github/workflows/ # 🏗️ In Progress
├── .gitignore
└── README.md # This file
```

## 🛠️ Tech Stack
| Category | Tools & Technologies |
|----------|---------------------|
| **Automation** | Java 11+, Selenium WebDriver 4.15, TestNG 7.8, Maven, Page Object Model |
| **Driver Management** | WebDriverManager 5.6.3 (automatic) |
| **Local Environment** | Docker, Docker Compose, nopCommerce, SQL Server |
| **CI/CD** | GitHub Actions (planned), Allure Reports (planned) |
| **Methodologies** | Risk-Based Testing, Shift-Left, Exploratory Testing, Agile |

## 🚀 Quick Start
1. **Explore the strategy:** Begin with `/01-qa-strategy/` to understand the testing approach
2. **Run local tests:** Navigate to `/02-local-mock-nopcommerce/` and follow README there
3. **Review audit findings:** Check `/03-production-audit-opencart/` for real-world QA artifacts

## 📈 Key Principles Demonstrated
- **Hybrid Testing Approach:** Combining precise automated checks with exploratory manual testing
- **Risk-Based Prioritization:** Focusing effort on high-impact business scenarios
- **Engineering Mindset:** Building maintainable, version-controlled test assets
- **Business Alignment:** Connecting test scenarios to real user journeys and metrics

## 📬 Connect & Feedback
This is a living portfolio project. Feel free to explore, raise issues, or connect for discussion via [LinkedIn](https://www.linkedin.com/in/katsiaryna-malashchytskaya-741a40300).

---
*"Quality is not an act, it is a habit." — Aristotle*
