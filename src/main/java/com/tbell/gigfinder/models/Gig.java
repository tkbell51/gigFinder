package com.tbell.gigfinder.models;





import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;
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

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "gig_start")
    private Date gigStart;

    @Column(name = "gig_time_start")
    private String timeStart;

    @Column(name = "gig_end")
    private Date gigEnd;

    @Column(name = "gig_time_end")
    private String timeEnd;

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
            this.gigArt = "/assets/images/no-image.gif";
        }
    }
}
