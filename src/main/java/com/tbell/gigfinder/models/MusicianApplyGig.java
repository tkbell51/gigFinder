package com.tbell.gigfinder.models;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "musician_apply_gig")
public class MusicianApplyGig implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Id
    @ManyToMany
    @JoinColumn(name = "company_gig_id")
    private Gig gig;

    @Id
    @ManyToOne
    @JoinColumn(name = "user_musician_id")
    private MusicianProfile musicianProfile;

    @Column(name = "date_applied")
    private Date dateApplied;

    public MusicianApplyGig() {}

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

    public Date getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(Date dateApplied) {
        this.dateApplied = dateApplied;
    }
}
