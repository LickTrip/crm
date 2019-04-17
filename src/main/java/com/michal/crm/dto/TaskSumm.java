package com.michal.crm.dto;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Tasks;

import javax.persistence.Column;
import java.util.List;

public class TaskSumm {

    @Column(name = "task")
    private Tasks task;

    @Column(name = "contacts")
    private List<Contacts> contacts;

    @Column(name = "preview")
    private String preview;

    public Tasks getTask() {
        return task;
    }

    public List<Contacts> getContacts() {
        return contacts;
    }

    public String getPreview() {
        return preview;
    }

    public void setTask(Tasks task) {
        this.task = task;
    }

    public void setContacts(List<Contacts> contacts) {
        this.contacts = contacts;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public TaskSumm(Tasks task, List<Contacts> contacts, String preview) {
        this.task = task;
        this.contacts = contacts;
        this.preview = preview;
    }

    public TaskSumm() {
    }
}
