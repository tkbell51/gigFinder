package com.tbell.gigfinder.controllers;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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
//    @RequestMapping(value = "/home/", method = RequestMethod.GET)
//    public String homePage(Model model, Principal principal){
//        String username = principal.getName();
//        User user = userRepo.findByUsername(username);
//        return
//    }

    @RequestMapping(value = "/home/find-bands", method = RequestMethod.GET)
    public String findBands(Model model){

    Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
    model.addAttribute("musicians", allmusicians);
    return "findBands";
    }

    @RequestMapping(value = "/home/musician/{musicianId}", method = RequestMethod.GET)
    public String musicianDetails(@RequestParam("musicianId")long id,
                                  Model model){

        MusicianProfile musicianProfile = musicianRepo.findById(id);
        model.addAttribute("musicianProfile", musicianProfile);
        return "musicianDetails";
    }

    @RequestMapping(value = "/home/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model){


        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "findGigs";
    }

    @RequestMapping(value = "/home/gig/{gigId}", method = RequestMethod.GET)
    public String gigDetails(@RequestParam("gigId")long id,
                             Model model, Principal principal) throws InterruptedException, ApiException, IOException {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        Gig gig = gigRepo.findOne(id);
        model.addAttribute("gig", gig);

        ClientKey clientKey = new ClientKey();

        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey(clientKey.getAPI_KEY())
                .build();
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                gig.getGigLocation()).await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        model.addAttribute("location", results);
        System.out.println(gson.toJson(results[0].addressComponents));
        return "gigDetails";
    }
}
