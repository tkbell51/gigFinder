package com.tbell.gigfinder.services;

import com.tbell.gigfinder.Repositories.GigRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class NotificationService {



    private JavaMailSender javaMailSender;

    @Autowired
    public NotificationService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendDeclineNotification(MusicianProfile musicianProfile, Gig gig) throws MailException{
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, MMM d, ''yy");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(musicianProfile.getUser().getEmail());
        mail.setFrom("thegigfinder17@gmail.com");
        mail.setSubject("Declined " + gig.getGigTitle() + " - " + dt1.format(gig.getGigStart()));
        mail.setText("I'm Sorry. You were not selected for the the gig " + gig.getGigTitle() + ". Please check out some other gigs."  );

        javaMailSender.send(mail);
        System.out.println("Decline Mail Sent!");
    }

    public void sendHireNotification(MusicianProfile musicianProfile, Gig gig) throws MailException{
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, MMM d, ''yy");

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(musicianProfile.getUser().getEmail());
        mail.setFrom("thegigfinder17@gmail.com");
        mail.setSubject("You're Hired! " + gig.getGigTitle() + " - " + dt1.format(gig.getGigStart()));
        mail.setText("Congrats! You are selected for the the gig " + gig.getGigTitle() + ". Show those skills off and have fun!"  );

        javaMailSender.send(mail);
        System.out.println("Hire Mail Sent!");

    }
}
