package com.michal.crm.model.summaries;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Users;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "meeting_contacts")
public class MeetingContacts {

    @Id
    @GeneratedValue
    @Column(name = "meeting_contact_id")
    private Integer id;

    @NotNull
    @OneToOne()
    @JoinColumn(name = "meeting_id")
    private Meetings meeting;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "contact_id")
    private Contacts contact;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    //region getters
    public Integer getId() {
        return id;
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public Contacts getContact() {
        return contact;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public void setContact(Contacts contact) {
        this.contact = contact;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion


    public MeetingContacts() {
    }

    public MeetingContacts(Meetings meeting, Contacts contact, Users user) {
        this.user = user;
        this.meeting = meeting;
        this.contact = contact;
    }
}
