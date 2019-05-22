package com.michal.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(name = "company_history")
public class CompanyHistory {
    @Id
    @GeneratedValue
    @Column(name = "company_history_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "create_date")
    private Date createDate;

    public Integer getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public Users getUser() {
        return user;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public CompanyHistory() {
        this.createDate = new Date();
    }

    public CompanyHistory(Company company, Users user, Date createDate) {
        this.company = company;
        this.user = user;
        this.createDate = createDate;
    }
}
