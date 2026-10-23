package ru.qa.saucedemo.pages;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.data.User;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {

    private final SelenideElement usernameInput = $("#user-name");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement loginButton = $("#login-button");
    private final SelenideElement error = $("[data-test='error']");

    @Step("Открыть страницу входа")
    public LoginPage open() {
        Selenide.open("/");
        return this;
    }

    @Step("Войти как {user.username}")
    public InventoryPage loginAs(User user) {
        fillAndSubmit(user.username(), user.password());
        return new InventoryPage().shouldBeOpened();
    }

    /**
     * Отправляет форму и остаётся на странице входа — для негативных проверок.
     */
    @Step("Отправить форму входа: логин «{username}», пароль «{password}»")
    public LoginPage submit(String username, String password) {
        fillAndSubmit(username, password);
        return this;
    }

    @Step("Проверить ошибку «{message}»")
    public LoginPage shouldShowError(String message) {
        error.shouldBe(visible).shouldHave(text(message));
        return this;
    }

    @Step("Проверить, что открыта страница входа")
    public LoginPage shouldBeOpened() {
        loginButton.shouldBe(visible);
        return this;
    }

    private void fillAndSubmit(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        loginButton.click();
    }
}
