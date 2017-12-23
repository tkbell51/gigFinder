package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.*;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    MusicianApplyGigRepository applyRepo;

    @GetMapping("/")
    public String intro(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {

            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {

        }
        if(request.isUserInRole("ROLE_MUSICIAN")){
            return "redirect:/musician/my-profile";
        } else if(request.isUserInRole("ROLE_COMPANY")){
            return "redirect:/company/my-profile";
        } else {
            return "index";
        }
    }

    @GetMapping("/find-bands")
    public String findBands(Model model, Principal principal) {


            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);

            CompanyProfile companyProfile = compRepo.findByUser(user);
            model.addAttribute("companyProfile", companyProfile);

            MusicianProfile musicianProfile = musicianRepo.findByUser(user);
            model.addAttribute("musicianProfile", musicianProfile);

        Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
        model.addAttribute("musicians", allmusicians);
        return "Search/findBands";
    }

    @GetMapping("/find-gigs")
    public String findGigs(Model model, Principal principal) {


            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);

            CompanyProfile companyProfile = compRepo.findByUser(user);
            model.addAttribute("companyProfile", companyProfile);

            MusicianProfile musicianProfile = musicianRepo.findByUser(user);
            model.addAttribute("musicianProfile", musicianProfile);


        Iterable<Gig> allgigs = gigRepo.findAllByOrderByGigStartAsc();
        model.addAttribute("gig", allgigs);
        return "Search/findGigs";
    }

    @GetMapping("/find-bands/{musicianId}")
    public String musicianDetails(@PathVariable("musicianId")long id,
                                  Model model, Principal principal) {

            User user = userRepo.findByUsername(principal.getName());
            model.addAttribute("user", user);

            CompanyProfile companyProfile = compRepo.findByUser(user);
            model.addAttribute("companyProfile", companyProfile);

            MusicianProfile musicianProfile = musicianRepo.findByUser(user);
            model.addAttribute("musicianProfile", musicianProfile);

        MusicianProfile musicianDetail = musicianRepo.findById(id);
        model.addAttribute("musicianDetail", musicianDetail);

        Iterable<MediaContent> media = mediaRepo.findByMusicianProfile(musicianDetail);
        model.addAttribute("media", media);

        Iterable<MusicianApplyGig> hiredGigs = applyRepo.findAllByMusicianProfileAndHiredOrderByDateAppliedAsc(musicianDetail, true);
        model.addAttribute("hired", hiredGigs);
        return "musicianDetails";
    }


}
