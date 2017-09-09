package com.tbell.gigfinder.controllers;



import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.Gig;
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
    public String gigDetails(@PathVariable("gigId")long gigId,
                             Model model) throws InterruptedException,  IOException {
        Gig gig = gigRepo.findOne(gigId);
        model.addAttribute("gig", gig);

        ClientKey clientKey = new ClientKey();


        GeoCodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCodingInterface.class, "https://maps.googleapis.com");
        GeoCodingResponse response = geocodingInterface.geoCodingResponse(gig.getGigLocation(),
                clientKey.getAPI_KEY());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=200x100&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getAPI_KEY();
        model.addAttribute("url", oneMarkerUrl);
        System.out.println("-----------------------------------------------");
        System.out.println(gig.getGigDescription());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLat());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLng());
        System.out.println("LOCATION: " + lat + "," + lng);
        System.out.println(oneMarkerUrl);

        System.out.println("-------------------------------------------------");
        return "gigDetails";
    }
}
