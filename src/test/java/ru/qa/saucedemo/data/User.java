package ru.qa.saucedemo.data;

/**
 * Тестовые пользователи SauceDemo. У всех один пароль — он указан прямо на странице входа.
 */
public record User(String username, String password) {

    private static final String PASSWORD = "secret_sauce";

    public static final User STANDARD = new User("standard_user", PASSWORD);
    public static final User LOCKED_OUT = new User("locked_out_user", PASSWORD);
}
