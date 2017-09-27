package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.Arrays;
import java.util.List;

@Controller
public class CompanyController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;


    @RequestMapping(value = "/company/my-profile", method = RequestMethod.GET)
    public String companyProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);
        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(companyProfile);
        model.addAttribute("gig", myGigs);

        return "companyProfile";
    }

    @RequestMapping(value = "/company/{companyId}/update-profile", method = RequestMethod.POST)
    public String updateCompanyProfile(@PathVariable("companyId") long id,
                                       @RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email")String email,
                                       @RequestParam("companyName")String companyName,
                                       Model model) {
        CompanyProfile companyProfile = compRepo.findById(id);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);
        companyProfile.setPhoneNumber(phoneNumber);
        companyProfile.setEmail(email);
        compRepo.save(companyProfile);
        return "redirect:/company/my-profile";
    }




}
