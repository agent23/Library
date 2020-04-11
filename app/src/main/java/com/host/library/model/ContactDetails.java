package com.host.library.model;

import java.io.Serializable;

public class ContactDetails implements Serializable {
    private String areaCode;
    private String phone;
    private String email;

    public ContactDetails(String areaCode, String phone, String email) {
        this.areaCode = areaCode;
        this.phone = phone;
        this.email = email;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
