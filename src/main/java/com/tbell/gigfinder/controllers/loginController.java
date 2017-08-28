package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Controller
public class loginController {
    @Autowired
    UserRepository userRepo;

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
    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signupForm(Model model){
        model.addAttribute("user", new User());
        return "signup";
    }
}
