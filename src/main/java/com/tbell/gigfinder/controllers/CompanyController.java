package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
import com.tbell.gigfinder.enums.State;
import com.tbell.gigfinder.models.*;
import com.tbell.gigfinder.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.validation.Valid;
import java.security.Principal;
import java.util.Arrays;
import java.util.List;

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
    private StorageService storageService;


    @GetMapping("/company/my-profile")
    public String companyProfile(Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);


        Iterable<Gig> myGigs = gigRepo.findByCompanyProfileOrderByGigStartAsc(companyProfile);
        model.addAttribute("gig", myGigs);

        for(Gig eachGig : myGigs){
            List<MusicianApplyGig> applyGig = applyRepo.findAllByGigOrderByDateAppliedAsc(eachGig);
            model.addAttribute("applied", applyGig);
            Iterable<MusicianApplyGig> hiredGig = applyRepo.findByGigAndHiredOrderByDateAppliedAsc(eachGig, true);
            model.addAttribute("hired", hiredGig);
        }

        Iterable<MusicianProfile>localMusicians = musicianRepo.findMusicianProfileByLocationContainingIgnoreCase(companyProfile.getCompanyLocation());
        model.addAttribute("musician", localMusicians);


        return "companyProfile";
    }

    @GetMapping("/company/update-profile")
    public String UpdateCompanyProfileView(Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        model.addAttribute("companyProfile", companyProfile);

        List<State> stateEnum = Arrays.asList(State.values());
        model.addAttribute("states", stateEnum);

        return "updateProfile";
    }

    @PostMapping("/company/update-profile")
    public String updateCompanyProfile(@RequestParam("companyContactFirstName")String firstName,
                                       @RequestParam("companyContactLastName")String lastName,
                                       @RequestParam("phoneNumber")String phoneNumber,
                                       @RequestParam("email") String email,
                                       @RequestParam("companyName")String companyName,
                                       @RequestParam("companyCity") String city,
                                       @RequestParam("companyState") String state,

                                       Model model, Principal principal) {
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        userRepo.save(user);

        CompanyProfile companyProfile = compRepo.findByUser(user);
        companyProfile.setCompanyName(companyName);
        companyProfile.setCompanyContactFirstName(firstName);
        companyProfile.setCompanyContactLastName(lastName);
        companyProfile.setCompanyCity(city);
        companyProfile.setCompanyState(state);
        companyProfile.setCompanyLocation(city + ", " + state);
        compRepo.save(companyProfile);
        return "redirect:/company/my-profile";
    }

    @PostMapping("/company/{companyProfileId}/coverPic")
    public String uploadCompanyCoverPic(@PathVariable("companyProfileId")long id,
                                        @RequestParam("companyCoverPic") MultipartFile coverPic,
                                        Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

            CompanyProfile newComp = compRepo.findById(id);
            if(newComp.getCompanyCoverPic()!=null){
                storageService.deleteOne(newComp.getCompanyCoverPic());
            }
            String fileName = coverPic.getOriginalFilename();
            newComp.setCompanyCoverPic(fileName);
            compRepo.save(newComp);
            storageService.store(coverPic);
            return "redirect:/company/my-profile";
    }

    @PostMapping("/company/{companyProfileId}/profPic")
    public String uploadCompanyProfPic(@PathVariable("companyProfileId")long id,
                                       @RequestParam("companyProfPic") MultipartFile profPic,
                                       Model model, Principal principal){
        User user = userRepo.findByUsername(principal.getName());
        model.addAttribute("user", user);

            CompanyProfile newComp = compRepo.findById(id);

            if(!newComp.getCompanyProfPic().equals("empty-profile-pic.jpg ")){
                storageService.deleteOne(newComp.getCompanyProfPic());
            }

            storageService.store(profPic);
            String fileName = profPic.getOriginalFilename();
            newComp.setCompanyProfPic(fileName);
            compRepo.save(newComp);
            return "redirect:/company/my-profile";

    }




}
