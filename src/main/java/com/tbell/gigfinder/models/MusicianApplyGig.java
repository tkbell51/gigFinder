package com.tbell.gigfinder.models;


import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "musician_apply_gig")
public class MusicianApplyGig implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne
    @JoinColumn(name = "company_gig_id")
    private Gig gig;


    @ManyToOne
    @JoinColumn(name = "user_musician_id")
    private MusicianProfile musicianProfile;

    @Column(name = "date_applied")
    private LocalDate dateApplied;

    public MusicianApplyGig() {}

    public MusicianApplyGig(Gig gig, MusicianProfile musicianProfile, LocalDate dateApplied) {
        this.gig = gig;
        this.musicianProfile = musicianProfile;
        this.dateApplied = dateApplied;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Gig getGig() {
        return gig;
    }

    public void setGig(Gig gig) {
        this.gig = gig;
    }

    public MusicianProfile getMusicianProfile() {
        return musicianProfile;
    }

    public void setMusicianProfile(MusicianProfile musicianProfile) {
        this.musicianProfile = musicianProfile;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }
}
