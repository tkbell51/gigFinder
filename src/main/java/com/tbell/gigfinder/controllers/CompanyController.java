package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CompanyProfileRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.MusicianProfile;
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



    @RequestMapping(value = "/gigfinder/company/createprofile/", method = RequestMethod.GET)
    public String companyProfileCreate(@RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email")String email,
                                       @RequestParam("companuPic")String companyPic,
                                       @RequestParam("companyName")String companyName,
                                       Model model){
        //how to get user from session
        CompanyProfile companyProfile = new CompanyProfile(user, companyName, firstName, lastName, phoneNumber, email);
        compRepo.save(companyProfile);
        return "redirect:/gigfinder/company/find-bands";

    }
}
