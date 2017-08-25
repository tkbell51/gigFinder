package com.tbell.gigfinder.models;

import sun.util.calendar.CalendarDate;

import javax.persistence.*;

@Entity
@Table(name = "user_musician")
public class MusicianProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String firstName;
    private String lastName;
    private String musicianEmail;
    private String musicianPhoneNumber;
    private CalendarDate birthDate;
    private String musicianInstruments;
    private String location;
    private String bio;


    public MusicianProfile() {}

    public MusicianProfile(String firstName, String lastName, String musicianEmail, String musicianPhoneNumber, CalendarDate birthDate, String musicianInstruments, String location, String bio) {
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

    public CalendarDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(CalendarDate birthDate) {
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
}
