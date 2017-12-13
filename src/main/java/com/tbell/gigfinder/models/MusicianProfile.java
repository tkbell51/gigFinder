package com.tbell.gigfinder.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_musician")
public class MusicianProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotEmpty(message = "Name must not be empty")
    @Column(name = "first_name")
    private String firstName;

    @NotEmpty(message = "Name must not be empty")
    @Column(name = "last_name")
    private String lastName;

    @NotEmpty(message = "Instruments must not be empty")
    @Column(name = "musician_instruments")
    private String musicianInstruments;

    @NotEmpty
    private String musicianCity;

    @NotEmpty
    private String musicianState;

    @Column(name = "musician_location")
    private String location;

    @Column(name = "musician_bio")
    private String bio;

    @Column(name = "musician_cover_pic")
    private String coverPicImage;

    @Column(name = "musician_prof_pic")
    private String profPicImage;

    @OneToMany(mappedBy = "musicianProfile", cascade = CascadeType.ALL)
    private List<MediaContent> mediaContents;

    @OneToOne
    @JoinColumn(name = "user_data_id")
    private User user;

    @OneToMany(mappedBy = "musicianProfile")
    private Set<MusicianApplyGig> musicianAppyGigs;


    public MusicianProfile() {}


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getMusicianInstruments() {
        return musicianInstruments;
    }

    public void setMusicianInstruments(String musicianInstruments) {
        this.musicianInstruments = musicianInstruments.replaceAll("[,.!?;:]", "$0 ").replaceAll("\\s+", " ");
    }

    public String getMusicianCity() {
        return musicianCity;
    }

    public void setMusicianCity(String musicianCity) {
        this.musicianCity = musicianCity;
    }

    public String getMusicianState() {
        return musicianState;
    }

    public void setMusicianState(String musicianState) {
        this.musicianState = musicianState;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<MediaContent> getMediaContents() {
        return mediaContents;
    }

    public void setMediaContents(List<MediaContent> mediaContents) {
        this.mediaContents = mediaContents;
    }

    public String getCoverPicImage() {
        return coverPicImage;
    }

    public void setCoverPicImage(String coverPicImage) {
        this.coverPicImage = coverPicImage;
    }

    public String getProfPicImage() {
        return profPicImage;
    }

    public void setProfPicImage(String profPicImage) {
        this.profPicImage = profPicImage;
    }

    public Set<MusicianApplyGig> getMusicianAppyGigs() {
        return musicianAppyGigs;
    }

    public void setMusicianAppyGigs(Set<MusicianApplyGig> musicianAppyGigs) {
        this.musicianAppyGigs = musicianAppyGigs;
    }

    @Override
    public String toString() {
        return "MusicianProfile{" +
                "First Name='" + firstName + '\'' +
                ", Last Name='" + lastName + '\'' +
                ", Musical Instruments='" + musicianInstruments + '\'' +
                ", Location='" + location + '\'' +
                ", Bio='" + bio + '\'' +
                '}';
    }

    @PrePersist
    void preImage(){
        if(this.profPicImage == null){
            this.profPicImage = "empty-profile-pic.jpg";
        }
    }
}
