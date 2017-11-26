package com.tbell.gigfinder.models;



import javax.persistence.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "user_musician")
public class MusicianProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;


    @Column(name = "musician_instruments")
    private String musicianInstruments;

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

    public MusicianProfile(User user, String firstName, String lastName,  String musicianInstruments, String location, String bio) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.musicianInstruments = musicianInstruments;
        this.location = location;
        this.bio = bio;

    }

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
        this.musicianInstruments = musicianInstruments;
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

    @PrePersist
    void preImage(){
        if(this.profPicImage == null){
            this.profPicImage = "/assets/images/empty-profile-pic.jpg";
        }
    }
}
