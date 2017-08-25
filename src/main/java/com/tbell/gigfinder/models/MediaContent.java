package com.tbell.gigfinder.models;

import sun.util.calendar.CalendarDate;

import javax.persistence.*;

@Entity
@Table(name = "mediaContent")
public class MediaContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String mediaURL;
    private CalendarDate addedDate;

    public MediaContent() {}

    public MediaContent(String mediaURL, CalendarDate addedDate) {
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

    public CalendarDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(CalendarDate addedDate) {
        this.addedDate = addedDate;
    }
}
