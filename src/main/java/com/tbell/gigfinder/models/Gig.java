package com.tbell.gigfinder.models;



import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "company_gig")
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gig_location")
    private String gigLocation;

    @Column(name = "gig_type")
    private String gigType;

    @Column(name = "gig_start")
    private Calendar gigStart;

    @Column(name = "gig_end")
    private Calendar gigEnd;

    @Column(name = "gig_description")
    private String gigDescription;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;

    public Gig() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getGigLocation() {
        return gigLocation;
    }

    public void setGigLocation(String gigLocation) {
        this.gigLocation = gigLocation;
    }


    public String getGigType() {
        return gigType;
    }

    public void setGigType(String gigType) {
        this.gigType = gigType;
    }

    public Calendar getGigStart() {
        return gigStart;
    }

    public void setGigStart(Calendar gigStart) {
        this.gigStart = gigStart;
    }

    public Calendar getGigEnd() {
        return gigEnd;
    }

    public void setGigEnd(Calendar gigEnd) {
        this.gigEnd = gigEnd;
    }

    public String getGigDescription() {
        return gigDescription;
    }

    public void setGigDescription(String gigDescription) {
        this.gigDescription = gigDescription;
    }

    public CompanyProfile getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
    }
}
