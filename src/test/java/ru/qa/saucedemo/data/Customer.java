package ru.qa.saucedemo.data;

/**
 * Данные покупателя для формы оформления заказа.
 */
public record Customer(String firstName, String lastName, String postalCode) {

    public static Customer valid() {
        return new Customer("Ivan", "Petrov", "101000");
    }

    public Customer withFirstName(String firstName) {
        return new Customer(firstName, lastName, postalCode);
    }

    public Customer withLastName(String lastName) {
        return new Customer(firstName, lastName, postalCode);
    }

    public Customer withPostalCode(String postalCode) {
        return new Customer(firstName, lastName, postalCode);
    }
}
