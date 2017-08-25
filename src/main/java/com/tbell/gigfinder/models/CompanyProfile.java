package com.tbell.gigfinder.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "company_profile")
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "contact_first_name")
    private String companyContactFirstName;

    @Column(name = "contact_last_name")
    private String companyContactLastName;

    @Column(name = "company_phone_number")
    private String phoneNumber;

    @Column(name = "company_email")
    private String email;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL)
    private List<Gig> gigs;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CompanyProfile() {}

    public CompanyProfile(User user, String companyName, String companyContactFirstName, String companyContactLastName, String phoneNumber, String email) {
        this.user = user;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Gig> getGigs() {
        return gigs;
    }

    public void setGigs(List<Gig> gigs) {
        this.gigs = gigs;
    }
}
