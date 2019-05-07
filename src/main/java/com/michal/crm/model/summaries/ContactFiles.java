package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;
import com.michal.crm.model.Users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contact_files")
public class ContactFiles {

    @Id
    @GeneratedValue
    @Column(name = "contact_file_id")
    private Integer id;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "file_id")
    private Files file;

    @NotNull
    @OneToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    //endregion
    public Integer getId() {
        return id;
    }

    public Files getFile() {
        return file;
    }

    public Contacts getContact() {
        return contact;
    }

    public Users getUser() {
        return user;
    }

    //region getters

    //endregion
    //region setters
    public void setFile(Files file) {
        this.file = file;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion


    public ContactFiles() {
    }

    public ContactFiles(Files file, Contacts contact, Users user) {
        this.user = user;
        this.file = file;
        this.contact = contact;
    }
}
