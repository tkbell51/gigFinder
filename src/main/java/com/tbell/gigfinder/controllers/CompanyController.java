package com.tbell.gigfinder.controllers;

import com.sun.org.apache.regexp.internal.RE;
import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.models.CompanyProfile;
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
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Calendar;

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

    @RequestMapping(value = "/dashboard/company/profile", method = RequestMethod.GET)
    public String companyProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(compUser);
        model.addAttribute("gig", myGigs);
        return "companyProfile";
    }

    @RequestMapping(value = "/dashboard/company/createprofile/", method = RequestMethod.GET)
    public String createCompanyProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        return "createCompany";
    }

    @RequestMapping(value = "/dashboard/company/createprofile/", method = RequestMethod.POST)
    public String companyProfileCreate(@RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email")String email,
                                       @RequestParam("companuPic")String companyPic,
                                       @RequestParam("companyName")String companyName,
                                       Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile companyProfile = new CompanyProfile(user, companyName, firstName, lastName, phoneNumber, email);
        compRepo.save(companyProfile);
        return "redirect:/dashboard/company/profile";

    }

    @RequestMapping(value = "/dashboard/company/create-gig/", method = RequestMethod.GET)
    public String createGig (Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        return "createGig";
    }

    @RequestMapping(value = "/dashboard/company/create-gig", method = RequestMethod.POST)
    public String createGig (@RequestParam("gigLocation")String location,
                             @RequestParam("gigType")String type,
                             @RequestParam("gigStart") Calendar start,
                             @RequestParam("gigEnd")Calendar end,
                             @RequestParam("gigDescription") String description,
                             Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Gig newGig = new Gig();
        newGig.setGigDescription(description);
        newGig.setGigType(type);
        newGig.setGigStart(start);
        newGig.setGigEnd(end);
        newGig.setCompanyProfile(compUser);
        gigRepo.save(newGig);
        return "redirect:/dashboard/company/profile";
    }
}
