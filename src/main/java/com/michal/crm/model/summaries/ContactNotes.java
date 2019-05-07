package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Users;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "contact_notes")
public class ContactNotes {

    @Id
    @GeneratedValue
    @Column(name = "contact_notes_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Column(name = "text")
    private String text;

    @NotNull
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "create_date")
    private Date createDate;

    //region getters
    public Integer getId() {
        return id;
    }

    public Contacts getContact() {
        return contact;
    }

    public String getText() {
        return text;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion


    public void setId(Integer id) {
        this.id = id;
    }

    public ContactNotes() {
        this.createDate = new Date();
    }

    public ContactNotes(Contacts contact, String text, Date createDate, Users user) {
        this.user = user;
        this.contact = contact;
        this.text = text;
        this.createDate = createDate;
    }
}
