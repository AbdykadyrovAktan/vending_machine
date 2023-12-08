package model;

import enums.ActionLetter;

public class Product {
    protected String name;
    protected ActionLetter actionLetter;
    protected double price;

    public Product(String name, ActionLetter actionLetter, int price) {
        this.name = name;
        this.actionLetter = actionLetter;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public ActionLetter getActionLetter() {
        return actionLetter;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        String str = "[%s] - %s";
        return String.format(str, price, name);
    }
}
