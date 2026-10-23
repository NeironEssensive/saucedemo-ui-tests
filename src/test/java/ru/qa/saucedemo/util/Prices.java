package ru.qa.saucedemo.util;

import java.math.BigDecimal;

public final class Prices {

    private Prices() {
    }

    /**
     * Достаёт сумму из текста вида "$29.99" или "Item total: $39.98".
     */
    public static BigDecimal parse(String text) {
        int dollar = text.indexOf('$');
        if (dollar < 0) {
            throw new IllegalArgumentException("В тексте нет суммы в долларах: " + text);
        }
        return new BigDecimal(text.substring(dollar + 1).trim());
    }
}
