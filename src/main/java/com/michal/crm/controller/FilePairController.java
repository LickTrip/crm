package com.michal.crm.controller;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;
import com.michal.crm.model.types.StorageType;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.ContactsService;
import com.michal.crm.service.Storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/pairFile")
public class FilePairController {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private StorageService storageService;
    @Autowired
    private ContactsService contactsService;

    @RequestMapping(value = "/")
    public String pairFilePage(Model model, @RequestParam("fileId") int fileId){
        mainModel(model, fileId);
        model.addAttribute("searchedCont", contactsService.getTopTen());
        return "uploadFormPairUser";
    }

    @RequestMapping(value = "/addFile")
    public String addFile(@RequestParam("fileId") int fileId, @RequestParam("contId") int contId){
        Contacts contact = contactsService.getContactById(contId);
        Files file = storageService.loadFileInfo(fileId);
        storageService.pairFileWithUser(file,contact);
        return "redirect:/pairFile/?fileId=" + fileId;
    }

    @RequestMapping(value = "/deletePairedFile")
    public String deletePairedFile(@RequestParam("fileId") int fileId, @RequestParam("contId") int contId){
        Contacts contact = contactsService.getContactById(contId);
        Files file = storageService.loadFileInfo(fileId);
        storageService.divideFileWithUser(file,contact);
        return "redirect:/pairFile/?fileId=" + fileId;
    }

    @RequestMapping(value = "/searchContact")
    public String searchFileContact(@RequestParam(value = "searchName") String name, @RequestParam("fileId") int fileId, Model model){
        List<Contacts> searchedContact = contactsService.searchContacts(name);
        mainModel(model,fileId);
        model.addAttribute("searchedCont", searchedContact);
        return "uploadFormPairUser";
    }

    private void mainModel(Model model, int fileId){
        Files file = storageService.loadFileInfo(fileId);
        List<Contacts> contactsList = contactsService.getContactsWithPairFile(file);
        model.addAttribute("file", file);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("addedCont", contactsList);
        model.addAttribute("isDoc", file.getStorageType() == StorageType.DOC);
    }
}
