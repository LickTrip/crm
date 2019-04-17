package com.michal.crm.model;

import com.michal.crm.model.types.EmailProtocolTypes;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user_email_config")
public class UserEmailConfig {

    @Id
    @GeneratedValue
    @Column(name = "email_conf_id")
    private Integer id;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "email")
    private String email;

    @Size(min = 5, max = 70)
    @Column(name = "email_pass")
    private String emailPassword;

    @Column(name = "email_host")
    private String emailHost;

    @Column(name = "email_port")
    private int emailPort;

    @Enumerated(EnumType.STRING)
    @Column(name = "protocol_type")
    private EmailProtocolTypes protocolType;

    public Integer getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getEmailPassword() {
        return emailPassword;
    }

    public String getEmailHost() {
        return emailHost;
    }

    public int getEmailPort() {
        return emailPort;
    }

    public EmailProtocolTypes getProtocolType() {
        return protocolType;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setEmailPassword(String emailPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.emailPassword = passwordEncoder.encode(emailPassword);
    }

    public void setEmailHost(String emailHost) {
        this.emailHost = emailHost;
    }

    public void setEmailPort(int emailPort) {
        this.emailPort = emailPort;
    }

    public void setProtocolType(EmailProtocolTypes protocolType) {
        this.protocolType = protocolType;
    }

    public UserEmailConfig() {
    }

    public UserEmailConfig(String email, String emailPassword, String emailHost, int emailPort, EmailProtocolTypes protocolType) {
        this.email = email;
        this.emailHost = emailHost;
        this.emailPort = emailPort;
        this.protocolType = protocolType;

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.emailPassword = passwordEncoder.encode(emailPassword);
    }
}
