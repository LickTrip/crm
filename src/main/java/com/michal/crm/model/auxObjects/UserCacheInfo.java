package com.michal.crm.model.auxObjects;

import com.michal.crm.model.Files;

public class UserCacheInfo {
    private String firstName;
    private String surname;
    private String description;
    private Files image;

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getDescription() {
        return description;
    }

    public Files getImage() {
        return image;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(Files image) {
        this.image = image;
    }

    public UserCacheInfo() {
    }

    public UserCacheInfo(String firstName, String surname, String description, Files image) {
        this.firstName = firstName;
        this.surname = surname;
        this.description = description;
        this.image = image;
    }
}
