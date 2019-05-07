package com.michal.crm.model;

import com.michal.crm.model.types.AcademicDegreeTypes;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "contacts")
public class Contacts {

    @Id
    @GeneratedValue
    @Column(name = "contact_id")
    private Integer id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "surname")
    private String surname;

    @Enumerated(EnumType.STRING)
    @Column(name = "academic_degree")
    private AcademicDegreeTypes academicDegree;

    @Column(name = "company")
    private String company;

    @OneToOne
    @JoinColumn(name = "address_id")
    private Addresses address;

    @Column(name = "work_position")
    private String workPosition;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "born_date")
    private Date bornDate;

    @NotNull
    @Column(name = "email")
    @Size(min = 3, max = 50)
    private String email;

    @Column(name = "telNumber")
    private int telNumber;

    @OneToOne
    @JoinColumn(name = "file_id")
    private Files image;

    //region getters
    public Integer getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public AcademicDegreeTypes getAcademicDegree() {
        return academicDegree;
    }

    public String getCompany() {
        return company;
    }

    public Addresses getAddress() {
        return address;
    }

    public String getWorkPosition() {
        return workPosition;
    }

    public Date getBornDate() {
        return bornDate;
    }

    public Files getImage() {
        return image;
    }

    public int getTelNumber() {
        return telNumber;
    }

    public String getEmail() {
        return email;
    }

    public Users getUser() {
        return user;
    }

    //endregion

    //region setters
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAcademicDegree(AcademicDegreeTypes academicDegree) {
        this.academicDegree = academicDegree;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public void setAddress(Addresses address) {
        this.address = address;
    }

    public void setWorkPosition(String workPosition) {
        this.workPosition = workPosition;
    }

    public void setBornDate(Date bornDate) {
        this.bornDate = bornDate;
    }

    public void setImage(Files image) {
        this.image = image;
    }

    public void setTelNumber(int telNumber) {
        this.telNumber = telNumber;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    //endregion

    public Contacts() {
    }

    public Contacts(Users user, String firstName, String surname, AcademicDegreeTypes academicDegree, String company, Addresses address, String workPosition, String email, int telNumber, Date bornDate) {
        this.user = user;
        this.firstName = firstName;
        this.surname = surname;
        this.academicDegree = academicDegree;
        this.company = company;
        this.address = address;
        this.workPosition = workPosition;
        this.bornDate = bornDate;
        this.email = email;
        this.telNumber = telNumber;
    }
}
