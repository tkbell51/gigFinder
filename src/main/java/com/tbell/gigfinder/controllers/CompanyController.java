package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Controller
public class CompanyController {

    @Autowired
    UserRepository userRepo;

    @Autowired
    RoleRepository roleRepo;

    @Autowired
    CompanyProfileRepository compRepo;

    @Autowired
    MusicianProfileRepository musicianRepo;

    @Autowired
    GigRepository gigRepo;

    @Autowired
    MusicianApplyGigRepository applyRepo;

    @Autowired
    MediaContentRepository mediaRepo;




    @RequestMapping(value = "/company/my-profile", method = RequestMethod.GET)
    public String companyProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);


        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(companyProfile);
        model.addAttribute("gig", myGigs);

        for(Gig eachGig : myGigs){
            List<MusicianApplyGig> applyGig = applyRepo.findAllByGig(eachGig);
            model.addAttribute("applied", applyGig);
        }


        Iterable<MusicianProfile>localMusicians = musicianRepo.findMusicianProfileByLocationContainingIgnoreCase(companyProfile.getCompanyLocation());
        model.addAttribute("musician", localMusicians);


        return "companyProfile";
    }

    @RequestMapping(value = "/company/{companyId}/update-profile", method = RequestMethod.POST)
    public String updateCompanyProfile(@PathVariable("companyId") long id,
                                       @RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email")String email,
                                       @RequestParam("companyName")String companyName,

                                       Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);


        CompanyProfile companyProfile = compRepo.findById(id);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);
        companyProfile.setPhoneNumber(phoneNumber);
        companyProfile.setEmail(email);

        compRepo.save(companyProfile);
        return "redirect:/company/my-profile";
    }


    @RequestMapping(value = "/company/create-gig/", method = RequestMethod.GET)
    public String createGig (Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("gig", new Gig());
        List<State> stateEnums = Arrays.asList(State.values());
        model.addAttribute("states", stateEnums);
        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        return "createGig";
    }

    @RequestMapping(value = "/company/create-gig/", method = RequestMethod.POST)
    public String createGig (@RequestParam("locationStreet")String street,
                             @RequestParam("locationCity") String city,
                             @RequestParam("locationState") String state,
                             @RequestParam("locationZip") String zip,
                             @RequestParam("gigType")String type,
                             @RequestParam("gigStart") Date start,
                             @RequestParam("gigEnd")Date end,
                             @RequestParam("gigArt")String gigArt,
                             @RequestParam("gigTitle")String gigTitle,
                             @RequestParam("gigDescription") String description,
                             Model model, Principal principal){



        String location = street + ", " + city + ", " + state + ", " + zip;
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);

        Gig newGig = new Gig();
        newGig.setGigTitle(gigTitle);
        newGig.setGigLocation(location);
        newGig.setGigDescription(description);
        newGig.setGigType(type);




        newGig.setGigEnd(end);
        newGig.setGigStart(start);
        newGig.setCompanyProfile(compUser);
        newGig.setGigArt(gigArt);
            gigRepo.save(newGig);


        return "redirect:/company/my-profile";
    }
    @RequestMapping(value = "/company/gig/{gigId}", method = RequestMethod.GET)
    public String companyGigDetails(@PathVariable("gigId") long id,
                                    Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        Gig gig = gigRepo.findOne(id);
        model.addAttribute("gig", gig);

        List<MusicianApplyGig> applyGig = applyRepo.findAllByGigId(id);
        model.addAttribute("applied", applyGig);

        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);
        List<GigTypes> gigEnums = Arrays.asList(GigTypes.values());
        model.addAttribute("gigTypes", gigEnums);
        ClientKey clientKey = new ClientKey();


        GeoCodingInterface geocodingInterface = Feign.builder()
                .decoder(new GsonDecoder())
                .target(GeoCodingInterface.class, "https://maps.googleapis.com");
        GeoCodingResponse response = geocodingInterface.geoCodingResponse(gig.getGigLocation(),
                clientKey.getAPI_KEY());
        double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
        double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x1100&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getAPI_KEY();
        model.addAttribute("url", oneMarkerUrl);
        return "gigDetails";
    }

    @RequestMapping(value = "/company/gig/{gigId}/update", method = RequestMethod.POST)
    public String updateGig(@PathVariable("gigId") long id,
                            @RequestParam("locationStreet")String street,
                            @RequestParam("locationCity") String city,
                            @RequestParam("locationState") String state,
                            @RequestParam("locationZip") String zip,
                            @RequestParam("gigType")String type,
                            @RequestParam("gigStart") Date start,
                            @RequestParam("gigEnd")Date end,
                            @RequestParam("gigArt")String gigArt,
                            @RequestParam("gigTitle")String gigTitle,
                            @RequestParam("gigDescription") String description,
                            Model model, Principal principal){
        String location = street + ", " + city + ", " + state + ", " + zip;
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Gig newGig = gigRepo.findById(id);
        newGig.setGigLocation(location);
        newGig.setGigTitle(gigTitle);
        newGig.setGigDescription(description);
        newGig.setGigType(type);
//

        newGig.setGigEnd(end);
        newGig.setGigStart(start);
        newGig.setGigArt(gigArt);
        newGig.setCompanyProfile(compUser);
        gigRepo.save(newGig);
        return "redirect:/company/my-profile";
    }

    @RequestMapping(value = "/company/gig/{gigId}/delete", method = RequestMethod.POST)
    public String deleteGig(@PathVariable("gigId")long id){
        gigRepo.delete(id);
        return "redirect:/company/my-profile";
    }

    @RequestMapping(value = "/company/find-bands", method = RequestMethod.GET)
    public String findBands(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);
        Iterable<MusicianProfile> allmusicians = musicianRepo.findAll();
        model.addAttribute("musicians", allmusicians);
        return "findBands";
    }

    @RequestMapping(value = "/company/find-bands/{musicianId}", method = RequestMethod.GET)
    public String musicianDetails(@PathVariable("musicianId")long id,
                                  Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        MusicianProfile musicianDetail = musicianRepo.findById(id);
        model.addAttribute("musicianProfile", musicianDetail);

        Iterable<MediaContent> media = mediaRepo.findByMusicianProfile(musicianDetail);
        model.addAttribute("media", media);
        return "musicianDetails";
    }


    @RequestMapping(value = "/company/find-gigs", method = RequestMethod.GET)
    public String findGigs(Model model, Principal principal) {
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        Iterable<Gig> allgigs = gigRepo.findAll();
        model.addAttribute("gig", allgigs);
        return "findGigs";
    }

}
