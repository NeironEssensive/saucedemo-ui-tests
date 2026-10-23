package ru.qa.saucedemo.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.data.Product;

import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.exactTextsCaseSensitiveInAnyOrder;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CartPage {

    private final SelenideElement title = $(".title");
    private final ElementsCollection itemNames = $$(".cart_item .inventory_item_name");
    private final SelenideElement checkoutButton = $("#checkout");

    @Step("Проверить, что открыта корзина")
    public CartPage shouldBeOpened() {
        title.shouldHave(exactText("Your Cart"));
        return this;
    }

    @Step("Проверить, что в корзине ровно: {products}")
    public CartPage shouldContainExactly(Product... products) {
        String[] titles = Arrays.stream(products).map(Product::title).toArray(String[]::new);
        itemNames.shouldHave(exactTextsCaseSensitiveInAnyOrder(titles));
        return this;
    }

    @Step("Убрать из корзины «{product}»")
    public CartPage remove(Product product) {
        itemNames.findBy(exactText(product.title())).closest(".cart_item")
                .$("button").shouldHave(exactText("Remove")).click();
        return this;
    }

    @Step("Перейти к оформлению заказа")
    public CheckoutInfoPage checkout() {
        checkoutButton.click();
        return new CheckoutInfoPage().shouldBeOpened();
    }
}
