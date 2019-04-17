package com.michal.crm.dto;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;

import java.util.ArrayList;
import java.util.List;

public class ActivityDto {
    private Meetings meeting;
    private Tasks task;
    private List<Contacts> contactsList;

    public ActivityDto(Meetings meeting, Tasks task) {
        this.meeting = meeting;
        this.task = task;
        this.contactsList = new ArrayList<>();
    }

    public ActivityDto(Meetings meeting, Tasks task, List<Contacts> contactsList) {
        this.meeting = meeting;
        this.task = task;
        this.contactsList = contactsList;
    }

    public ActivityDto() {
    }

    public Meetings getMeeting() {
        return meeting;
    }

    public Tasks getTask() {
        return task;
    }

    public List<Contacts> getContactsList() {
        return contactsList;
    }

    public void setMeeting(Meetings meeting) {
        this.meeting = meeting;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    public void setContactsList(List<Contacts> contactsList) {
        this.contactsList = contactsList;
    }
}
