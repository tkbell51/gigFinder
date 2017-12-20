package com.tbell.gigfinder.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalTime;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "company_gig")
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Title must not be empty")
    @Column(name = "gig_title")
    private String gigTitle;

    @NotEmpty(message = "Street Name may not be empty")
    private String gigStreet;

    @NotEmpty(message = "City may not be empty")
    private String gigCity;

    @NotEmpty(message = "State may not be empty")
    private String gigState;

    @NotEmpty(message = "Zip Code may not be empty")
    private String gigZip;

    @Column(name = "gig_location")
    private String gigLocation;

    @NotEmpty(message = "Type must not be empty")
    @Column(name = "gig_type")
    private String gigType;

    @NotNull(message = "Start Date must not be empty")
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gig_start")
    private Date gigStart;

    @NotEmpty(message = "Start Time must not be empty")
    @Column(name = "gig_time_start")
    private String timeStart;

    @NotNull(message = "End Date must not be empty")
    @Column(name = "gig_end")
    private Date gigEnd;

    @NotEmpty(message = "End Time must not be empty")
    @Column(name = "gig_time_end")
    private String timeEnd;

    @NotEmpty(message = "Description must not be empty")
    @Column(name = "gig_description")
    private String gigDescription;

    @Column(name = "gig_art")
    private String gigArt;

    @Column(name = "gig_hired")
    private Boolean gigHired;

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

    public String getGigStreet() {
        return gigStreet;
    }

    public void setGigStreet(String gigStreet) {
        this.gigStreet = gigStreet;
    }

    public String getGigCity() {
        return gigCity;
    }

    public void setGigCity(String gigCity) {
        this.gigCity = gigCity;
    }

    public String getGigState() {
        return gigState;
    }

    public void setGigState(String gigState) {
        this.gigState = gigState;
    }

    public String getGigZip() {
        return gigZip;
    }

    public void setGigZip(String gigZip) {
        this.gigZip = gigZip;
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
        this.gigArt = gigArt.replaceAll("\\s+","+");
    }


    public CompanyProfile getCompanyProfile() {
        return companyProfile;
    }

    public void setCompanyProfile(CompanyProfile companyProfile) {
        this.companyProfile = companyProfile;
    }

    public String getTimeStart() {
        return timeStart;
    }

    public void setTimeStart(String timeStart) {
        this.timeStart = timeStart;
    }

    public String getTimeEnd() {
        return timeEnd;
    }

    public void setTimeEnd(String timeEnd) {
        this.timeEnd = timeEnd;
    }

    public Boolean getGigHired() {
        return gigHired;
    }

    public void setGigHired(Boolean gigHired) {
        this.gigHired = gigHired;
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
            this.gigArt = "no-image-thumbnail.jpg";
        }
    }
}
