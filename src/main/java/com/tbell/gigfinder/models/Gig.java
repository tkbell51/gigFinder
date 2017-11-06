package com.tbell.gigfinder.models;



import org.springframework.data.geo.Point;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "company_gig")
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "gig_title")
    private String gigTitle;

    @Column(name = "gig_location")
    private String gigLocation;


    @Column(name = "gig_type")
    private String gigType;

    @Column(name = "gig_start")
    private Date gigStart;

    @Column(name = "gig_end")
    private Date gigEnd;

    @Column(name = "gig_description")
    private String gigDescription;

    @Column(name = "gig_art")
    private String gigArt;

    @ManyToOne
    @JoinColumn(name = "company_profile_id")
    private CompanyProfile companyProfile;


    @OneToMany(mappedBy = "gig", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<MusicianApplyGig> musicianApplyGigs;

    public Gig() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGigTitle() {
        return gigTitle;
    }

    public void setGigTitle(String gigTitle) {
        this.gigTitle = gigTitle;
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

    public Date getGigStart() {
        return gigStart;
    }

    public void setGigStart(Date gigStart) {
        this.gigStart = gigStart;
    }

    public Date getGigEnd() {
        return gigEnd;
    }

    public void setGigEnd(Date gigEnd) {
        this.gigEnd = gigEnd;
    }

    public String getGigDescription() {
        return gigDescription;
    }

    public void setGigDescription(String gigDescription) {
        this.gigDescription = gigDescription;
    }

    public String getGigArt() {
        return gigArt;
    }

    public void setGigArt(String gigArt) {
        this.gigArt = gigArt;
    }


    public CompanyProfile getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
    }



    public Set<MusicianApplyGig> getMusicianApplyGigs() {
        return musicianApplyGigs;
    }

    public void setMusicianApplyGigs(Set<MusicianApplyGig> musicianApplyGigs) {
        this.musicianApplyGigs = musicianApplyGigs;
    }

    @PrePersist
    void preGigImage(){
        if(this.gigArt == null){
            this.gigArt = "/assets/images/no-image.gif";
        }
    }
}
