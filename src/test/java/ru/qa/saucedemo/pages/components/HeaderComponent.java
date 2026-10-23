package ru.qa.saucedemo.pages.components;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.pages.CartPage;
import ru.qa.saucedemo.pages.LoginPage;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Selenide.$;

/**
 * Шапка сайта: иконка корзины и боковое меню. Одна и та же на всех страницах после входа.
 */
public class HeaderComponent {

    private final SelenideElement cartLink = $(".shopping_cart_link");
    private final SelenideElement cartBadge = $(".shopping_cart_badge");
    private final SelenideElement menuButton = $("#react-burger-menu-btn");
    private final SelenideElement logoutLink = $("#logout_sidebar_link");

    @Step("Проверить, что на иконке корзины {count}")
    public HeaderComponent shouldShowCartCount(int count) {
        if (count == 0) {
            // у пустой корзины счётчика нет вообще, а не "0"
            cartBadge.shouldNot(exist);
        } else {
            cartBadge.shouldHave(exactText(String.valueOf(count)));
        }
        return this;
    }

    @Step("Открыть корзину")
    public CartPage openCart() {
        cartLink.click();
        return new CartPage().shouldBeOpened();
    }

    @Step("Выйти из аккаунта через боковое меню")
    public LoginPage logout() {
        menuButton.click();
        logoutLink.click();
        return new LoginPage().shouldBeOpened();
    }
}
