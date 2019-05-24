package com.michal.crm.controller;

import com.michal.crm.model.Company;
import com.michal.crm.model.CompanyHistory;
import com.michal.crm.model.Contacts;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.CompanyService;
import com.michal.crm.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/company")
public class CompanyController {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ContactsService contactsService;

    @RequestMapping(value = "/")
    public String companyList(Model model) {
        model.addAttribute("companyHistory", companyService.getCompanyHistory());
        model.addAttribute("searchedComp", companyService.getTopTen());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "listCompany";
    }

    @RequestMapping(value = "/companyDetail")
    public String companyDetail(Model model, @RequestParam(value = "compId") int compId) {
        Company company = companyService.getCompanyById(compId);
        companyService.writeHistory(company);
        model.addAttribute("contactList", companyService.getCompanyContacts(company));
        model.addAttribute("company", company);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "companyDetail";
    }

    @RequestMapping(value = "/searchCompany")
    public String searchCompany(Model model, @RequestParam(value = "searchName") String name) {
        model.addAttribute("companyHistory", companyService.getCompanyHistory());
        model.addAttribute("searchedComp", companyService.searchCompany(name));
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "listCompany";
    }

    @RequestMapping(value = "/searchContacts")
    public String searchContacts(Model model, @RequestParam(value = "searchName") String name, @RequestParam(value = "id") int compId) {
        Company company = companyService.getCompanyById(compId);
        List<Contacts> contactsList = contactsService.searchContacts(name);
        model.addAttribute("searchedCont", contactsList);
        model.addAttribute("addedCont", companyService.getCompanyContacts(company));
        model.addAttribute("company", company);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "companyNewContact";
    }

    @RequestMapping(value = "/addCompany")
    public String addCompany(Model model) {
        model.addAttribute("company", new Company());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "companyNewComp";
    }

    @RequestMapping(value = "/editCompany")
    public String editCompany(Model model, @RequestParam(value = "compId") int compId) {
        Company company = companyService.getCompanyById(compId);
        model.addAttribute("company", company);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "companyEditComp";
    }

    @RequestMapping(value = "/addContacts")
    public String addContacts(Model model, @RequestParam(value = "compId") int compId) {
        Company company = companyService.getCompanyById(compId);
        model.addAttribute("searchedCont", new ArrayList<>());
        model.addAttribute("addedCont", companyService.getCompanyContacts(company));
        model.addAttribute("company", company);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "companyNewContact";
    }

    @RequestMapping(value = "/addContact")
    public String addContact(@RequestParam(value = "compId") int compId, @RequestParam(value = "contId") int contId) {
        companyService.addContactToCompany(compId, contId);
        return "redirect:/company/addContacts?compId=" + compId;
    }

    @RequestMapping(value = "/saveComp")
    public String saveComp(@ModelAttribute(value = "company") Company company) {
        int compId = companyService.addCompany(company);
        return "redirect:/company/companyDetail?compId=" + compId;
    }

    @RequestMapping(value = "/saveChanges")
    public String saveChanges(@ModelAttribute(value = "company") Company company) {
        int compId = companyService.editCompany(company);
        return "redirect:/company/companyDetail?compId=" + compId;
    }

    @RequestMapping(value = "/deleteComp")
    public String deleteComp(@RequestParam(value = "compId") int compId) {
        companyService.deleteCompany(compId);
        return "redirect:/company/";
    }

    @RequestMapping(value = "/deleteContact")
    public String deleteContact(@RequestParam(value = "compId") int compId, @RequestParam(value = "contId") int contId, @RequestParam(value = "isDetail") boolean isDetail) {
        companyService.deleteCompanyContact(compId, contId);
        if (isDetail)
            return "redirect:/company/companyDetail?compId=" + compId;
        else
            return "redirect:/company/addContacts?compId=" + compId;
    }
}
