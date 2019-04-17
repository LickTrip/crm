package com.michal.crm.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.mail.Address;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

public class Email {
    private  int msgNumber;
    private Address[] sendToA;
    private List<String> sendToNames;
    private List<String> sendToEmails;
    private String subject;
    private String text;
    private String preview;
    private Date sendDate;

    @Column(name = "sendToA")
    public Address[] getSendToA() {
        return sendToA;
    }

    @Column(name = "sendToNames")
    public List<String> getSendToNames() {
        return sendToNames;
    }

    @Column(name = "sendToEmails")
    public List<String> getSendToEmails() {
        return sendToEmails;
    }

    @Column(name = "preview")
    public String getPreview() {
        return preview;
    }

    @Column(name = "msgNumber")
    public int getMsgNumber() {
        return msgNumber;
    }

    @Column(name = "endDate")
    @Temporal(TemporalType.DATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    public Date getSendDate() {
        return sendDate;
    }

    @Column(name = "subject")
    public String getSubject() {
        return subject;
    }

    @Column(name = "text")
    public String getText() {
        return text;
    }

    public void setSendToA(Address[] sendToA) {
        this.sendToA = sendToA;
    }

    public void setSendToNames(List<String> sendToNames) {
        this.sendToNames = sendToNames;
    }

    public void setSendToEmails(List<String> sendToEmails) {
        this.sendToEmails = sendToEmails;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public void setMsgNumber(int msgNumber) {
        this.msgNumber = msgNumber;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Email(){}

    public Email(Address[] sendToA, String subject, String text) {
        this.sendToA = sendToA;
        this.subject = subject;
        this.text = text;
    }
}
