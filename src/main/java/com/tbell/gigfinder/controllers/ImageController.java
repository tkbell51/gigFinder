package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class ImageController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MusicianApplyGigRepository applyRepo;


//Company Image Post

    @RequestMapping(value = "/company/{companyProfileId}/coverPic", method = RequestMethod.POST)
    public String uploadCompanyCoverPic(@PathVariable("companyProfileId")long id,
                                        @RequestParam("companyCoverPic") String coverPic,
                                        Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);



        if (coverPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please try Again");
            return "Messages/messagePage";
        } else {
            CompanyProfile newComp = compRepo.findById(id);
            newComp.setCompanyCoverPic(coverPic);
            compRepo.save(newComp);
            return "redirect:/company/my-profile";
        }
    }

    @RequestMapping(value = "/company/{companyProfileId}/profPic", method = RequestMethod.POST)
    public String uploadCompanyProfPic(@PathVariable("companyProfileId")long id,
                                       @RequestParam("companyProfPic") String profPic,
                                       Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);



        if (profPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please try Again");
            return "Messages/messagePage";
        } else {
            CompanyProfile newComp = compRepo.findById(id);
            newComp.setCompanyProfPic(profPic);
            compRepo.save(newComp);
            return "redirect:/company/my-profile";
        }
    }


//    Musician Image Post
    @RequestMapping(value = "/musician/coverPic/{musicianProfileId}/", method = RequestMethod.POST)
    public String uploadMusicCoverPic(@PathVariable("musicianProfileId")long id,
                                      @RequestParam("coverPicImage") String coverPic,
                                      Model model, Principal principal)throws Exception{
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);


        if(coverPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please Try Again");
            return "Messages/messagePage";
        } else {
            MusicianProfile newMusician = musicRepo.findById(id);
            newMusician.setCoverPicImage(coverPic);
            musicRepo.save(newMusician);
            return "redirect:/musician/my-profile";
        }

    }

    @RequestMapping(value = "/musician/profilePic/{musicianProfileId}/", method = RequestMethod.POST)
    public String uploadMusicProfPic(@PathVariable("musicianProfileId")long id,
                                     @RequestParam("profPicImage") String profPic,
                                     Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);


        if(profPic == null) {
            model.addAttribute("user", user);
            model.addAttribute("message", "Something went wrong. Please Try Again");
            return "Messages/messagePage";
        } else {
            MusicianProfile newMusician = musicRepo.findById(id);
            newMusician.setProfPicImage(profPic);
            musicRepo.save(newMusician);
            return "redirect:/musician/my-profile";
        }

    }

}
