package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MediaContentRepository mediaRepo;

    @RequestMapping(value = "/gigfinder/find-bands", method = RequestMethod.GET)
    public String findBands(Model model){
    Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
    model.addAttribute("musicians", allmusicians);
    return "findBands";
    }

    @RequestMapping(value = "/gigfinder/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model){
        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "findGigs";
    }
}
