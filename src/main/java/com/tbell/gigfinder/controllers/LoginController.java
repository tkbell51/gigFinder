package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CompanyProfileRepository;
import com.tbell.gigfinder.Repositories.MusicianProfileRepository;
import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.MusicianProfile;
import com.tbell.gigfinder.models.Role;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
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
        return "createCompany";
    }

    @RequestMapping(value = "/signup/company/", method = RequestMethod.POST)
    public String createCompanyProfile(@RequestParam("username")String username,
                                       @RequestParam("password")String password,
                                       @RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email")String email,
                                       @RequestParam("companyName")String companyName,
                                       Model model){
        User user = new User();
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role companyRole = roleRepo.findByName("ROLE_COMPANY");
        user.setRole(companyRole);
        userRepo.save(user);

        CompanyProfile companyProfile = new CompanyProfile();
        companyProfile.setUser(user);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);
        companyProfile.setPhoneNumber(phoneNumber);
        companyProfile.setEmail(email);
        compRepo.save(companyProfile);
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
        return "createMusician";
    }

    @RequestMapping(value = "/signup/musician/", method = RequestMethod.POST)
    public String createMusicianProfile(@RequestParam("username")String username,
                                       @RequestParam("password")String password,
                                       @RequestParam("firstName")String firstName,
                                       @RequestParam("lastName")String lastName,
                                       @RequestParam("musicianPhoneNumber")String phoneNumber,
                                       @RequestParam("musicianEmail")String musicianEmail,
                                       @RequestParam("birthDate")String birthDate,
                                       @RequestParam("musicianInstruments")String instruments,
                                       @RequestParam("musicianState") String state,
                                       @RequestParam("musicianCity")String city,
                                       @RequestParam("bio")String bio,
                                       Model model){
        User user = new User();
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        Role musicianRole = roleRepo.findByName("ROLE_MUSICIAN");
        user.setRole(musicianRole);
        userRepo.save(user);
        String location = city + ", " + state;
        MusicianProfile musicianProfile  = new MusicianProfile(user, firstName, lastName, musicianEmail,
                phoneNumber, birthDate, instruments, location, bio);
        musicRepo.save(musicianProfile);
        return "redirect:/login";

    }
    
}
