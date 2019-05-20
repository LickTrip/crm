package com.michal.crm.controller;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.Files;
import com.michal.crm.model.types.StorageType;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.ContactsService;
import com.michal.crm.service.Storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping(value = "/personalFile")
public class PersonalFileController {

    @Autowired
    CacheService cacheService;
    @Autowired
    ContactsService contactsService;
    @Autowired
    StorageService storageService;

    @RequestMapping(value = "/contactFiles")
    public String contactFiles(Model model, @ModelAttribute(value = "contId") int contId) {
        Contacts contact = contactsService.getContactById(contId);
        List<Files> filesList = storageService.loadContactFilesInfo(contact);
        model.addAttribute("filesList", filesList);
        model.addAttribute("contact", contact);
        model.addAttribute("contId", contId);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactFiles";
    }

    @RequestMapping(value = "/deleteContFile")
    public String deleteContFile(@ModelAttribute(value = "fileId") int fileId, @ModelAttribute(value = "contId") int contId, RedirectAttributes redirectAttributes) {
        Files file = storageService.loadFileInfo(fileId);
        storageService.divideFileWithAllUser(file);
        storageService.deleteFile(file);
        redirectAttributes.addFlashAttribute("contId", contId);
        return "redirect:/personalFile/contactFiles";
    }

    @RequestMapping(value = "/downloadContFile")
    @ResponseBody
    public ResponseEntity<Resource> downloadContFile(@ModelAttribute(value = "fileId") int fileId, @ModelAttribute(value = "contId") int contId, RedirectAttributes redirectAttributes) {
        Files file = storageService.loadFileInfo(fileId);
        Resource rawFile;
        rawFile = storageService.loadAsResource(file.getName(), file.getStorageType());
        storageService.loadAsResource(file.getName(), file.getStorageType());
        redirectAttributes.addFlashAttribute("contId", contId);
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + rawFile.getFilename() + "\"").body(rawFile);
    }
}
