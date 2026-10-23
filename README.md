# SauceDemo UI Tests

[![UI tests](https://github.com/NeironEssensive/saucedemo-ui-tests/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/NeironEssensive/saucedemo-ui-tests/actions/workflows/ui-tests.yml)
[![Allure report](https://img.shields.io/badge/Allure-report-orange)](https://neironessensive.github.io/saucedemo-ui-tests/)

UI test automation for [SauceDemo](https://www.saucedemo.com), a demo online store:
login, product catalog, shopping cart and checkout. The tests run in GitHub Actions on every push,
and the latest Allure report is published to GitHub Pages.

**Latest test report:** https://neironessensive.github.io/saucedemo-ui-tests/

## What is covered

| Area | Checks |
|---|---|
| Login | successful login; errors for a locked-out user, wrong password, empty username and empty password; logout via the side menu; the catalog is not accessible without logging in |
| Catalog | all products are displayed; sorting by name (A → Z, Z → A) and by price (low to high, high to low) |
| Cart | cart badge counter; removing a product from the catalog; cart contents and removing a product on the cart page |
| Checkout | full flow from the catalog to "Thank you for your order!"; required fields of the customer form; item total, 8% tax and order total calculation |

20 tests in total, some of them parameterized.

## Tech stack

- Java 17, Maven (the wrapper is included, no need to install Maven)
- [Selenide](https://selenide.org) — a Selenium WebDriver wrapper with built-in smart waits
- JUnit 5 — including `@ParameterizedTest` with `@MethodSource` and `@EnumSource`
- AssertJ — assertions on lists and amounts
- Allure — report with steps and screenshots
- GitHub Actions + GitHub Pages — CI and report publishing

## Project structure

```
src/test/java/ru/qa/saucedemo
├── data/                     test data
│   ├── User.java             SauceDemo users
│   ├── Product.java          products with prices (BigDecimal)
│   ├── SortOrder.java        sort options
│   └── Customer.java         customer data for the checkout form
├── pages/                    Page Objects, one class per page
│   ├── components/
│   │   └── HeaderComponent   header with cart and menu, shared by all pages
│   ├── LoginPage
│   ├── InventoryPage         product catalog
│   ├── CartPage
│   ├── CheckoutInfoPage      step 1: customer information
│   ├── CheckoutOverviewPage  step 2: items and totals
│   └── CheckoutCompletePage
├── tests/                    tests grouped by feature
│   ├── BaseTest              browser and Allure setup
│   ├── LoginTest
│   ├── InventoryTest
│   ├── CartTest
│   └── CheckoutTest
└── util/
    └── Prices.java           parses amounts like "$29.99"
```

## Design decisions

- **Page Object with a fluent interface.** Every page method returns the page the user ends up on,
  so a test reads like a scenario:
  `inventory.add(BACKPACK).header().openCart().checkout().continueWith(customer).finish()`.
  Selectors live only in page classes: if the markup changes, there is one place to fix.
- **Tests know nothing about selectors.** There is not a single `$("...")` in the tests — only actions
  and checks in the language of the domain.
- **Stable locators.** Tests use `id`, `data-test` attributes and CSS classes instead of positional XPath.
  A product card is found by the product name, so the tests do not depend on the order of products on the page.
- **Test isolation.** Each test gets a fresh browser (`closeWebDriver()` in `@AfterEach`),
  so tests never share a cart or a session and pass in any order.
- **Expected values are calculated, not copied.** The checkout total test computes the tax and the total
  from product prices (`BigDecimal`, `HALF_UP` rounding) and compares them with the page.
- **Readable report.** Page methods are annotated with `@Step`, tests have `@DisplayName`,
  and a screenshot is attached to the report when a test fails.
  Test names and step descriptions are written in Russian.
- **Headless in CI.** Locally the tests run in a regular Chrome window; in GitHub Actions they run headless
  via `-Dselenide.headless=true`, with no code changes.

## Running the tests

Requires JDK 17+ and Google Chrome.

```bash
# all tests, browser window visible
./mvnw test

# headless, same as in CI
./mvnw test -Dselenide.headless=true

# a single test class
./mvnw test -Dtest=CheckoutTest

# open the Allure report in a browser after the run
./mvnw allure:serve
```

On Windows, use `mvnw.cmd` instead of `./mvnw`.

## CI

The workflow [`.github/workflows/ui-tests.yml`](.github/workflows/ui-tests.yml) runs on push and pull request
to `main`, and can be started manually (`workflow_dispatch`). It:

1. sets up JDK 17 and caches Maven dependencies;
2. runs the tests in headless Chrome;
3. builds the Allure report, even if some tests failed;
4. uploads the report as a build artifact and, for `main`, publishes it to GitHub Pages.
