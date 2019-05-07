package com.michal.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "meetings")
public class Meetings {

    @Id
    @GeneratedValue
    @Column(name = "meeting_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "meeting_name")
    private String name;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "create_date")
    private Date createDate;

    @NotNull
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "term")
    private Date term;

    @Column(name = "note")
    private String note;

    @NotNull
    @Size(min = 1, max = 40)
    @Column(name = "place")
    private String place;

    @NotNull
    @Column(name = "complete")
    private boolean complete;

    //region getters
    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getTerm() {
        return term;
    }

    public String getNote() {
        return note;
    }

    public String getPlace() {
        return place;
    }

    public boolean isComplete() {
        return complete;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setTerm(Date term) {
        this.term = term;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public void setComplete(boolean complete) {
        this.complete = complete;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion

    public Meetings(){
        this.createDate = new Date();
        this.complete = false;
    }

    public Meetings(Users user, String name, Date createDate, Date term, String note, String place, boolean complete) {
        this.user = user;
        this.name = name;
        this.createDate = createDate;
        this.term = term;
        this.note = note;
        this.place = place;
        this.complete = complete;
    }
}
