package com.michal.crm.model.summaries;

import com.michal.crm.model.Company;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "company_contacts")
public class CompanyContacts {
    @Id
    @GeneratedValue
    @Column(name = "company_contact_id")
    private Integer id;

    @NotNull
    @OneToOne()
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    public Integer getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public Contacts getContact() {
        return contact;
    }

    public Users getUser() {
        return user;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public CompanyContacts() {
    }

    public CompanyContacts(Company company, Contacts contact, Users user) {
        this.company = company;
        this.contact = contact;
        this.user = user;
    }
}
