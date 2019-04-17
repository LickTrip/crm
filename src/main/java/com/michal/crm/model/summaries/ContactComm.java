package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "contact_comm")
public class ContactComm {

    @Id
    @GeneratedValue
    @Column(name = "contact_comm_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @Size(max = 50)
    @Column(name = "email_description")
    private String emailDescription;

    @Size(min = 3, max = 30)
    @Column(name = "email")
    private String email;

    @Size(max = 50)
    @Column(name = "telephone_description")
    private String telDescription;

    @Column(name = "telephone")
    private int tel;

    //region getters
    public Integer getId() {
        return id;
    }

    public Contacts getContact() {
        return contact;
    }

    public String getEmail() {
        return email;
    }

    public int getTel() {
        return tel;
    }

    public String getEmailDescription() {
        return emailDescription;
    }

    public String getTelDescription() {
        return telDescription;
    }

//endregion

    //region setters
    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTel(int tel) {
        this.tel = tel;
    }

    public void setEmailDescription(String emailDescription) {
        this.emailDescription = emailDescription;
    }

    public void setTelDescription(String telDescription) {
        this.telDescription = telDescription;
    }

//endregion

    public ContactComm() {
    }

    public ContactComm(Contacts contact, String email, int tel, String emailDescription, String telDescription) {
        this.contact = contact;
        this.email = email;
        this.tel = tel;
        this.emailDescription = emailDescription;
        this.telDescription = telDescription;
    }
}
