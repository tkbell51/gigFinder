package com.tbell.gigfinder.services;

import com.tbell.gigfinder.models.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService{

    User findByUsername(String username);
}
