package com.tbell.gigfinder.controllers;


import com.tbell.gigfinder.Repositories.*;
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


        Iterable<Gig> myGigs = gigRepo.findByCompanyProfile(companyProfile);
        model.addAttribute("gig", myGigs);

        for(Gig eachGig : myGigs){
            List<MusicianApplyGig> applyGig = applyRepo.findAllByGig(eachGig);
            model.addAttribute("applied", applyGig);
            Iterable<MusicianApplyGig> hiredGig = applyRepo.findByGigAndHired(eachGig, true);
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

        return "updateProfile";
    }

    @PostMapping("/company/update-profile")
    public String updateCompanyProfile(@ModelAttribute @Valid User user,
                                       BindingResult bindingResultUser,
                                       @ModelAttribute @Valid CompanyProfile companyProfile,
                                       BindingResult bindingResultCompanyProfile,
                                       Model model, Principal principal) {

        if(bindingResultUser.hasErrors()){
            return "updateProfile";
        }else if(bindingResultCompanyProfile.hasErrors()){
            return "updateProfile";
        }

        userRepo.save(user);
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
            storageService.deleteOne(newComp.getCompanyCoverPic());
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
