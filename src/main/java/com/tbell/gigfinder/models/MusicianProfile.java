package com.tbell.gigfinder.models;



import javax.persistence.*;
import java.util.Calendar;
import java.util.List;

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

    @Column(name = "musician_email")
    private String musicianEmail;

    @Column(name = "musician_phone_number")
    private String musicianPhoneNumber;

    @Column(name = "birth_date")
    private Calendar birthDate;

    @Column(name = "musician_instruments")
    private String musicianInstruments;

    @Column(name = "musician_location")
    private String location;

    @Column(name = "musician_bio")
    private String bio;

    @Column(name = "musician_pic")
    private String picImage;

    @OneToMany(mappedBy = "musicianProfile", cascade = CascadeType.ALL)
    private List<MediaContent> mediaContents;

    @OneToOne
    @JoinColumn(name = "user_data_id")
    private User user;


    public MusicianProfile() {}

    public MusicianProfile(User user, String firstName, String lastName, String musicianEmail, String musicianPhoneNumber, Calendar birthDate, String musicianInstruments, String location, String bio) {
        this.user = user;
        this.firstName = firstName;
        this.lastName = lastName;
        this.musicianEmail = musicianEmail;
        this.musicianPhoneNumber = musicianPhoneNumber;
        this.birthDate = birthDate;
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

    public String getMusicianEmail() {
        return musicianEmail;
    }

    public void setMusicianEmail(String musicianEmail) {
        this.musicianEmail = musicianEmail;
    }

    public String getMusicianPhoneNumber() {
        return musicianPhoneNumber;
    }

    public void setMusicianPhoneNumber(String musicianPhoneNumber) {
        this.musicianPhoneNumber = musicianPhoneNumber;
    }

    public Calendar getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Calendar birthDate) {
        this.birthDate = birthDate;
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

    public String getPicImage() {
        return picImage;
    }

    public void setPicImage(String picImage) {
        this.picImage = picImage;
    }
}
