package com.michal.crm.controller;

import com.michal.crm.dto.EmailDto;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.CompanyService;
import com.michal.crm.service.ContactsService;
import com.michal.crm.service.Email.EmailService;
import com.michal.crm.service.Email.Exception.EmailException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/emails")
public class EmailController {

    @Autowired
    private CompanyService companyService;
    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CacheService cacheService;

    private final EmailService emailService;
    @Autowired
    public EmailController(EmailService emailService) { this.emailService = emailService; }

    private List<EmailDto> emailList;

    @RequestMapping(value = "/")
    public String emailList(Model model){
        emailList = new ArrayList<>();
        getBasicModel(model);
        return "listEmails";
    }

    @RequestMapping(value = "/overview")
    public String overview(Model model, @RequestParam(value = "isCont") boolean isCont){
        getBasicModel(model);
        model.addAttribute("isCont", isCont);
        return "listEmails";
    }

    @RequestMapping(value = "/searchItem")
    public String searchItem(Model model, @RequestParam(value = "searchName") String name, @RequestParam(value = "isCont") boolean isCont){

        if (isCont){
            model.addAttribute("searchedCont", contactsService.searchContacts(name));
            model.addAttribute("searchedComp", companyService.getTopTen());
        }else {
            model.addAttribute("searchedComp", companyService.searchCompany(name));
            model.addAttribute("searchedCont", contactsService.getTopTen());
        }
        model.addAttribute("isCont", isCont);
        model.addAttribute("addedEmails", emailList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "listEmails";
    }

    @RequestMapping(value = "/addToTable")
    public String addToTable(@RequestParam(value = "itemId") int itemId, @RequestParam(value = "isCont") boolean isCont){
        EmailDto emailItem = emailService.addItemToTable(itemId, isCont);
        emailItem.setId(emailService.generateListId(emailList));
        emailList.add(emailItem);
        return "redirect:/emails/overview?isCont=" + isCont;
    }

    @RequestMapping(value = "/removeFromTable")
    public String removeFromTable(@RequestParam(value = "itemId") int itemId){
        emailList = emailService.removeItem(emailList, itemId);
        return "redirect:/emails/overview?isCont=true";
    }

    @RequestMapping(value = "/generate")
    public String generate(RedirectAttributes redirectAttributes) {
        String emails = emailService.getEmails(emailList);
        redirectAttributes.addFlashAttribute("emailsString", emails);
        return "redirect:/emails/overview?isCont=true";
    }

    @RequestMapping(value = "/openOutlook")
    public String openOutlook(RedirectAttributes redirectAttributes) {
        if (!emailService.openInOutLook(emailList)){
            redirectAttributes.addFlashAttribute("notOutlookPath", true);
        }
        return "redirect:/emails/overview?isCont=true";
    }

    private void getBasicModel(Model model){
        model.addAttribute("addedEmails", emailList);
        model.addAttribute("searchedComp", companyService.getTopTen());
        model.addAttribute("searchedCont", contactsService.getTopTen());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
    }

    @ExceptionHandler(EmailException.class)
    public String handleStorageException(EmailException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("err_message", ex.getMessage());
        return "redirect:/emails/overview?isCont=true";
    }
}
