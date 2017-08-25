package com.tbell.gigfinder.models;

import javax.persistence.*;

@Entity
@Table(name = "company_profile")
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String companyName;
    private String companyContactFirstName;
    private String companyContactLastName;
    private String phoneNumber;
    private String email;

    public CompanyProfile() {}

    public CompanyProfile(String companyName, String companyContactFirstName, String companyContactLastName, String phoneNumber, String email) {
        this.companyName = companyName;
        this.companyContactFirstName = companyContactFirstName;
        this.companyContactLastName = companyContactLastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyContactFirstName() {
        return companyContactFirstName;
    }

    public void setCompanyContactFirstName(String companyContactFirstName) {
        this.companyContactFirstName = companyContactFirstName;
    }

    public String getCompanyContactLastName() {
        return companyContactLastName;
    }

    public void setCompanyContactLastName(String companyContactLastName) {
        this.companyContactLastName = companyContactLastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
