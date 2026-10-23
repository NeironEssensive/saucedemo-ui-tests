package ru.qa.saucedemo.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import ru.qa.saucedemo.data.Product;
import ru.qa.saucedemo.data.SortOrder;
import ru.qa.saucedemo.pages.InventoryPage;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Feature("Каталог")
@DisplayName("Каталог товаров")
class InventoryTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void logIn() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("В каталоге есть все товары магазина")
    void catalogShowsAllProducts() {
        inventory.shouldHaveProductCount(Product.values().length);

        assertThat(inventory.productNames())
                .containsExactlyInAnyOrderElementsOf(Product.titles());
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(value = SortOrder.class, names = {"NAME_A_TO_Z", "NAME_Z_TO_A"})
    @DisplayName("Сортировка по имени")
    void sortByName(SortOrder order) {
        List<String> names = inventory.sortBy(order).productNames();

        Comparator<String> alphabetical = Comparator.naturalOrder();
        assertThat(names).isSortedAccordingTo(
                order == SortOrder.NAME_A_TO_Z ? alphabetical : alphabetical.reversed());
    }

    @ParameterizedTest(name = "{0}")
    @EnumSource(value = SortOrder.class, names = {"PRICE_LOW_TO_HIGH", "PRICE_HIGH_TO_LOW"})
    @DisplayName("Сортировка по цене")
    void sortByPrice(SortOrder order) {
        List<BigDecimal> prices = inventory.sortBy(order).productPrices();

        Comparator<BigDecimal> cheapFirst = Comparator.naturalOrder();
        assertThat(prices).isSortedAccordingTo(
                order == SortOrder.PRICE_LOW_TO_HIGH ? cheapFirst : cheapFirst.reversed());
    }
}
