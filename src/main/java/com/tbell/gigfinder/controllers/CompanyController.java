package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import com.tbell.gigfinder.services.NotificationService;
import com.tbell.gigfinder.services.SMSservice;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MusicianApplyGigRepository applyRepo;

    @Autowired
    MediaContentRepository mediaRepo;

    @Autowired
    NotificationService notificationService;

    @Autowired
    SMSservice smSservice;

    @Autowired
    MessageRepository messageRepo;


    @RequestMapping(value = "/company/my-profile", method = RequestMethod.GET)
    public String companyProfile(Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);


        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(companyProfile);
        model.addAttribute("gig", myGigs);

        for(Gig eachGig : myGigs){
            List<MusicianApplyGig> applyGig = applyRepo.findAllByGig(eachGig);
            model.addAttribute("applied", applyGig);
            Iterable<MusicianApplyGig> hiredGig = applyRepo.findByGigAndHired(eachGig, true);
            model.addAttribute("hired", hiredGig);
        }

        Iterable<MusicianProfile>localMusicians = musicianRepo.findMusicianProfileByLocationContainingIgnoreCase(companyProfile.getCompanyLocation());
        model.addAttribute("musician", localMusicians);


        return "companyProfile";
    }

    @RequestMapping(value = "/company/update-profile", method = RequestMethod.POST)
    public String updateCompanyProfile(@RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email") String email,
                                       @RequestParam("companyName")String companyName,

                                       Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        if(phoneNumber.substring(0,2) != "+1") {
            phoneNumber = "+1" + phoneNumber;
        }
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        userRepo.save(user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);

        compRepo.save(companyProfile);
        return "redirect:/company/my-profile";
    }


    @RequestMapping(value = "/company/create-gig/", method = RequestMethod.GET)
    public String createGig (Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        model.addAttribute("gig", new Gig());
        List<State> stateEnums = Arrays.asList(State.values());
        model.addAttribute("states", stateEnums);
        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Create/createGig";
    }

    @RequestMapping(value = "/company/create-gig/", method = RequestMethod.POST)
    public String createGig (@RequestParam("locationStreet")String street,
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
        Gig newGig = new Gig();
        newGig.setGigTitle(gigTitle);
        newGig.setGigLocation(location);
        newGig.setGigDescription(description);
        newGig.setGigType(type);
        newGig.setGigStart(start);
        newGig.setTimeStart(timeStart);
        newGig.setGigEnd(end);
        newGig.setTimeEnd(timeEnd);
        newGig.setCompanyProfile(compUser);
        newGig.setGigArt(gigArt);
            gigRepo.save(newGig);


        return "redirect:/company/my-profile";
    }





    @RequestMapping(value = "/company/find-bands", method = RequestMethod.GET)
    public String findBands(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);
        Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
        model.addAttribute("musicians", allmusicians);
        return "Search/findBands";
    }

    @RequestMapping(value = "/company/find-bands/{musicianId}", method = RequestMethod.GET)
    public String musicianDetails(@PathVariable("musicianId")long id,
                                  Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);


        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        MusicianProfile musicianDetail = musicianRepo.findById(id);
        model.addAttribute("musicianDetail", musicianDetail);

        Iterable<MediaContent> media = mediaRepo.findByMusicianProfile(musicianDetail);
        model.addAttribute("media", media);

        Iterable<MusicianApplyGig> hiredGigs = applyRepo.findAllByMusicianProfileAndHired(musicianDetail, true);
        model.addAttribute("hired", hiredGigs);
        return "musicianDetails";
    }


    @RequestMapping(value = "/company/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "Search/findGigs";
    }

}
