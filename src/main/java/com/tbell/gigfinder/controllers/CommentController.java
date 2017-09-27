package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.CommentRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    CommentRepository commentRepo;


}
