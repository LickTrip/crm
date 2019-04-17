package com.michal.crm.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_ftp_config")
public class UserFtpConfig {

    @Id
    @GeneratedValue
    @Column(name = "ftp_conf_id")
    private Integer id;

    @Size(min = 3, max = 50)
    @Column(name = "ftp_name")
    private String ftpName;

    @Size(min = 5, max = 70)
    @Column(name = "ftp_pass")
    private String ftpPassword;

    @Column(name = "ftp_host")
    private String ftpHost;

    @Column(name = "ftp_port")
    private int ftpPort;

    public Integer getId() {
        return id;
    }

    public String getFtpName() {
        return ftpName;
    }

    public String getFtpPassword() {
        return ftpPassword;
    }

    public String getFtpHost() {
        return ftpHost;
    }

    public int getFtpPort() {
        return ftpPort;
    }

    public void setFtpName(String ftpName) {
        this.ftpName = ftpName;
    }

    public void setFtpPassword(String ftpPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.ftpPassword = passwordEncoder.encode(ftpPassword);
    }

    public void setFtpHost(String ftpHost) {
        this.ftpHost = ftpHost;
    }

    public void setFtpPort(int ftpPort) {
        this.ftpPort = ftpPort;
    }

    public UserFtpConfig() {
    }

    public UserFtpConfig(String ftpName, String ftpPassword, String ftpHost, int ftpPort) {
        this.ftpName = ftpName;
        this.ftpHost = ftpHost;
        this.ftpPort = ftpPort;

//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        this.ftpPassword = passwordEncoder.encode(ftpPassword);
        this.ftpPassword = ftpPassword;
    }
}
