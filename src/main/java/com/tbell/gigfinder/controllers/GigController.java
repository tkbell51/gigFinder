package com.tbell.gigfinder.controllers;

import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.enums.GigTypes;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;

public class GigController {

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
                             @RequestParam("gigStart") String start,
                             @RequestParam("gigEnd")String end,
                             @RequestParam("gigDescription") String description,
                             Model model, Principal principal){
        String location = street + ", " + city + ", " + state + ", " + zip;
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Gig newGig = new Gig();
        newGig.setGigLocation(location);
        newGig.setGigDescription(description);
        newGig.setGigType(type);
        newGig.setGigStart(start);
        newGig.setGigEnd(end);
        newGig.setCompanyProfile(compUser);
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
        String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=14&size=500x1000&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getAPI_KEY();
        model.addAttribute("url", oneMarkerUrl);
        System.out.println("-----------------------------------------------");
        System.out.println(gig.getGigDescription());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLat());
        System.out.println(response.getResults().get(0).getGeometry().getLocation().getLng());
        System.out.println("LOCATION: " + lat + "," + lng);
        System.out.println(oneMarkerUrl);

        System.out.println("-------------------------------------------------");
        return "companyGigDetails";
    }

    @RequestMapping(value = "/company/gig/{gigId}/update", method = RequestMethod.POST)
    public String updateGig(@PathVariable("gigId") long id,
                            @RequestParam("locationStreet")String street,
                            @RequestParam("locationCity") String city,
                            @RequestParam("locationState") String state,
                            @RequestParam("locationZip") String zip,
                            @RequestParam("gigType")String type,
                            @RequestParam("gigStart") String start,
                            @RequestParam("gigEnd")String end,
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
        newGig.setGigDescription(description);
        newGig.setGigType(type);
        newGig.setGigStart(start);
        newGig.setGigEnd(end);
        newGig.setCompanyProfile(compUser);
        gigRepo.save(newGig);
        return "redirect:/company/my-profile";
    }

    @RequestMapping(value = "/company/gig/{gigId}/delete", method = RequestMethod.POST)
    public String deleteGig(@PathVariable("gigId")long id){
        gigRepo.delete(id);
        return "redirect:/company/my-profile";
    }
}
