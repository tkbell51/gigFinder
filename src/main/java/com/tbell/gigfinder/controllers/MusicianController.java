package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.RoleRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MusicianController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

//    @RequestMapping(value = "/gigfinder/musician", method = RequestMethod.GET)
//    public String musicianHome (Model Model);
}
