package com.michal.crm.controller;

import com.michal.crm.dto.ProfileDto;
import com.michal.crm.model.UserFtpConfig;
import com.michal.crm.model.Users;
import com.michal.crm.model.types.ResultTypes;
import com.michal.crm.service.*;
import com.michal.crm.service.Email.EmailService;
import com.michal.crm.service.Email.EmailServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;

@Controller
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    private UserService userService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private FtpService ftpService;
    @Autowired
    private HelperService helperService;

    private final EmailService emailService;

    @Autowired
    public ProfileController(EmailService emailService) {
        this.emailService = emailService;
    }

    //region show
    @RequestMapping(value = "/profileHome")
    public String showProfileHome(Model model) {
        model.addAttribute("user", userService.getLoggedUser());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileHome";
    }

    @RequestMapping(value = "/profileEditInfo")
    public String showProfileEditInfo(Model model) {
        model.addAttribute("user", userService.getLoggedUser());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileEditInfo";
    }

    @RequestMapping(value = "/profileEmail")
    public String showProfileEmail(Model model) {
        model.addAttribute("emailConfig", userService.getLoggedUser().getEmailConfig());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileEmail";
    }

    @RequestMapping(value = "/profileEditEmail")
    public String showProfileEditEmail(Model model, @ModelAttribute("flashAttErr") Object flashAttErr) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUser(userService.getLoggedUser());
        model.addAttribute("showErrorMatchToast", flashAttErr);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileEditEmail";
    }

    @RequestMapping(value = "/profileFtp")
    public String showProfileFtp(Model model) {
        model.addAttribute("ftpConfig", userService.getLoggedUser().getFtpConfig());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("isConnected", false);
        return "profileFtp";
    }

    @RequestMapping(value = "/profileEditFtp")
    public String showProfileEditFtp(Model model, @ModelAttribute("flashAttErr") Object flashAttErr) {
        ProfileDto profileDto = new ProfileDto();
        profileDto.setUser(userService.getLoggedUser());
        model.addAttribute("showErrorMatchToast", flashAttErr);
        model.addAttribute("profileDto", profileDto);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileEditFtp";
    }

    @RequestMapping(value = "/profileNewPass")
    public String showProfileNewPass(Model model, @ModelAttribute("flashAtt") Object flashAtt) {
        model = helperService.addTwoResultAttsForToast(model, flashAtt, "showErrorMatchToast", "showErrorWPassToast", ResultTypes.PASS_NOT_MATCH, ResultTypes.WRONG_PASS);
        model.addAttribute("profileDto", new ProfileDto());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileNewPass";
    }
    //endregion

    @RequestMapping(value = "/editProfileInfo")
    public RedirectView editProfileInfo(@ModelAttribute(value = "user") Users user) {
        userService.updateUserInfo(user);
        return new RedirectView("/profile/profileHome");
    }

    @RequestMapping(value = "/editEmailConf")
    public RedirectView editEmailConf(@ModelAttribute(value = "profileDto") ProfileDto profileDto, RedirectAttributes attributes) {
        ResultTypes resultTypes = emailService.updateEmailInfo(profileDto);
        switch (resultTypes) {
            case PASS_NOT_MATCH:
                attributes.addFlashAttribute("flashAttErr", true);
                return new RedirectView("/profile/profileEditEmail");
            case OK:
                return new RedirectView("/profile/profileEmail");
            default:
                return new RedirectView("/profile/profileEmail");
        }
    }

    @RequestMapping(value = "/editFtpConf")
    public RedirectView editFtpConf(@ModelAttribute(value = "profileDto") ProfileDto profileDto, RedirectAttributes attributes) {
        ResultTypes resultTypes = ftpService.updateFtpInfo(profileDto);
        switch (resultTypes) {
            case PASS_NOT_MATCH:
                attributes.addFlashAttribute("flashAttErr", true);
                return new RedirectView("/profile/profileEditFtp");
            case OK:
                return new RedirectView("/profile/profileFtp");
            default:
                return new RedirectView("/profile/profileFtp");
        }
    }

    @RequestMapping(value = "/changePass")
    public RedirectView changePass(@ModelAttribute(value = "profileDto") ProfileDto profileDto, RedirectAttributes attributes) {
        ResultTypes resultTypes = userService.changePass(profileDto);

        if (resultTypes == ResultTypes.OK)
            return new RedirectView("/profile/profileHome");

        attributes.addFlashAttribute("flashAtt", resultTypes.getNumber());
        return new RedirectView("/profile/profileNewPass");
    }

    @RequestMapping(value = "/testFtp")
    public String testFtp(Model model) throws IOException {
        UserFtpConfig ftpConfig = userService.getLoggedUser().getFtpConfig();

        model.addAttribute("ftpConfig", ftpConfig);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("isConnected", true);
        return "profileFtp";
    }

    @RequestMapping(value = "/profileOutlook")
    public String showProfileOutlook(Model model) {
        model.addAttribute("outlookPath", userService.getLoggedUser().getEmailConfig().getOutlookPath());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileOutlook";
    }

    @RequestMapping(value = "/profileEditOutlook")
    public String showProfileEditOutlook(Model model) {
        model.addAttribute("outlookPath", userService.getLoggedUser().getEmailConfig().getOutlookPath());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "profileEditOutlook";
    }

    @RequestMapping(value = "/editOutlookConf")
    public String editProfileOutlook(@RequestParam(value = "outlookPath") String outlookPath) {
        emailService.updateOutlookPath(outlookPath);
        return "redirect:/profile/profileOutlook";
    }
}
