package ru.qa.saucedemo.data;

/**
 * Варианты сортировки каталога: value — значение option в выпадающем списке.
 */
public enum SortOrder {

    NAME_A_TO_Z("az", "по имени (A → Z)"),
    NAME_Z_TO_A("za", "по имени (Z → A)"),
    PRICE_LOW_TO_HIGH("lohi", "по цене (по возрастанию)"),
    PRICE_HIGH_TO_LOW("hilo", "по цене (по убыванию)");

    private final String value;
    private final String label;

    SortOrder(String value, String label) {
        this.value = value;
        this.label = label;
    }

    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return label;
    }
}
