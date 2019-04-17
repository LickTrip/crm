package com.michal.crm.model.auxObjects;

public class SearchBox {
    private String name;

    public SearchBox(String name) {
        this.name = name;
    }
    public SearchBox() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
