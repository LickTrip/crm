package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;

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
    //region getters

    //endregion
    //region setters
    public void setFile(Files file) {
        this.file = file;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }
    //endregion


    public ContactFiles() {
    }

    public ContactFiles(Files file, Contacts contact) {
        this.file = file;
        this.contact = contact;
    }
}
