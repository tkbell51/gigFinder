package com.tbell.gigfinder.models;


import javax.persistence.*;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "media_content")
public class MediaContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "media_url")
    private String media_url;

    @Temporal(TemporalType.TIMESTAMP)
    private Date addedDate;

    private String title;

    @ManyToOne
    @JoinColumn(name = "user_musician_id")
    private MusicianProfile musicianProfile;

    public MediaContent() {}

    public MediaContent(String media_url, Date addedDate, String title) {
        this.media_url = media_url;
        this.addedDate = addedDate;
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public Date getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(Date addedDate) {
        this.addedDate = addedDate;
    }

    public MusicianProfile getMusicianProfile() {
        return musicianProfile;
    }

    public void setMusicianProfile(MusicianProfile musicianProfile) {
        this.musicianProfile = musicianProfile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
