package com.michal.crm.model.types;

public enum StorageType {
    DOC("\\doc"),
    IMG("\\img"),
    EMAIL("\\mail");

    private String path;

    public String getPath() {
        return path;
    }

    StorageType(String path) {
        this.path = path;
    }
}
