package com.michal.crm.controller;

import com.michal.crm.model.Email;
import com.michal.crm.model.types.EmailTypes;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@Controller
public class EmailController {

    @Autowired
    private EmailService emailService;
    @Autowired
    private CacheService cacheService;

    @RequestMapping(value = "/newEmail")
    public String newEmailPage(){
        return "newEmail";
    }

    @RequestMapping(value = "/allEmails")
    public String allEmailsPage(Model model){
        List<Email> emailsList = emailService.readEmails(EmailTypes.ALL, null);
        model.addAttribute("emailList", emailsList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "allEmails";
    }

    @RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
    public RedirectView sendEmail(){

        //Email email = new Email("michal.zacpal@uhk.cz", "Test CMR email", "Testovaci email z CMR");
       // emailService.sendEmail(email);
        //TODO redirect view and model
        return new RedirectView("/newEmail");
    }

    @RequestMapping(value = "/receiveEmail", method = RequestMethod.POST)
    public RedirectView receiveEmail() throws Exception {
        emailService.readEmails(EmailTypes.SEEN, null);
//        emailService.downloadEmails("pop3", "pop.gmail.com","995","cmr666.tst@gmail.com","hesloheslo123");
        return new RedirectView("/newEmail");
    }
}
