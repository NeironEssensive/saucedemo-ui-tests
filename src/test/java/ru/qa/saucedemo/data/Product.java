package ru.qa.saucedemo.data;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * Товары каталога. Цены храним в BigDecimal, чтобы сравнивать суммы без ошибок округления double.
 */
public enum Product {

    BACKPACK("Sauce Labs Backpack", "29.99"),
    BIKE_LIGHT("Sauce Labs Bike Light", "9.99"),
    BOLT_T_SHIRT("Sauce Labs Bolt T-Shirt", "15.99"),
    FLEECE_JACKET("Sauce Labs Fleece Jacket", "49.99"),
    ONESIE("Sauce Labs Onesie", "7.99"),
    RED_T_SHIRT("Test.allTheThings() T-Shirt (Red)", "15.99");

    private final String title;
    private final BigDecimal price;

    Product(String title, String price) {
        this.title = title;
        this.price = new BigDecimal(price);
    }

    public String title() {
        return title;
    }

    public BigDecimal price() {
        return price;
    }

    public static List<String> titles() {
        return Arrays.stream(values()).map(Product::title).toList();
    }

    /** Название товара — так он выглядит в шагах Allure и в именах параметризованных тестов. */
    @Override
    public String toString() {
        return title;
    }
}
