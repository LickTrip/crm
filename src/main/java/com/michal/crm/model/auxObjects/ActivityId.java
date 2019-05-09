package com.michal.crm.model.auxObjects;

public class ActivityId {
    private int id;
    private boolean isTask;
    private boolean isEdit;
    private int contId;

    public ActivityId(int id, boolean isTask) {
        this.id = id;
        this.isTask = isTask;
        this.contId = 0;
    }

    public ActivityId(int id, boolean isTask, int contId) {
        this.id = id;
        this.isTask = isTask;
        this.contId = contId;
    }

    public ActivityId(int id, boolean isTask, boolean isEdit) {
        this.id = id;
        this.isTask = isTask;
        this.isEdit = isEdit;
    }

    public ActivityId(boolean isTask){
        this.isTask = isTask;
    }

    public ActivityId() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isTask() {
        return isTask;
    }

    public void setTask(boolean task) {
        isTask = task;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public int getContId() {
        return contId;
    }

    public void setContId(int contId) {
        this.contId = contId;
    }
}
