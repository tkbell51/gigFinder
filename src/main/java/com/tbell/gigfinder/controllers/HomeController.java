package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

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

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String intro(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {

            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {
        }
        return "index";
    }
//    @RequestMapping(value = "/home/", method = RequestMethod.GET)
//    public String homePage(Model model, Principal principal){
//        String username = principal.getName();
//        User user = userRepo.findByUsername(username);
//        return
//    }

    @RequestMapping(value = "/home/find-bands", method = RequestMethod.GET)
    public String findBands(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
    Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
    model.addAttribute("musicians", allmusicians);
    return "findBands";
    }

    @RequestMapping(value = "/home/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);

        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "findGigs";
    }
}
