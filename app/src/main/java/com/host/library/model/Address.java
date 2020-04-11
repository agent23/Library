package com.host.library.model;

import java.io.Serializable;

public class Address implements Serializable {
    private String countryISO;
    private int postCode;
    private int streetNumber;
    private String streetName;

    public Address(String countryISO, int postCode, int streetNumber, String streetName) {
        this.countryISO = countryISO;
        this.postCode = postCode;
        this.streetNumber = streetNumber;
        this.streetName = streetName;
    }

    public String getCountryISO() {
        return countryISO;
    }

    public void setCountryISO(String countryISO) {
        this.countryISO = countryISO;
    }

    public int getPostCode() {
        return postCode;
    }

    public void setPostCode(int postCode) {
        this.postCode = postCode;
    }

    public int getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(int streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
