package com.tbell.gigfinder.services;

import com.tbell.gigfinder.Repositories.GigRepository;
import com.tbell.gigfinder.Repositories.UserRepository;
import com.tbell.gigfinder.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepo;

//    @Autowired
//    GigRepository gigRepo;

    @Override
    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = findByUsername(s);
        if (user == null) {
            throw new UsernameNotFoundException("Login / Password are incorrect");
        }
        return user;
    }

//    public void removeOldItems() {
//        Calendar cal = Calendar.getInstance();
//        java.sql.Date today = new java.sql.Date(cal.getTimeInMillis());
//
//        gigRepo.removeOlderThan(today);
//
//
//    }



}
