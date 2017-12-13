package com.tbell.gigfinder.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "company_profile")
public class CompanyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Company Name may not be empty")
    @Column(name = "company_name")
    private String companyName;

    @NotEmpty(message = "First Name may not be empty")
    @Column(name = "contact_first_name")
    private String companyContactFirstName;

    @NotEmpty(message = "Last Name may not be empty")
    @Column(name = "contact_last_name")
    private String companyContactLastName;

    @Column(name = "company_cover_pic")
    private String companyCoverPic;

    @Column(name = "company_prof_pic")
    private String companyProfPic;

    @NotEmpty(message = "Company City may not be empty")
    private String companyCity;

    @NotEmpty(message = "Company State may not be empty")
    private String companyState;

    @Column(name = "company_location")
    private String companyLocation;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL)
    private List<Gig> gigs;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    public CompanyProfile() {}


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

    public String getCompanyCoverPic() {
        return companyCoverPic;
    }

    public void setCompanyCoverPic(String companyPic) {
        this.companyCoverPic = companyPic;
    }

    public String getCompanyProfPic() {
        return companyProfPic;
    }

    public void setCompanyProfPic(String companyProfPic) {
        this.companyProfPic = companyProfPic;
    }

    public String getCompanyCity() {
        return companyCity;
    }

    public void setCompanyCity(String companyCity) {
        this.companyCity = companyCity;
    }

    public String getCompanyState() {
        return companyState;
    }

    public void setCompanyState(String companyState) {
        this.companyState = companyState;
    }

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    @Override
    public String toString() {
        return "CompanyProfile{" +
                "Company Name='" + companyName + '\'' +
                ", Company Contact First Name='" + companyContactFirstName + '\'' +
                ", Company Contact Last Name='" + companyContactLastName + '\'' +
                ", Company Location='" + companyLocation + '\'' +
                '}';
    }

    @PrePersist
    void preImage(){
        if(this.companyProfPic == null){
            this.companyProfPic = "empty-profile-pic.jpg";
        }
    }
}
