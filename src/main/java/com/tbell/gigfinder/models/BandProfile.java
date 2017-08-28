package com.tbell.gigfinder.models;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "band_profile")
public class BandProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String bandName;
    private String bandContactFirstName;
    private String bandContactLastName;
    private String bandPhoneNumber;
    private String bandEmail;
    private String bandDescription;
    private String bandPic;
    private String bandLocation;

    @OneToMany(mappedBy = "companyProfile", cascade = CascadeType.ALL)
    private List<Gig> gigs;

    @OneToOne
    @JoinColumn(name = "user_data_id")
    private User user;

    public BandProfile() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBandName() {
        return bandName;
    }

    public void setBandName(String bandName) {
        this.bandName = bandName;
    }

    public String getBandContactFirstName() {
        return bandContactFirstName;
    }

    public void setBandContactFirstName(String bandContactFirstName) {
        this.bandContactFirstName = bandContactFirstName;
    }

    public String getBandContactLastName() {
        return bandContactLastName;
    }

    public void setBandContactLastName(String bandContactLastName) {
        this.bandContactLastName = bandContactLastName;
    }

    public String getBandPhoneNumber() {
        return bandPhoneNumber;
    }

    public void setBandPhoneNumber(String bandPhoneNumber) {
        this.bandPhoneNumber = bandPhoneNumber;
    }

    public String getBandEmail() {
        return bandEmail;
    }

    public void setBandEmail(String bandEmail) {
        this.bandEmail = bandEmail;
    }

    public String getBandDescription() {
        return bandDescription;
    }

    public void setBandDescription(String bandDescription) {
        this.bandDescription = bandDescription;
    }

    public String getBandPic() {
        return bandPic;
    }

    public void setBandPic(String bandPic) {
        this.bandPic = bandPic;
    }

    public String getBandLocation() {
        return bandLocation;
    }

    public void setBandLocation(String bandLocation) {
        this.bandLocation = bandLocation;
    }

    public List<Gig> getGigs() {
        return gigs;
    }

    public void setGigs(List<Gig> gigs) {
        this.gigs = gigs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
