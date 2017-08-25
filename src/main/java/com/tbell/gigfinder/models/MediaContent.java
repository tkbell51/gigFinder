package com.tbell.gigfinder.models;


import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "media_content")
public class MediaContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mediaURL;
    private Calendar addedDate;

    @ManyToOne
    @JoinColumn(name = "musician_profile_id")
    private MusicianProfile musicianProfile;

    public MediaContent() {}

    public MediaContent(String mediaURL, Calendar addedDate) {
        this.mediaURL = mediaURL;
        this.addedDate = addedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMediaURL() {
        return mediaURL;
    }

    public void setMediaURL(String mediaURL) {
        this.mediaURL = mediaURL;
    }

    public Calendar getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Calendar addedDate) {
        this.addedDate = addedDate;
    }

    public MusicianProfile getMusicianProfile() {
        return musicianProfile;
    }

    public void setMusicianProfile(MusicianProfile musicianProfile) {
        this.musicianProfile = musicianProfile;
    }
}
