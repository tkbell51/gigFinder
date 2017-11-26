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
import org.springframework.web.bind.annotation.*;

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

    @RequestMapping(value = "/musician/gig/{gigId}", method = RequestMethod.GET)
    public String gigDetails(@PathVariable("gigId")long gigId,
                             Model model, Principal principal) throws InterruptedException, IOException {

        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);
        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        Gig gig = gigRepo.findOne(gigId);
        model.addAttribute("gig", gig);
        Iterable<MusicianApplyGig> applyGig = applyRepo.findAllByGigId(gigId);
        model.addAttribute("applied", applyGig);

        ClientKey clientKey = new ClientKey();


        GeoCodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCodingInterface.class, "https://maps.googleapis.com");
        GeoCodingResponse response = geocodingInterface.geoCodingResponse(gig.getGigLocation(),
                clientKey.getSTATIC_API_KEY());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x1100&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getSTATIC_API_KEY();
        model.addAttribute("url", oneMarkerUrl);
        return "gigDetails";
    }


    @RequestMapping(value = "/musician/gig/{gigId}/apply", method = RequestMethod.POST)
    public String gigApply (@PathVariable("gigId")long gigId,
                            Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        MusicianProfile musicianProfile = musicRepo.findByUser(user);

        Gig gigApply = gigRepo.findById(gigId);

        Iterable<MusicianApplyGig> allApplied = applyRepo.findAllByGigAndMusicianProfile(gigApply, musicianProfile);
        Gig gigA = null;
        MusicianProfile mp1 = null;
        for(MusicianApplyGig eachApplied : allApplied){
            gigA = eachApplied.getGig();
            mp1 = eachApplied.getMusicianProfile();
        }

        MusicianApplyGig musicianApplyGig = new MusicianApplyGig(gigApply, musicianProfile, new Date(System.currentTimeMillis()));

        if(gigApply == gigA && musicianProfile == mp1){
            model.addAttribute("user", user);
            model.addAttribute("musicianProfile", musicianProfile);
            model.addAttribute("message", "You have already applied for this gig.");
            return "Messages/messagePage";
        } else {
            applyRepo.save(musicianApplyGig);
            return "redirect:/musician/gig/{gigId}/success";

        }
    }




    @RequestMapping(value = "/musician/gig/{gigId}/success", method = RequestMethod.GET)
    public String applySuccess(@PathVariable("gigId") long gigId,
                               Principal principal, Model model){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        Gig gigApply = gigRepo.findById(gigId);
        model.addAttribute("gig", gigApply);

        return "Messages/gigSuccess";
    }

    @RequestMapping(value = "/musician/find-bands", method = RequestMethod.GET)
    public String findBands(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        Iterable<MusicianProfile> allmusicians = musicRepo.findAll();
        model.addAttribute("musicians", allmusicians);
        return "Search/findBands";
    }

    @RequestMapping(value = "/musician/find-bands/{musicianId}", method = RequestMethod.GET)
    public String musicianDetails(@PathVariable("musicianId")long id,
                                  Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);

        MusicianProfile musicianDetail = musicRepo.findById(id);
        model.addAttribute("musicianDetail", musicianDetail);

        Iterable<MediaContent> mySongs = mediaRepo.findByMusicianProfile(musicianDetail);
        model.addAttribute("media", mySongs);

        Iterable<MusicianApplyGig> hiredGigs = applyRepo.findAllByMusicianProfileAndHired(musicianDetail, true);
        model.addAttribute("hired", hiredGigs);
        return "musicianDetails";
    }


    @RequestMapping(value = "/musician/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        MusicianProfile musicianProfile = musicRepo.findByUser(user);
        model.addAttribute("musicianProfile", musicianProfile);
        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "Search/findGigs";
    }


}
