package com.michal.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "contact_history")
public class ContactHistory {

    @Id
    @GeneratedValue
    @Column(name = "contact_history_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "create_date")
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public Contacts getContact() {
        return contact;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Users getUser() {
        return user;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public ContactHistory() {
        this.createDate = new Date();
    }

    public ContactHistory(Contacts contact, Date createDate, Users user) {
        this.contact = contact;
        this.createDate = createDate;
        this.user = user;
    }
}
