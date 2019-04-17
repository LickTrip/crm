package com.michal.crm.dto;

import com.michal.crm.model.Users;

public class ProfileDto {

    private Users user;
    private String passNew;
    private String passConf;
    private String passOld;
    private boolean agreeCond;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public String getPassConf() {
        return passConf;
    }

    public void setPassConf(String passConf) {
        this.passConf = passConf;
    }

    public String getPassOld() {
        return passOld;
    }

    public void setPassOld(String passOld) {
        this.passOld = passOld;
    }

    public String getPassNew() {
        return passNew;
    }

    public void setPassNew(String passNew) {
        this.passNew = passNew;
    }

    public boolean isAgreeCond() {
        return agreeCond;
    }

    public void setAgreeCond(boolean agreeCond) {
        this.agreeCond = agreeCond;
    }

    public ProfileDto(Users user, String passNew, String passConf, String passOld) {
        this.user = user;
        this.passNew = passNew;
        this.passOld = passOld;
        this.passConf = passConf;
    }

    public ProfileDto() {

    }
}
