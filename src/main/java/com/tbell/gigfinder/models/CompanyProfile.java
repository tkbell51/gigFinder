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


    @Column(name = "company_cover_pic")
    private String companyCoverPic;

    @Column(name = "company_prof_pic")
    private String companyProfPic;

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

    public String getCompanyLocation() {
        return companyLocation;
    }

    public void setCompanyLocation(String companyLocation) {
        this.companyLocation = companyLocation;
    }

    @PrePersist
    void preImage(){
        if(this.companyProfPic == null){
            this.companyProfPic = "/assets/images/empty-profile-pic.jpg";
        }
    }
}
