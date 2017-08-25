package com.tbell.gigfinder.models;

import org.springframework.security.core.userdetails.UserDetails;
import sun.util.calendar.CalendarDate;

import javax.persistence.*;

@Entity
@Table(name = "user_gig")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String userEmail;
    private String userPhoneNumber;
    private CalendarDate signupDate;


//    Company Info


//    Musician Info

}
