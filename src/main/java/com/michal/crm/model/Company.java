package com.michal.crm.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue
    @Column(name = "company_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Size(min = 2, max = 30)
    @Column(name = "name")
    private String name;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Addresses address;

    @NotNull
    @Column(name = "email")
    @Size(min = 3, max = 50)
    private String email;

    @Column(name = "telNumber")
    private int telNumber;

    @OneToOne
    @JoinColumn(name = "file_id")
    private Files image;

    public Integer getId() {
        return id;
    }

    public Users getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public Addresses getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public int getTelNumber() {
        return telNumber;
    }

    public Files getImage() {
        return image;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(Addresses address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTelNumber(int telNumber) {
        this.telNumber = telNumber;
    }

    public void setImage(Files image) {
        this.image = image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Company() {
    }

    public Company(Users user, String name, Addresses address, String email, int telNumber, Files image) {
        this.user = user;
        this.name = name;
        this.address = address;
        this.email = email;
        this.telNumber = telNumber;
        this.image = image;
    }
}
