package com.michal.crm.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "addresses")
public class Addresses {

    @Id
    @GeneratedValue
    @Column(name = "address_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Column(name = "state")
    private String state;

    @NotNull
    @Column(name = "city")
    private String city;

    @NotNull
    @Column(name = "street")
    private String street;

    @Column(name = "street_no")
    private int streetNo;

    @NotNull
    @Column(name = "zip")
    private int zip;

    //region getters
    public Integer getId() {
        return id;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public int getStreetNo() {
        return streetNo;
    }

    public int getZip() {
        return zip;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setState(String state) {
        this.state = state;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setStreetNo(int streetNo) {
        this.streetNo = streetNo;
    }

    public void setZip(int zip) {
        this.zip = zip;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion

    public Addresses(){}

    public Addresses(Users user, String state, String city, String street, int streetNo, int zip) {
        this.user = user;
        this.state = state;
        this.city = city;
        this.street = street;
        this.streetNo = streetNo;
        this.zip = zip;
    }
}
