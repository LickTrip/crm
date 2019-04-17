package com.michal.crm.model.types;

public enum ResultTypes {
    OK(0),
    PASS_NOT_MATCH(1),
    WRONG_PASS(2),
    NO_AGREE_TERMS(3);

    private int number;

    public int getNumber() {
        return number;
    }

    ResultTypes(int number) {
        this.number = number;
    }
}
