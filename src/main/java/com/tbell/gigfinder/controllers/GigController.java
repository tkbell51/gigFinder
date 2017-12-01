package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.*;
import com.tbell.gigfinder.services.NotificationService;
import com.tbell.gigfinder.services.SMSservice;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Controller
public class GigController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MusicianApplyGigRepository applyRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SMSservice smSservice;

    @Autowired
    MessageRepository messageRepo;


    @RequestMapping(value = "/gig/{gigId}", method = RequestMethod.GET)
    public String companyGigDetails(@PathVariable("gigId") long id,
                                    Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        MusicianProfile musicianProfile = musicianRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Gig gig = gigRepo.findOne(id);
        model.addAttribute("gig", gig);

        Iterable<MusicianApplyGig> applyGig = applyRepo.findAllByGigId(id);
        model.addAttribute("applied", applyGig);

        Iterable<MusicianApplyGig> hired = applyRepo.findByGigAndHired(gig, true);
        model.addAttribute("hired", hired);

        Iterable<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        Iterable<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        ClientKey clientKey = new ClientKey();

        GeoCodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCodingInterface.class, "https://maps.googleapis.com");
        GeoCodingResponse response = geocodingInterface.geoCodingResponse(gig.getGigLocation(),
                clientKey.getSTATIC_API_KEY());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=400x500&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getSTATIC_API_KEY();
        model.addAttribute("url", oneMarkerUrl);
        return "gigDetails";
    }

    @RequestMapping(value = "/company/gig/{gigId}/update", method = RequestMethod.POST)
    public String updateGig(@PathVariable("gigId") long id,
                            @RequestParam("locationStreet")String street,
                            @RequestParam("locationCity") String city,
                            @RequestParam("locationState") String state,
                            @RequestParam("locationZip") String zip,
                            @RequestParam("gigType")String type,
                            @RequestParam("gigStart") Date start,
                            @RequestParam("timeStart") String timeStart,
                            @RequestParam("gigEnd")Date end,
                            @RequestParam("timeEnd") String timeEnd,
                            @RequestParam("gigArt")String gigArt,
                            @RequestParam("gigTitle")String gigTitle,
                            @RequestParam("gigDescription") String description,
                            Model model, Principal principal){

        String location = street + ", " + city + ", " + state + ", " + zip;
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Gig newGig = gigRepo.findById(id);
        newGig.setGigLocation(location);
        newGig.setGigTitle(gigTitle);
        newGig.setGigDescription(description);
        newGig.setGigType(type);
        newGig.setGigEnd(end);
        newGig.setTimeEnd(timeEnd);
        newGig.setGigStart(start);
        newGig.setTimeStart(timeStart);
        newGig.setGigArt(gigArt);
        newGig.setCompanyProfile(compUser);
        gigRepo.save(newGig);
        return "redirect:/company/my-profile";
    }

    @RequestMapping(value = "/company/gig/{gigId}/delete", method = RequestMethod.POST)
    public String deleteGig(@PathVariable("gigId")long id){
        gigRepo.delete(id);
        return "redirect:/company/my-profile";
    }

    @RequestMapping(value = "/company/gig/{gigId}/confirm-hire/musician/{musicianId}", method = RequestMethod.GET)
    public String confirmHire(@PathVariable("gigId")long gigId,
                              @PathVariable("musicianId")long musicianId,
                              Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        Gig gig = gigRepo.findOne(gigId);
        model.addAttribute("gig", gig);

        MusicianProfile musicianProfile = musicianRepo.findById(musicianId);
        model.addAttribute("musicianProfile", musicianProfile);

        return "confirmHire";
    }

    @RequestMapping(value = "/company/gig/{gigId}/hire", method = RequestMethod.POST)
    public String hireMusician(@PathVariable("gigId")long gigId,
                               @RequestParam("musician")long musicianId,
                               Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        Gig gigApply = gigRepo.findById(gigId);
        gigApply.setGigHired(true);
        gigRepo.save(gigApply);

        MusicianProfile musicianHired = musicianRepo.findById(musicianId);
        MusicianApplyGig hire = applyRepo.findByGigAndMusicianProfile(gigApply,musicianHired);
        hire.setHired(true);
        applyRepo.save(hire);
        SimpleDateFormat dt1 = new SimpleDateFormat("EEE, MMM d, ''yy");
        model.addAttribute("message", "Congratulations!\n" + musicianHired.getFirstName() + " " + musicianHired.getLastName() + "\nis hired for the gig\n" + gigApply.getGigTitle() + "\non " + dt1.format(gigApply.getGigStart()));

        Iterable<MusicianApplyGig> gig = applyRepo.findAllByGig(gigApply);

        Gig gigA = null;
        MusicianProfile mp1 = null;
        Boolean mHired = null;

        for(MusicianApplyGig eachGig:gig){
            gigA = eachGig.getGig();
            mp1 = eachGig.getMusicianProfile();
            mHired = eachGig.isHired();

            if(gigA == gigApply && mp1 != musicianHired){
                System.out.println(mp1.getFirstName() + mp1.getLastName() + " has not been hired for " + gigA.getGigTitle());
                eachGig.setHired(false);
                applyRepo.save(eachGig);

                try{
                    smSservice.sendDeclineMessage(mp1.getUser(), gigA);

                    notificationService.sendDeclineNotification(mp1, gigA);


                    Message message = new Message();
                    message.setAuthorUsername(user.getUsername());
                    message.setSender(user);
                    message.setReceiver(mp1.getUser());
                    message.setNote("Decline " + gigA.getGigTitle());
                    message.setDate(new Date(System.currentTimeMillis()));
                    messageRepo.save(message);
                } catch (MailException ex){

                }
            }
        }

        if(gigA == gigApply && mp1 == musicianHired && mHired == true ){
            System.out.println(mp1.getFirstName() + " " + mp1.getLastName() + " has been hired for " + gigA.getGigTitle());
            try{
                smSservice.sendHireMessage(mp1.getUser(), gigA);

                notificationService.sendHireNotification(mp1, gigA);


                Message message = new Message();
                message.setAuthorUsername(user.getUsername());
                message.setSender(user);
                message.setReceiver(mp1.getUser());
                message.setNote("Hire " + gigA.getGigTitle());
                message.setDate(new Date(System.currentTimeMillis()));
                messageRepo.save(message);
            } catch (MailException ex){

            }
        }

        return "Messages/messagePage";
    }

    @RequestMapping(value = "/musician/gig/{gigId}/apply", method = RequestMethod.POST)
    public String gigApply (@PathVariable("gigId")long gigId,
                            Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        MusicianProfile musicianProfile = musicianRepo.findByUser(user);

        Gig gigApply = gigRepo.findById(gigId);

        Iterable<MusicianApplyGig> allApplied = applyRepo.findAllByGigAndMusicianProfile(gigApply, musicianProfile);
        Gig gigA = null;
        MusicianProfile mp1 = null;
        for(MusicianApplyGig eachApplied : allApplied){
            gigA = eachApplied.getGig();
            mp1 = eachApplied.getMusicianProfile();
        }

        MusicianApplyGig musicianApplyGig = new MusicianApplyGig(gigApply, musicianProfile, new Date(System.currentTimeMillis()));

        if(gigApply == gigA && musicianProfile == mp1){
            model.addAttribute("user", user);
            model.addAttribute("musicianProfile", musicianProfile);
            model.addAttribute("message", "You have already applied for this gig.");
            return "Messages/messagePage";
        } else {
            applyRepo.save(musicianApplyGig);
            return "redirect:/musician/gig/{gigId}/success";

        }
    }




    @RequestMapping(value = "/musician/gig/{gigId}/success", method = RequestMethod.GET)
    public String applySuccess(@PathVariable("gigId") long gigId,
                               Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicianRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Gig gigApply = gigRepo.findById(gigId);
        model.addAttribute("gig", gigApply);

        return "Messages/gigSuccess";
    }
}
