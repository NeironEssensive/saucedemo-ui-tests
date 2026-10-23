# UI-автотесты SauceDemo

[![UI tests](https://github.com/NeironEssensive/saucedemo-ui-tests/actions/workflows/ui-tests.yml/badge.svg)](https://github.com/NeironEssensive/saucedemo-ui-tests/actions/workflows/ui-tests.yml)
[![Allure report](https://img.shields.io/badge/Allure-отчёт-orange)](https://neironessensive.github.io/saucedemo-ui-tests/)

Автотесты пользовательского интерфейса учебного интернет-магазина [SauceDemo](https://www.saucedemo.com):
вход, каталог, корзина и оформление заказа. Тесты запускаются в GitHub Actions на каждый push,
а свежий отчёт Allure публикуется на GitHub Pages.

**Отчёт последнего прогона:** https://neironessensive.github.io/saucedemo-ui-tests/

## Что проверяется

| Область | Проверки |
|---|---|
| Авторизация | успешный вход; ошибки для заблокированного пользователя, неверного пароля, пустого логина и пароля; выход через меню; каталог недоступен без входа |
| Каталог | отображаются все товары; сортировка по имени (A → Z, Z → A) и по цене (по возрастанию и убыванию) |
| Корзина | счётчик на иконке корзины; удаление товара в каталоге; содержимое корзины и удаление на странице корзины |
| Оформление заказа | полный путь от каталога до «Thank you for your order!»; обязательные поля формы покупателя; расчёт суммы, налога 8% и итога |

Всего 20 тестов (часть из них параметризованные).

## Стек

- Java 17, Maven (wrapper в репозитории — ставить Maven не нужно)
- [Selenide](https://selenide.org) — обёртка над Selenium WebDriver с умными ожиданиями
- JUnit 5 — в том числе `@ParameterizedTest` с `@MethodSource` и `@EnumSource`
- AssertJ — проверки списков и сумм
- Allure — отчёт с шагами и скриншотами
- GitHub Actions + GitHub Pages — CI и публикация отчёта

## Структура

```
src/test/java/ru/qa/saucedemo
├── data/                     тестовые данные
│   ├── User.java             пользователи SauceDemo
│   ├── Product.java          товары с ценами (BigDecimal)
│   ├── SortOrder.java        варианты сортировки
│   └── Customer.java         данные покупателя для формы заказа
├── pages/                    Page Object — по классу на страницу
│   ├── components/
│   │   └── HeaderComponent   шапка: корзина и меню, общая для всех страниц
│   ├── LoginPage
│   ├── InventoryPage         каталог
│   ├── CartPage
│   ├── CheckoutInfoPage      шаг 1: данные покупателя
│   ├── CheckoutOverviewPage  шаг 2: товары и суммы
│   └── CheckoutCompletePage
├── tests/                    тесты по функциональным областям
│   ├── BaseTest              настройка браузера и Allure
│   ├── LoginTest
│   ├── InventoryTest
│   ├── CartTest
│   └── CheckoutTest
└── util/
    └── Prices.java           разбор сумм вида "$29.99"
```

## Принятые решения

- **Page Object с fluent-интерфейсом.** Каждый метод страницы возвращает страницу, на которой окажется
  пользователь, поэтому тест читается как сценарий:
  `inventory.add(BACKPACK).header().openCart().checkout().continueWith(customer).finish()`.
  Селекторы живут только в классах страниц: если вёрстка поменяется, правится одно место.
- **Тесты не знают о селекторах.** В тестах нет ни одного `$("...")` — только действия и проверки
  на языке предметной области.
- **Стабильные селекторы.** Используются `id`, `data-test` и классы, а не XPath по позиции.
  Карточка товара ищется по его названию, поэтому тесты не зависят от порядка товаров на странице.
- **Изоляция тестов.** Для каждого теста запускается новый браузер (`closeWebDriver()` в `@AfterEach`),
  поэтому тесты не делят корзину и сессию и проходят в любом порядке.
- **Ожидаемые значения считаются, а не копируются.** В проверке итоговой суммы налог и итог
  вычисляются в тесте из цен товаров (`BigDecimal`, округление `HALF_UP`) и сравниваются со страницей.
- **Читаемый отчёт.** Методы страниц размечены `@Step` с русскими названиями, у тестов есть `@DisplayName`,
  при падении в отчёт прикладывается скриншот.
- **Headless в CI.** Локально тесты идут в обычном Chrome, а в GitHub Actions — в headless-режиме
  через `-Dselenide.headless=true`, без изменений в коде.

## Запуск

Нужны JDK 17+ и Google Chrome.

```bash
# все тесты, браузер виден
./mvnw test

# без окна браузера, как в CI
./mvnw test -Dselenide.headless=true

# один класс тестов
./mvnw test -Dtest=CheckoutTest

# открыть отчёт Allure в браузере после прогона
./mvnw allure:serve
```

На Windows вместо `./mvnw` — `mvnw.cmd`.

## CI

Workflow [`.github/workflows/ui-tests.yml`](.github/workflows/ui-tests.yml) запускается на push и pull request
в `main` и вручную (`workflow_dispatch`):

1. поднимает JDK 17 и кэширует зависимости Maven;
2. запускает тесты в headless Chrome;
3. собирает отчёт Allure — даже если тесты упали;
4. сохраняет отчёт как артефакт сборки и для `main` публикует его на GitHub Pages.
