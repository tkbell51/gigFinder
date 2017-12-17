package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CompanyProfileRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


@Controller
@SessionAttributes("user")
public class LoginController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicRepo;


    @GetMapping("/login")
    public String login(Model model, HttpServletRequest request) {
        model.addAttribute("user", new User());
        try {
            Object message = request.getSession().getAttribute("error");
            model.addAttribute("error", message);
            request.getSession().removeAttribute("error");
        } catch (Exception ex) {
        }
        return "login";
    }


    @GetMapping("/signup/company")
    public String createCompanyProfile(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("companyProfile", new CompanyProfile());
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        return "Create/createCompany";
    }

    @PostMapping("/signup/company")
    public String createCompanyProfile(@ModelAttribute @Valid User user,
                                       BindingResult bindingResultUser,
                                       @ModelAttribute @Valid CompanyProfile companyProfile,
                                       BindingResult bindingResultCompanyProfile,
                                       Model model){
        if(bindingResultUser.hasErrors()){
            return "Create/createCompany";
        }else if(bindingResultCompanyProfile.hasErrors()){
            return "Create/createCompany";
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role companyRole = roleRepo.findByName("ROLE_COMPANY");
        user.setRole(companyRole);
        userRepo.save(user);

        companyProfile.setUser(user);
        companyProfile.setCompanyLocation(companyProfile.getCompanyCity() + ", " + companyProfile.getCompanyState());
        compRepo.save(companyProfile);

        model.addAttribute("message", "Thank you for joining! Please login");
        model.addAttribute("user", user);
        model.addAttribute("companyProfile", companyProfile);
        return "results";
    }




    @GetMapping("/signup/musician/")
    public String createMusicianProfile(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("musicianProfile", new MusicianProfile());
        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        return "Create/createMusician";
    }

    @PostMapping("/signup/musician/")
    public String createMusicianProfile(@ModelAttribute @Valid User user,
                                        BindingResult bindingResultUser,
                                        @ModelAttribute @Valid MusicianProfile musicianProfile,
                                        BindingResult bindingResultMusicianProfile,
                                        Model model ){

        if(bindingResultUser.hasErrors()){
            return "Create/createMusician";
        }else if(bindingResultMusicianProfile.hasErrors()){
            return "Create/createMusician";
        }

        String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role musicianRole = roleRepo.findByName("ROLE_MUSICIAN");
        user.setRole(musicianRole);
        userRepo.save(user);

        musicianProfile.setUser(user);
        musicianProfile.setLocation(musicianProfile.getMusicianCity() + ", " + musicianProfile.getMusicianState());
        musicRepo.save(musicianProfile);

        model.addAttribute("message", "Thank you for joining! Please login");
        model.addAttribute("user", user);
        model.addAttribute("musicianProfile", musicianProfile);
        return "results";
    }


}
