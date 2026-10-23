package ru.qa.saucedemo.pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.pages.components.HeaderComponent;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class CheckoutCompletePage {

    private final SelenideElement title = $(".title");
    private final SelenideElement completeHeader = $(".complete-header");
    private final HeaderComponent header = new HeaderComponent();

    @Step("Проверить, что открыта страница завершения заказа")
    public CheckoutCompletePage shouldBeOpened() {
        title.shouldHave(exactText("Checkout: Complete!"));
        return this;
    }

    @Step("Проверить сообщение об успешном заказе")
    public CheckoutCompletePage shouldShowSuccessMessage() {
        completeHeader.shouldHave(text("Thank you for your order!"));
        return this;
    }

    public HeaderComponent header() {
        return header;
    }
}
