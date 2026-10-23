package ru.qa.saucedemo.pages;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import ru.qa.saucedemo.data.Product;
import ru.qa.saucedemo.data.SortOrder;
import ru.qa.saucedemo.pages.components.HeaderComponent;
import ru.qa.saucedemo.util.Prices;

import java.math.BigDecimal;
import java.util.List;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class InventoryPage {

    private final SelenideElement title = $(".title");
    private final ElementsCollection items = $$(".inventory_item");
    private final ElementsCollection itemNames = $$(".inventory_item_name");
    private final ElementsCollection itemPrices = $$(".inventory_item_price");
    private final SelenideElement sortSelect = $(".product_sort_container");
    private final HeaderComponent header = new HeaderComponent();

    @Step("Проверить, что открыт каталог товаров")
    public InventoryPage shouldBeOpened() {
        title.shouldHave(exactText("Products"));
        return this;
    }

    public HeaderComponent header() {
        return header;
    }

    @Step("Добавить в корзину «{product}»")
    public InventoryPage add(Product product) {
        card(product).$("button").shouldHave(exactText("Add to cart")).click();
        return this;
    }

    @Step("Убрать из корзины «{product}»")
    public InventoryPage remove(Product product) {
        card(product).$("button").shouldHave(exactText("Remove")).click();
        return this;
    }

    @Step("Отсортировать товары {order}")
    public InventoryPage sortBy(SortOrder order) {
        sortSelect.selectOptionByValue(order.value());
        return this;
    }

    @Step("Проверить, что в каталоге {count} товаров")
    public InventoryPage shouldHaveProductCount(int count) {
        items.shouldHave(size(count));
        return this;
    }

    public List<String> productNames() {
        return itemNames.shouldHave(sizeGreaterThan(0)).texts();
    }

    public List<BigDecimal> productPrices() {
        return itemPrices.shouldHave(sizeGreaterThan(0)).texts().stream()
                .map(Prices::parse)
                .toList();
    }

    /**
     * Карточка ищется по названию товара, а не по позиции: тест не зависит от порядка товаров на странице.
     */
    private SelenideElement card(Product product) {
        return itemNames.findBy(exactText(product.title())).closest(".inventory_item");
    }
}
