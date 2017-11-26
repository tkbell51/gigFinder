package com.tbell.gigfinder.controllers;

import com.sun.org.apache.xpath.internal.operations.Mod;
import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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


    //Company Search for musician
    @RequestMapping(value = "/company/search/", method = RequestMethod.GET)
    public String searchPageCompany(Principal principal, Model model){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        return "Search/search";
    }


    @RequestMapping(value = "/searchName", method = RequestMethod.POST)
    public String searchName(@RequestParam("firstName") String firstName,
                             @RequestParam("lastName") String lastName,
                             Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile searchMusicians = musicianRepo.findByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
        model.addAttribute("musician", searchMusicians);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        return "Search/search";

    }

    @RequestMapping(value = "/searchUsername", method = RequestMethod.POST)
    public String searchUsername(@RequestParam("musicianUsername") String musicianUsername,
                                 Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        User searchUser = userRepo.findByUsernameContainingIgnoreCase(musicianUsername);
        MusicianProfile searchMusician = musicianRepo.findByUser(searchUser);
        model.addAttribute("musician", searchMusician);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);

        return "Search/search";
    }

    @RequestMapping(value = "/searchInstrument", method = RequestMethod.POST)
    public String searchByInstrument(@RequestParam("instrument") String instrument,
                                    Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        Iterable<MusicianProfile> searchMusicians = musicianRepo.findByMusicianInstrumentsContainingIgnoreCase(instrument);
        model.addAttribute("musician", searchMusicians);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        return "Search/search";
    }

    @RequestMapping(value = "/searchLocation", method = RequestMethod.POST)
    public String searchByLocation(@RequestParam("location")String location,
                                   Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);



        Iterable<MusicianProfile> searchMusicians = musicianRepo.findMusicianProfileByLocationContainingIgnoreCase(location);
        model.addAttribute("musician", searchMusicians );

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        return "Search/search";
    }


    //Musician Search for gig

    @RequestMapping(value = "/musician/search/", method = RequestMethod.GET)
    public String searchPageMusician(Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";
    }

    @RequestMapping(value = "/searchGigLocation", method = RequestMethod.POST)
    public String searchGigLocation(@RequestParam("location")String location,
                                    Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        Iterable<Gig> searchGig = gigRepo.findByGigLocationContainingIgnoreCase(location);
        model.addAttribute("gig", searchGig);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";

    }

    @RequestMapping(value = "/searchGigType", method = RequestMethod.POST)
    public String searchGigType(@RequestParam("type")String type,
                                Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        Iterable<Gig> searchGig = gigRepo.findByGigTypeContainingIgnoreCase(type);
        model.addAttribute("gig", searchGig);

        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "Search/search";

    }


}
