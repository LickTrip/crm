package com.michal.crm.model.auxObjects;

public class ActivityId {
    private int id;
    private boolean isTask;
    private boolean isEdit;

    public ActivityId(int id, boolean isTask) {
        this.id = id;
        this.isTask = isTask;
    }

    public ActivityId(int id, boolean isTask, boolean isEdit) {
        this.id = id;
        this.isTask = isTask;
        this.isEdit = isEdit;
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
}
