package com.tbell.gigfinder.models;

import sun.util.calendar.CalendarDate;

import javax.persistence.*;

@Entity
@Table(name = "user_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String message;
    private CalendarDate messageDate;


    public Comment() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public CalendarDate getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(CalendarDate messageDate) {
        this.messageDate = messageDate;
    }
}
