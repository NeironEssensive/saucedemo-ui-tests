package ru.qa.saucedemo.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.data.Customer;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

/**
 * Шаг 1 оформления заказа: имя, фамилия, индекс.
 */
public class CheckoutInfoPage {

    private final SelenideElement title = $(".title");
    private final SelenideElement firstNameInput = $("#first-name");
    private final SelenideElement lastNameInput = $("#last-name");
    private final SelenideElement postalCodeInput = $("#postal-code");
    private final SelenideElement continueButton = $("#continue");
    private final SelenideElement error = $("[data-test='error']");

    @Step("Проверить, что открыта форма данных покупателя")
    public CheckoutInfoPage shouldBeOpened() {
        title.shouldHave(exactText("Checkout: Your Information"));
        return this;
    }

    @Step("Заполнить данные покупателя ({customer.firstName} {customer.lastName}) и продолжить")
    public CheckoutOverviewPage continueWith(Customer customer) {
        fillAndContinue(customer);
        return new CheckoutOverviewPage().shouldBeOpened();
    }

    /**
     * Отправляет форму и остаётся на странице — для проверок валидации.
     */
    @Step("Отправить форму с неполными данными")
    public CheckoutInfoPage submitInvalid(Customer customer) {
        fillAndContinue(customer);
        return this;
    }

    @Step("Проверить ошибку «{message}»")
    public CheckoutInfoPage shouldShowError(String message) {
        error.shouldBe(visible).shouldHave(text(message));
        return this;
    }

    private void fillAndContinue(Customer customer) {
        firstNameInput.setValue(customer.firstName());
        lastNameInput.setValue(customer.lastName());
        postalCodeInput.setValue(customer.postalCode());
        continueButton.click();
    }
}
