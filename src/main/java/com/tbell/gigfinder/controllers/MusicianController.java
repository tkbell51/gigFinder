package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.enums.Instruments;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.security.Principal;
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

    @RequestMapping(value = "/musician/my-profile", method = RequestMethod.GET)
    public String musicianProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Iterable<MediaContent> mediaContents = mediaRepo.findByMusicianProfile(musicianProfile);
        model.addAttribute("media", mediaContents);

        model.addAttribute("mediaContent", new MediaContent());
        Iterable<Gig>localGigs = gigRepo.findByGigLocation(musicianProfile.getLocation());
        model.addAttribute("gig", localGigs);
        List<Instruments> instrumentEnums = Arrays.asList(Instruments.values());
        model.addAttribute("instruments", instrumentEnums);
        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        return "musicianProfile";
    }

    @RequestMapping(value = "/musician/{musicianProfileId}/update-profile", method = RequestMethod.POST)
    public String musicianProfileUpdate(@PathVariable("musicianProfileId")long id,
                                        @RequestParam("firstName")String firstName,
                                        @RequestParam("lastName")String lastName,
                                        @RequestParam("musicianPhoneNumber")String phoneNumber,
                                        @RequestParam("musicianEmail")String musicianEmail,
                                        @RequestParam("birthDate")String birthDate,
                                        @RequestParam("musicianInstruments")String instruments,
                                        @RequestParam("location") String location,
                                        @RequestParam("bio")String bio,
                                        Model model) throws Exception {
        MusicianProfile musicianProfile = musicRepo.findById(id);
        musicianProfile.setFirstName(firstName);
        musicianProfile.setLastName(lastName);
        musicianProfile.setMusicianPhoneNumber(phoneNumber);
        musicianProfile.setMusicianEmail(musicianEmail);
        musicianProfile.setBirthDate(birthDate);
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
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
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
                               Model model){
        MediaContent mediaContent = mediaRepo.findOne(id);
        mediaContent.setMedia_url(mediaURL);
        mediaContent.setTitle(title);
        mediaContent.setAddedDate(new Date(System.currentTimeMillis()));
        mediaRepo.save(mediaContent);
        return "redirect:/musician/my-profile";
    }

    @RequestMapping(value = "/musician/media/{mediaId}/delete", method = RequestMethod.POST)
    public String deleteMedia (@PathVariable("mediaId")long id){
        mediaRepo.delete(id);
        return "redirect:/musician/my-profile";
    }

    @RequestMapping(value = "/musician/gig/{gigId}", method = RequestMethod.GET)
    public String gigDetails(@PathVariable("gigId")long gigId,
                             Model model) throws InterruptedException, IOException {
        Gig gig = gigRepo.findOne(gigId);
        model.addAttribute("gig", gig);

        ClientKey clientKey = new ClientKey();


        GeoCodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCodingInterface.class, "https://maps.googleapis.com");
        GeoCodingResponse response = geocodingInterface.geoCodingResponse(gig.getGigLocation(),
                clientKey.getAPI_KEY());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x250&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getAPI_KEY();
        model.addAttribute("url", oneMarkerUrl);
        System.out.println("-----------------------------------------------");
        System.out.println(gig.getGigDescription());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLat());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLng());
        System.out.println("LOCATION: " + lat + "," + lng);
        System.out.println(oneMarkerUrl);

        System.out.println("-------------------------------------------------");
        return "gigDetails";
    }


    @RequestMapping(value = "/musician/gig/{gigId}/apply", method = RequestMethod.POST)
    public String gigApply (@PathVariable("gig")long gigId,
                            Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        MusicianProfile musicianProfile = musicRepo.findByUser(user);

        Gig gigApply = gigRepo.findById(gigId);

        MusicianApplyGig

    }

}
