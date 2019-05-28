package com.michal.crm.dto;

import com.michal.crm.model.Company;
import com.michal.crm.model.Contacts;

public class EmailDto {
    private int id;
    private boolean isCont;
    private Company company;
    private Contacts contacts;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isCont() {
        return isCont;
    }

    public void setCont(boolean cont) {
        isCont = cont;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Contacts getContacts() {
        return contacts;
    }

    public void setContacts(Contacts contacts) {
        this.contacts = contacts;
    }

    public EmailDto() {
    }

    public EmailDto(int id, boolean isCont, Company company, Contacts contacts) {
        this.id = id;
        this.isCont = isCont;
        this.company = company;
        this.contacts = contacts;
    }
}
