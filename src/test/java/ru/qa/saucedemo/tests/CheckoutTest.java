package ru.qa.saucedemo.tests;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.qa.saucedemo.data.Customer;
import ru.qa.saucedemo.pages.CheckoutOverviewPage;
import ru.qa.saucedemo.pages.InventoryPage;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static ru.qa.saucedemo.data.Product.BACKPACK;
import static ru.qa.saucedemo.data.Product.BIKE_LIGHT;
import static ru.qa.saucedemo.data.Product.ONESIE;

@Feature("Оформление заказа")
@DisplayName("Оформление заказа")
class CheckoutTest extends BaseTest {

    private static final BigDecimal TAX_RATE = new BigDecimal("0.08");

    private InventoryPage inventory;

    @BeforeEach
    void logIn() {
        inventory = loginAsStandardUser();
    }

    @Test
    @DisplayName("Пользователь оформляет заказ от каталога до подтверждения")
    void userCanPlaceOrder() {
        inventory.add(BACKPACK)
                .header()
                .openCart()
                .checkout()
                .continueWith(Customer.valid())
                .shouldContainExactly(BACKPACK)
                .finish()
                .shouldShowSuccessMessage()
                .header()
                .shouldShowCartCount(0);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("incompleteCustomers")
    @DisplayName("Форма покупателя не пропускает пустые поля")
    void checkoutFormRequiresAllFields(String caseName, Customer customer, String error) {
        inventory.add(ONESIE)
                .header()
                .openCart()
                .checkout()
                .submitInvalid(customer)
                .shouldShowError(error);
    }

    static Stream<Arguments> incompleteCustomers() {
        Customer valid = Customer.valid();
        return Stream.of(
                arguments("Без имени", valid.withFirstName(""), "First Name is required"),
                arguments("Без фамилии", valid.withLastName(""), "Last Name is required"),
                arguments("Без индекса", valid.withPostalCode(""), "Postal Code is required"));
    }

    @Test
    @DisplayName("Итог заказа = стоимость товаров + налог 8%")
    void overviewCalculatesTotals() {
        CheckoutOverviewPage overview = inventory.add(BACKPACK)
                .add(BIKE_LIGHT)
                .header()
                .openCart()
                .checkout()
                .continueWith(Customer.valid());

        // ожидаемые суммы считаем сами из цен товаров, а не копируем со страницы
        BigDecimal itemTotal = BACKPACK.price().add(BIKE_LIGHT.price());
        BigDecimal tax = itemTotal.multiply(TAX_RATE).setScale(2, RoundingMode.HALF_UP);

        assertThat(overview.itemTotal()).isEqualByComparingTo(itemTotal);
        assertThat(overview.tax()).isEqualByComparingTo(tax);
        assertThat(overview.total()).isEqualByComparingTo(itemTotal.add(tax));
    }
}
