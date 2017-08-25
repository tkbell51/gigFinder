package com.tbell.gigfinder.models;

import sun.util.calendar.CalendarDate;

import javax.persistence.*;

@Entity
@Table(name = "user_gig")
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String gigLocation;
    private String gigType;
    private CalendarDate gigStart;
    private CalendarDate gigEnd;
    private String gigDescription;

    public Gig() {}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public CalendarDate getGigStart() {
        return gigStart;
    }

    public void setGigStart(CalendarDate gigStart) {
        this.gigStart = gigStart;
    }

    public CalendarDate getGigEnd() {
        return gigEnd;
    }

    public void setGigEnd(CalendarDate gigEnd) {
        this.gigEnd = gigEnd;
    }

    public String getGigDescription() {
        return gigDescription;
    }

    public void setGigDescription(String gigDescription) {
        this.gigDescription = gigDescription;
    }
}
