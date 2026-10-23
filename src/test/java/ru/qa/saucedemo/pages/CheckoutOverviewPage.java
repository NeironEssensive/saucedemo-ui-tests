package ru.qa.saucedemo.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.data.Product;
import ru.qa.saucedemo.util.Prices;

import java.math.BigDecimal;
import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.exactTextsCaseSensitiveInAnyOrder;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

/**
 * Шаг 2 оформления заказа: список товаров и суммы.
 */
public class CheckoutOverviewPage {

    private final SelenideElement title = $(".title");
    private final ElementsCollection itemNames = $$(".cart_item .inventory_item_name");
    private final SelenideElement itemTotalLabel = $(".summary_subtotal_label");
    private final SelenideElement taxLabel = $(".summary_tax_label");
    private final SelenideElement totalLabel = $(".summary_total_label");
    private final SelenideElement finishButton = $("#finish");

    @Step("Проверить, что открыта страница подтверждения заказа")
    public CheckoutOverviewPage shouldBeOpened() {
        title.shouldHave(exactText("Checkout: Overview"));
        return this;
    }

    @Step("Проверить, что в заказе ровно: {products}")
    public CheckoutOverviewPage shouldContainExactly(Product... products) {
        String[] titles = Arrays.stream(products).map(Product::title).toArray(String[]::new);
        itemNames.shouldHave(exactTextsCaseSensitiveInAnyOrder(titles));
        return this;
    }

    /** Сумма товаров без налога ("Item total: $39.98"). */
    public BigDecimal itemTotal() {
        return amount(itemTotalLabel);
    }

    /** Налог ("Tax: $3.20"). */
    public BigDecimal tax() {
        return amount(taxLabel);
    }

    /** Итог к оплате ("Total: $43.18"). */
    public BigDecimal total() {
        return amount(totalLabel);
    }

    @Step("Подтвердить заказ")
    public CheckoutCompletePage finish() {
        finishButton.click();
        return new CheckoutCompletePage().shouldBeOpened();
    }

    private BigDecimal amount(SelenideElement label) {
        return Prices.parse(label.shouldBe(visible).text());
    }
}
