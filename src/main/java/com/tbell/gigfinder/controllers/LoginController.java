package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CompanyProfileRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import com.tbell.gigfinder.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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

    @Autowired
    private StorageService storageService;


    @RequestMapping(value = "/login", method = RequestMethod.GET)
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


    @RequestMapping(value = "/signup/company/", method = RequestMethod.GET)
    public String createCompanyProfile(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("companyProfile", new CompanyProfile());
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        return "Create/createCompany";
    }

    @RequestMapping(value = "/signup/company/", method = RequestMethod.POST)
    public String createCompanyProfile(@RequestParam("username")String username,
                                       @RequestParam("password")String password,
                                       @RequestParam("email")String email,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("companyName")String companyName,
                                       @RequestParam("companyState") String state,
                                       @RequestParam("companyCity")String city,
                                       @RequestParam("companyProfPic") MultipartFile profPic,
                                       Model model){
        User user = new User();
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setEmail(email);

        String location = city + ", " + state;
        if(phoneNumber.substring(0,2) != "+1") {
            phoneNumber = "+1" + phoneNumber;
        }

        user.setPhoneNumber(phoneNumber);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role companyRole = roleRepo.findByName("ROLE_COMPANY");
        user.setRole(companyRole);
        userRepo.save(user);


        CompanyProfile companyProfile = new CompanyProfile();
        storageService.store(profPic);
        String fileName = profPic.getOriginalFilename();
        companyProfile.setCompanyProfPic(fileName);
        companyProfile.setUser(user);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);
        companyProfile.setCompanyLocation(location);
        compRepo.save(companyProfile);

        model.addAttribute("message", "Thank you for joining! Please login");

        return "redirect:/login";
    }




    @RequestMapping(value = "/signup/musician/", method = RequestMethod.GET)
    public String createMusicianProfile(Model model){
        model.addAttribute("user", new User());
        model.addAttribute("musicianProfile", new MusicianProfile());
        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        return "Create/createMusician";
    }

    @RequestMapping(value = "/signup/musician/", method = RequestMethod.POST)
    public String createMusicianProfile(@RequestParam("username")String username,
                                        @RequestParam("password")String password,
                                        @RequestParam("email")String email,
                                        @RequestParam("phoneNumber")String phoneNumber,
                                        @RequestParam("firstName")String firstName,
                                        @RequestParam("lastName")String lastName,
                                        @RequestParam("musicianInstruments")String instruments,
                                        @RequestParam("musicianState") String state,
                                        @RequestParam("musicianCity")String city,
                                        @RequestParam("bio")String bio,
                                        @RequestParam("profPicImage") MultipartFile profPic,
                                        Model model){
        User user = new User();
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setEmail(email);
        if(phoneNumber.substring(0,2) != "+1") {
            phoneNumber = "+1" + phoneNumber;
        }
        user.setPhoneNumber(phoneNumber);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role musicianRole = roleRepo.findByName("ROLE_MUSICIAN");
        user.setRole(musicianRole);
        userRepo.save(user);

        String location = city + ", " + state;
        instruments = instruments.replaceAll("[,.!?;:]", "$0 ").replaceAll("\\s+", " ");


        MusicianProfile musicianProfile  = new MusicianProfile(user, firstName, lastName, instruments, location, bio);
        storageService.store(profPic);
        String fileName = profPic.getOriginalFilename();
        musicianProfile.setProfPicImage(fileName);
        musicRepo.save(musicianProfile);
        model.addAttribute("message", "Thank you for joining! Please login");
        return "redirect:/login";


    }


}
