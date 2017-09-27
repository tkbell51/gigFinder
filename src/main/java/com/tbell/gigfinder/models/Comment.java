package com.tbell.gigfinder.models;



import javax.persistence.*;
import java.util.Calendar;

@Entity
@Table(name = "user_comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String message;
    @Column(name = "message_date")
    private Calendar messageDate;

    @ManyToOne
    @JoinColumn(name = "user_data_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "company_gig_id")
    private Gig gig;
    
    
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

    public Calendar getMessageDate() {
        return messageDate;
    }

    public void setMessageDate(Calendar messageDate) {
        this.messageDate = messageDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Gig getGig() {
        return gig;
    }

    public void setGig(Gig gig) {
        this.gig = gig;
    }
}
