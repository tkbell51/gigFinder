package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.MediaContentRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Calendar;
import java.util.Date;

@Controller
public class MusicianController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    MusicianProfileRepository musicRepo;

    @Autowired
    MediaContentRepository mediaRepo;

    @RequestMapping(value = "/dashboard/musician/profile", method = RequestMethod.GET)
    public String musicianProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        Iterable<MediaContent> mediaContents = mediaRepo.findByMusicianProfile(musicianProfile);
        model.addAttribute("mediaContent", mediaContents);
        return "musicianProfile";
    }

    @RequestMapping(value = "/dashboard/musician/createprofile/", method = RequestMethod.GET)
    public String createMusicianProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        return "createMusician";
    }

    @RequestMapping(value = "/dashboard/musician/createprofile/", method = RequestMethod.POST)
    public String companyProfileCreate(@RequestParam("firstName")String firstName,
                                       @RequestParam("lastName")String lastName,
                                       @RequestParam("musicianPhoneNumber")String phoneNumber,
                                       @RequestParam("musicianEmail")String musicianEmail,
                                       @RequestParam("birthDate")Calendar birthDate,
                                       @RequestParam("musicianInstruments")String instruments,
                                       @RequestParam("location") String location,
                                       @RequestParam("bio")String bio,
                                       @RequestParam("picImage") String picImage,
                                       Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        MusicianProfile musicianProfile  = new MusicianProfile(user, firstName, lastName, musicianEmail,
                phoneNumber, birthDate, instruments, location, bio);
                musicianProfile.setPicImage(picImage);
        musicRepo.save(musicianProfile);
        return "redirect:/dashboard/musician/profile";

    }

    @RequestMapping(value = "/dashboard/musician/add-media/", method = RequestMethod.GET)
    public String addMedia (Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        return "addMedia";
    }

    @RequestMapping(value = "/dashboard/musician/add-media", method = RequestMethod.POST)
    public String createGig (@RequestParam("mediaURL")String mediaURL,
                             Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        MediaContent mediaContent = new MediaContent(mediaURL, new Date(System.currentTimeMillis()));
        mediaRepo.save(mediaContent);
        return "redirect:/dashboard/musician/profile";
    }
}
