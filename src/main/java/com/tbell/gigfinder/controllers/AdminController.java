package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CompanyProfileRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
public class AdminController {
    @Autowired
    UserRepository userRepo;

    @Autowired
    MusicianProfileRepository musicRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String adminPage (Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        Iterable<MusicianProfile> allMusicians = musicRepo.findAll();
        model.addAttribute("musician", allMusicians);
        Iterable<CompanyProfile> allCompanies = compRepo.findAll();
        model.addAttribute("comp", allCompanies);
        return "admin";
    }

}
