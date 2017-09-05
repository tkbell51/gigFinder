package com.tbell.gigfinder.models;



import org.springframework.data.geo.Point;

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
    private String gigStart;

    @Column(name = "gig_end")
    private String gigEnd;

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

    public String getGigStart() {
        return gigStart;
    }

    public void setGigStart(String gigStart) {
        this.gigStart = gigStart;
    }

    public String getGigEnd() {
        return gigEnd;
    }

    public void setGigEnd(String gigEnd) {
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
