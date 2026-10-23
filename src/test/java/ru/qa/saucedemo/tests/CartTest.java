package ru.qa.saucedemo.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.qa.saucedemo.pages.InventoryPage;

import static ru.qa.saucedemo.data.Product.BACKPACK;
import static ru.qa.saucedemo.data.Product.BIKE_LIGHT;
import static ru.qa.saucedemo.data.Product.BOLT_T_SHIRT;
import static ru.qa.saucedemo.data.Product.FLEECE_JACKET;
import static ru.qa.saucedemo.data.Product.ONESIE;
import static ru.qa.saucedemo.data.Product.RED_T_SHIRT;

@Feature("Корзина")
@DisplayName("Корзина")
class CartTest extends BaseTest {

    private InventoryPage inventory;

    @BeforeEach
    void logIn() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("Счётчик на иконке корзины растёт при добавлении товаров")
    void cartBadgeCountsAddedProducts() {
        inventory.header().shouldShowCartCount(0);

        inventory.add(BACKPACK);
        inventory.header().shouldShowCartCount(1);

        inventory.add(BIKE_LIGHT).add(ONESIE);
        inventory.header().shouldShowCartCount(3);
    }

    @Test
    @DisplayName("Товар можно убрать из корзины прямо в каталоге")
    void productCanBeRemovedInCatalog() {
        inventory.add(BACKPACK)
                .add(FLEECE_JACKET)
                .remove(BACKPACK)
                .header()
                .shouldShowCartCount(1)
                .openCart()
                .shouldContainExactly(FLEECE_JACKET);
    }

    @Test
    @DisplayName("В корзине лежат добавленные товары, лишний можно удалить")
    void cartShowsAddedProductsAndAllowsRemoving() {
        inventory.add(BOLT_T_SHIRT)
                .add(RED_T_SHIRT)
                .header()
                .openCart()
                .shouldContainExactly(BOLT_T_SHIRT, RED_T_SHIRT)
                .remove(BOLT_T_SHIRT)
                .shouldContainExactly(RED_T_SHIRT);
    }
}
