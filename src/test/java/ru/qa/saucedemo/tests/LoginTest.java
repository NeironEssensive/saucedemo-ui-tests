package ru.qa.saucedemo.tests;

import com.codeborne.selenide.Selenide;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import ru.qa.saucedemo.data.Product;
import ru.qa.saucedemo.data.User;
import ru.qa.saucedemo.pages.LoginPage;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@Feature("Авторизация")
@DisplayName("Авторизация")
class LoginTest extends BaseTest {

    private final LoginPage loginPage = new LoginPage();

    @Test
    @DisplayName("Стандартный пользователь входит и видит каталог")
    void standardUserCanLogIn() {
        loginPage.open()
                .loginAs(User.STANDARD)
                .shouldHaveProductCount(Product.values().length);
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("invalidCredentials")
    @DisplayName("Вход с неверными данными показывает ошибку")
    void loginWithInvalidCredentialsShowsError(String caseName, String username, String password, String error) {
        loginPage.open()
                .submit(username, password)
                .shouldShowError(error);
    }

    static Stream<Arguments> invalidCredentials() {
        return Stream.of(
                arguments("Заблокированный пользователь",
                        User.LOCKED_OUT.username(), User.LOCKED_OUT.password(),
                        "Sorry, this user has been locked out."),
                arguments("Неверный пароль",
                        User.STANDARD.username(), "wrong_password",
                        "Username and password do not match any user in this service"),
                arguments("Пустой логин",
                        "", User.STANDARD.password(),
                        "Username is required"),
                arguments("Пустой пароль",
                        User.STANDARD.username(), "",
                        "Password is required"));
    }

    @Test
    @DisplayName("Пользователь выходит из аккаунта через меню")
    void userCanLogOut() {
        loginAsStandardUser()
                .header()
                .logout();
    }

    @Test
    @DisplayName("Каталог недоступен без входа")
    void catalogRequiresLogin() {
        Selenide.open("/inventory.html");
        loginPage.shouldBeOpened()
                .shouldShowError("when you are logged in");
    }
}
