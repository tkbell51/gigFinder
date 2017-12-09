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
import com.tbell.gigfinder.storage.StorageService;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


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

    @Autowired
    private StorageService storageService;


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

    @RequestMapping(value = "/company/{companyProfileId}/coverPic", method = RequestMethod.POST)
    public String uploadCompanyCoverPic(@PathVariable("companyProfileId")long id,
                                        @RequestParam("companyCoverPic") MultipartFile coverPic,
                                        Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);



        if (coverPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please try Again");
            return "Messages/messagePage";
        } else {
            CompanyProfile newComp = compRepo.findById(id);
            storageService.deleteOne(newComp.getCompanyCoverPic());
            String fileName = coverPic.getOriginalFilename();
            newComp.setCompanyCoverPic(fileName);
            compRepo.save(newComp);
            storageService.store(coverPic);
            return "redirect:/company/my-profile";
        }
    }

    @RequestMapping(value = "/company/{companyProfileId}/profPic", method = RequestMethod.POST)
    public String uploadCompanyProfPic(@PathVariable("companyProfileId")long id,
                                       @RequestParam("companyProfPic") MultipartFile profPic,
                                       Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);



        if (profPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please try Again");
            return "Messages/messagePage";
        } else {
            CompanyProfile newComp = compRepo.findById(id);
            if(!newComp.getCompanyProfPic().equals("empty-profile-pic.jpg ")){
                storageService.deleteOne(newComp.getCompanyProfPic());
            }
            storageService.store(profPic);
            String fileName = profPic.getOriginalFilename();
            newComp.setCompanyProfPic(fileName);
            compRepo.save(newComp);
            return "redirect:/company/my-profile";
        }
    }




}
