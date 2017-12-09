package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import com.tbell.gigfinder.storage.StorageService;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.security.Principal;
import java.time.LocalDate;
import java.util.*;

@Controller
public class MusicianController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    MusicianProfileRepository musicRepo;

    @Autowired
    MediaContentRepository mediaRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MusicianApplyGigRepository applyRepo;

    @Autowired
    private StorageService storageService;


    @RequestMapping(value = "/musician/my-profile", method = RequestMethod.GET)
    public String musicianProfile(Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        model.addAttribute("mediaContent", new MediaContent());
        Iterable<MediaContent> mediaContents = mediaRepo.findByMusicianProfile(musicianProfile);
        model.addAttribute("media", mediaContents);

        Iterable<Gig>localGigs = gigRepo.findByGigLocationContaining(musicianProfile.getLocation());
        model.addAttribute("gig", localGigs);

        Iterable<MusicianApplyGig> myAppliedGigs = applyRepo.findAllByMusicianProfile(musicianProfile);
        model.addAttribute("applied", myAppliedGigs);

        Iterable<MusicianApplyGig> hiredGigs = applyRepo.findAllByMusicianProfileAndHired(musicianProfile, true);
        model.addAttribute("hired", hiredGigs);

        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);


        return "musicianProfile";
    }

    @RequestMapping(value = "/musician/update-profile", method = RequestMethod.POST)
    public String musicianProfileUpdate(@RequestParam("phoneNumber")String phoneNumber,
                                        @RequestParam("email")String email,
                                        @RequestParam("firstName")String firstName,
                                        @RequestParam("lastName")String lastName,
                                        @RequestParam("musicianInstruments")String instruments,
                                        @RequestParam("location") String location,
                                        @RequestParam("bio")String bio,
                                        Model model, Principal principal) throws Exception {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        userRepo.save(user);
        if(phoneNumber.substring(0,2) != "+1") {
            phoneNumber = "+1" + phoneNumber;
        }
        instruments = instruments.replaceAll("[,.!?;:]", "$0 ").replaceAll("\\s+", " ");
        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        musicianProfile.setFirstName(firstName);
        musicianProfile.setLastName(lastName);
        musicianProfile.setMusicianInstruments(instruments);
        musicianProfile.setLocation(location);
        musicianProfile.setBio(bio);
        musicRepo.save(musicianProfile);
        return "redirect:/musician/my-profile";
    }


    @RequestMapping(value = "/musician/add-media", method = RequestMethod.POST)
    public String createGig (@RequestParam("media_url")String mediaURL,
                             @RequestParam("title")String title,
                             Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        MediaContent mediaContent = new MediaContent(mediaURL, new Date(System.currentTimeMillis()), title);
        mediaContent.setMusicianProfile(musicianProfile);
        mediaRepo.save(mediaContent);
        return "redirect:/musician/my-profile";
    }

    @RequestMapping(value = "/musician/media/{mediaId}/update", method = RequestMethod.POST)
    public String updateMedia (@PathVariable("mediaId") long id,
                               @RequestParam("media_url")String mediaURL,
                               @RequestParam("title")String title,
                               Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MediaContent mediaContent = mediaRepo.findById(id);
        mediaContent.setMedia_url(mediaURL);
        mediaContent.setTitle(title);
        mediaContent.setAddedDate(new Date(System.currentTimeMillis()));
        mediaRepo.save(mediaContent);
        return "redirect:/musician/my-profile";
    }

    @RequestMapping(value = "/musician/media/delete/{mediaId}", method = RequestMethod.POST)
    public String deleteMedia (@PathVariable("mediaId")long id){
        mediaRepo.delete(id);
        return "redirect:/musician/my-profile";
    }

    @RequestMapping(value = "/musician/coverPic/{musicianProfileId}/", method = RequestMethod.POST)
    public String uploadMusicCoverPic(@PathVariable("musicianProfileId")long id,
                                      @RequestParam("coverPicImage") MultipartFile coverPic,
                                      Model model, Principal principal)throws Exception{
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);


        if(coverPic == null) {
            model.addAttribute("user", user);

            model.addAttribute("message", "Something went wrong. Please Try Again");
            return "Messages/messagePage";
        } else {
            MusicianProfile newMusician = musicRepo.findById(id);
            storageService.deleteOne(newMusician.getCoverPicImage());
            storageService.store(coverPic);
            String fileName = coverPic.getOriginalFilename();
            newMusician.setCoverPicImage(fileName);
            musicRepo.save(newMusician);
            return "redirect:/musician/my-profile";
        }

    }

    @RequestMapping(value = "/musician/profilePic/{musicianProfileId}/", method = RequestMethod.POST)
    public String uploadMusicProfPic(@PathVariable("musicianProfileId")long id,
                                     @RequestParam("profPicImage") MultipartFile profPic,
                                     Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);


        if(profPic == null) {
            model.addAttribute("user", user);
            model.addAttribute("message", "Something went wrong. Please Try Again");
            return "Messages/messagePage";
        } else {
            MusicianProfile newMusician = musicRepo.findById(id);
            if(!newMusician.getProfPicImage().equals("empty-profile-pic.jpg")){
                storageService.deleteOne(newMusician.getProfPicImage());
            }
            storageService.store(profPic);
            String fileName = profPic.getOriginalFilename();
            newMusician.setProfPicImage(fileName);
            musicRepo.save(newMusician);
            return "redirect:/musician/my-profile";
        }

    }


}
