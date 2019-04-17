package com.michal.crm.model;

import com.michal.crm.model.types.AcademicDegreeTypes;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "users")
public class Users implements UserDetails{

    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer id;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "first_name")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "surname")
    private String surname;

    @NotNull
    @Size(min = 2, max = 25)
    @Column(name = "username")
    private String username;

    @ManyToMany(cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    @Column(name = "role")
    @JoinTable(name = "role", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
    private List<Role> roles;

    @NotNull
    @Size(min = 5, max = 70)
    @Column(name = "password")
    private String password;

    @OneToOne
    @JoinColumn(name = "email_conf_id")
    private UserEmailConfig emailConfig;

    @OneToOne
    @JoinColumn(name = "ftp_conf_id")
    private UserFtpConfig ftpConfig;

    @Column(name = "tel_number")
    private int telNumber;

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

    @OneToOne
    @JoinColumn(name = "file_id")
    private Files image;

    //region getters

    public Integer getId() {
        return id;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserEmailConfig getEmailConfig() {
        return emailConfig;
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

    public UserFtpConfig getFtpConfig() {
        return ftpConfig;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public int getTelNumber() {
        return telNumber;
    }

    public List<Role> getRoles() {
        return roles;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

//endregion

    //region setters
    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setEmailConfig(UserEmailConfig emailConfig) {
        this.emailConfig = emailConfig;
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

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTelNumber(int telNumber) {
        this.telNumber = telNumber;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setFtpConfig(UserFtpConfig ftpConfig) {
        this.ftpConfig = ftpConfig;
    }

//endregion


    public Users() {}

    public Users(String firstName, String surname, String username, String password, UserEmailConfig emailConfig, UserFtpConfig ftpConfig, int telNumber, AcademicDegreeTypes academicDegree, String company, Addresses address, String workPosition, Date bornDate) {
        this.firstName = firstName;
        this.emailConfig = emailConfig;
        this.ftpConfig = ftpConfig;
        this.surname = surname;
        this.academicDegree = academicDegree;
        this.company = company;
        this.address = address;
        this.workPosition = workPosition;
        this.bornDate = bornDate;
        this.username = username;
        this.telNumber = telNumber;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }
}
