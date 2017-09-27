package com.tbell.gigfinder.controllers;



import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MediaContent;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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

    @RequestMapping(value = "/home/find-bands", method = RequestMethod.GET)
    public String findBands(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {

            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {

        }
    Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
    model.addAttribute("musicians", allmusicians);
    return "findBands";
    }

    @RequestMapping(value = "/home/find-bands/{musicianId}", method = RequestMethod.GET)
    public String musicianDetails(@PathVariable("musicianId")long id,
                                  Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {

            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {

        }
        MusicianProfile musicianProfile = musicianRepo.findById(id);
        model.addAttribute("musicianProfile", musicianProfile);

        Iterable<MediaContent> mySongs = mediaRepo.findByMusicianProfile(musicianProfile);
        model.addAttribute("media", mySongs);
        return "musicianDetails";
    }

    @RequestMapping(value = "/home/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {

            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {

        }
        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "findGigs";
    }


}
