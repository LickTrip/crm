package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.Users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "task_contacts")
public class TaskContacts {

    @Id
    @GeneratedValue
    @Column(name = "task_contact_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "task_id")
    private Tasks task;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_id")
   // @Column(name = "contact")
    private Contacts contact;

    //region getters
    public Integer getId() {
        return id;
    }

    public Tasks getTask() {
        return task;
    }

    public Contacts getContact() {
        return contact;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setTask(Tasks task) {
        this.task = task;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion

    public TaskContacts(){}

    public TaskContacts(Tasks task, Contacts contact, Users user) {
        this.user = user;
        this.task = task;
        this.contact = contact;
    }
}
