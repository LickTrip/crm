package com.michal.crm.dto;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;

import javax.persistence.Column;
import java.util.List;

public class MeetingSumm {

    @Column(name = "meeting")
    private Meetings meeting;

    @Column(name = "contacts")
    private List<Contacts> contacts;

    @Column(name = "preview")
    private String preview;

    public Meetings getMeeting() {
        return meeting;
    }

    public List<Contacts> getContacts() {
        return contacts;
    }

    public String getPreview() {
        return preview;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public void setContacts(List<Contacts> contacts) {
        this.contacts = contacts;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public MeetingSumm() {
    }

    public MeetingSumm(Meetings meeting, List<Contacts> contacts, String preview) {
        this.meeting = meeting;
        this.contacts = contacts;
        this.preview = preview;
    }
}
