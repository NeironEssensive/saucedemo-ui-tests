package ru.qa.saucedemo.tests;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeOptions;
import ru.qa.saucedemo.data.User;
import ru.qa.saucedemo.pages.InventoryPage;
import ru.qa.saucedemo.pages.LoginPage;

import java.util.Map;

public abstract class BaseTest {

    @BeforeAll
    static void setUpBrowser() {
        Configuration.baseUrl = System.getProperty("selenide.baseUrl", "https://www.saucedemo.com");
        Configuration.browserSize = "1920x1080";
        Configuration.timeout = 6000;
        Configuration.browserCapabilities = chromeOptions();
        // headless включается флагом -Dselenide.headless=true — так тесты запускаются в CI

        // каждое действие Selenide становится шагом отчёта, при падении прикладывается скриншот
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide()
                .screenshots(true)
                .savePageSource(false));
    }

    /**
     * Новый браузер на каждый тест: тесты не делят корзину и сессию и могут идти в любом порядке.
     */
    @AfterEach
    void closeBrowser() {
        Selenide.closeWebDriver();
    }

    protected InventoryPage loginAsStandardUser() {
        return new LoginPage().open().loginAs(User.STANDARD);
    }

    private static ChromeOptions chromeOptions() {
        // пароль secret_sauce есть в базах утечек, и без этих настроек Chrome после входа
        // показывает окно «Смените пароль», которое мешает работе со страницей
        Map<String, Object> prefs = Map.of(
                "credentials_enable_service", false,
                "profile.password_manager_enabled", false,
                "profile.password_manager_leak_detection", false);
        return new ChromeOptions()
                .addArguments("--disable-features=PasswordLeakDetection")
                .setExperimentalOption("prefs", prefs);
    }
}
