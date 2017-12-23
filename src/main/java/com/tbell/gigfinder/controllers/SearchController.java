package com.tbell.gigfinder.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class SearchController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;



    @GetMapping("/search")
    public String searchPageCompany(Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        MusicianProfile musicianProfile = musicianRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";
    }

    //Company Search for musician
    @PostMapping("/searchMusician")
    public String searchMusician(@RequestParam("search") String search,
                                 Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);

        Iterable<MusicianProfile> searchMusicians = musicianRepo.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCaseOrLocationContainingIgnoreCase(search, search, search);
        model.addAttribute("musician", searchMusicians);
        return "Search/search";

    }

    @PostMapping("/searchInstrument")
    public String searchByInstrument(@RequestParam("instrument") String instrument,
                                    Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        Iterable<MusicianProfile> searchMusicians = musicianRepo.findByMusicianInstrumentsContainingIgnoreCase(instrument);
        model.addAttribute("musician", searchMusicians);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        return "Search/search";
    }

    //Musician Search for gig



    @PostMapping("/searchGigLocation")
    public String searchGigLocation(@RequestParam("location")String location,
                                    Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicianRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Iterable<Gig> searchGig = gigRepo.findByGigLocationContainingIgnoreCaseOrderByGigStartAsc(location);
        model.addAttribute("gig", searchGig);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";

    }

    @PostMapping("/searchGigType")
    public String searchGigType(@RequestParam("type")String type,
                                Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicianRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Iterable<Gig> searchGig = gigRepo.findByGigTypeContainingIgnoreCaseOrderByGigStartAsc(type);
        model.addAttribute("gig", searchGig);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";

    }


}
