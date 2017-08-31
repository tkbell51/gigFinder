package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
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
import java.util.Date;

@Controller
public class LoginController {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;



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

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signupForm(@RequestParam("username")String username,
                             @RequestParam("password")String password,
                             @RequestParam("user_email")String email,
                             @RequestParam("user_phonenumber")String phoneNumber,
                             @RequestParam("role")String role,
                             Model model) {
        User user = new User();
        user.setUsername(username);
        String encryptedPassword = bCryptPasswordEncoder.encode(password);
        user.setPassword(encryptedPassword);
        user.setUser_email(email);
        user.setUser_phonenumber(phoneNumber);
        user.setSignup_date(new Date(System.currentTimeMillis()));
        user.setActive(true);
        switch (role) {
            case "company":
                Role companyRole = roleRepo.findByName("ROLE_COMPANY");
                user.setRole(companyRole);
                break;

            case "musician":
                Role musicianRole = roleRepo.findByName("ROLE_MUSICIAN");
                user.setRole(musicianRole);
                break;

            case "user":
                Role userRole = roleRepo.findByName("ROLE_USER");
                user.setRole(userRole);
                break;
            default:
                break;
        }
        userRepo.save(user);
        return "redirect:/login";
    }
}
