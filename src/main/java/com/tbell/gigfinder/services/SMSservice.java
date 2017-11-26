package com.tbell.gigfinder.services;

import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

@Service
public class SMSservice {

    private static final String TWILIO_SID =  System.getenv("TWILIO_ACCOUNT_SID");
    private static final String TWILIO_AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private static final String TWILIO_NUMBER = "+18642074948";

    public static void sendMessage(User user, String note) {

        Twilio.init(TWILIO_SID, TWILIO_AUTH_TOKEN);

        Message message = Message
                .creator(new PhoneNumber(user.getPhoneNumber()), new PhoneNumber(TWILIO_NUMBER),
                        note)
                .create();
        System.out.println(message.getSid());

    }

    public static void sendHireMessage(User user, Gig gig) {
        Twilio.init(TWILIO_SID, TWILIO_AUTH_TOKEN);
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, MMM d, ''yy");

        Message message = Message
                .creator(new PhoneNumber(user.getPhoneNumber()), new PhoneNumber(TWILIO_NUMBER),
                        "Congrats! You're hired for the gig '" + gig.getGigTitle() + "' - " + dt1.format(gig.getGigStart()) + ". Show those skills off and have fun!")
                .create();
        System.out.println(message.getSid());
    }

    public static void sendDeclineMessage(User user, Gig gig) {
        Twilio.init(TWILIO_SID, TWILIO_AUTH_TOKEN);
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, MMM d, ''yy");

        Message message = Message
                .creator(new PhoneNumber(user.getPhoneNumber()), new PhoneNumber(TWILIO_NUMBER),
                        "I'm Sorry. You were not selected for the the gig '" + gig.getGigTitle() + "' - " + dt1.format(gig.getGigStart()) +". Please check out some other gigs." )
                .create();
        System.out.println(message.getSid());
    }
}
