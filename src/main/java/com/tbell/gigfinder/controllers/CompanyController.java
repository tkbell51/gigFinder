package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.config.ClientKey;
import com.tbell.gigfinder.googleAPI.GeoCodingInterface;
import com.tbell.gigfinder.googleAPI.GeoCodingResponse;
import com.tbell.gigfinder.models.CompanyProfile;
import com.tbell.gigfinder.models.Gig;
import com.tbell.gigfinder.models.User;
import feign.Feign;
import feign.gson.GsonDecoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import java.security.Principal;

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

    @RequestMapping(value = "/dashboard/company/profile", method = RequestMethod.GET)
    public String companyProfile(Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        CompanyProfile compUser = compRepo.findByUser(user);
        model.addAttribute("compUser", compUser);
        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(compUser);
        model.addAttribute("gig", myGigs);

        for (Gig gigs:myGigs
             ) {
            ClientKey clientKey = new ClientKey();

            GeoCodingInterface geocodingInterface = Feign.builder()
                    .decoder(new GsonDecoder())
                    .target(GeoCodingInterface.class, "https://maps.googleapis.com");
            GeoCodingResponse response = geocodingInterface.geoCodingResponse(gigs.getGigLocation(),
                    clientKey.getAPI_KEY());
            double lat = response.getResults().get(0).getGeometry().getLocation().getLat();
            double lng = response.getResults().get(0).getGeometry().getLocation().getLng();
            String oneMarkerUrl = "https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=200x100&maptype=roadmap&markers=color:blue%7Clabel:S%7C" + lat + "," + lng + "&key=" + clientKey.getAPI_KEY();
            model.addAttribute("url", oneMarkerUrl);

            model.addAttribute("map", response);
            System.out.println("-----------------------------------------------");
            System.out.println(gigs.getGigDescription());

            System.out.println(response.getResults().get(0).getGeometry().getLocation().getLat());
            System.out.println(response.getResults().get(0).getGeometry().getLocation().getLng());
            System.out.println("LOCATION: " + lat + "," + lng);
            System.out.println(oneMarkerUrl);
            System.out.println("-------------------------------------------------");
        }
        return "companyProfile";
    }


    @RequestMapping(value = "/dashboard/company/create-gig/", method = RequestMethod.GET)
    public String createGig (Model model, Principal principal){
        String username = principal.getName();
        User user = userRepo.findByUsername(username);
        model.addAttribute("user", user);
        model.addAttribute("gig", new Gig());
        return "createGig";
    }

    @RequestMapping(value = "/dashboard/company/create-gig/", method = RequestMethod.POST)
    public String createGig (@RequestParam("gigLocation")String location,
                             @RequestParam("gigType")String type,
                             @RequestParam("gigStart") String start,
                             @RequestParam("gigEnd")String end,
                             @RequestParam("gigDescription") String description,
                             Model model, Principal principal){
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
        return "redirect:/dashboard/company/profile";
    }


}
