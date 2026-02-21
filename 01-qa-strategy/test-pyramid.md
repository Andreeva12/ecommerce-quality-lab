# Test Strategy: Adapted Pyramid for E-Commerce

## 🏔️ Our Testing Pyramid

             [ Exploratory & UX Testing ]
                       |
        [ E2E UI Tests (Critical Paths Only) ]
                       |
      [ API & Integration Tests (Business Logic) ]
                       |
[ Unit & Component Tests (Developed by Dev Team) ]

## 📊 Distribution & Rationale
| Layer | % of Tests | Focus Area | Tools | Execution Frequency |
|-------|-----------|------------|-------|---------------------|
| **API/Integration** | 60% | Business logic, data flow, contracts | Postman, REST Assured | On every commit |
| **E2E UI** | 25% | Critical user journeys (login→checkout) | Selenium, TestNG | Nightly & PR triggers |
| **Exploratory** | 15% | UX, edge cases, usability | Manual, Session-based | Per sprint/release |

## 🔍 Why This Distribution?
1. **Speed:** API tests run faster (seconds vs minutes for UI)
2. **Stability:** Less flaky than UI tests
3. **Cost:** Cheaper to maintain
4. **Coverage:** Tests the actual business logic, not just the UI layer

## 🎯 Critical User Journeys (Automated)
1. **User Registration & Login** – implemented
2. **Product Search & Filtering** – implemented (Excel-driven)
3. **Cart Management** – add/remove/update quantity – implemented
4. **Checkout Process** – guest & logged-in – implemented + DB validation
5. **Order History & Tracking** – planned