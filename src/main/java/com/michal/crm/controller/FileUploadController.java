package com.michal.crm.controller;

import com.michal.crm.model.Files;
import com.michal.crm.model.types.ImageUseType;
import com.michal.crm.model.types.StorageType;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.CompanyService;
import com.michal.crm.service.ContactsService;
import com.michal.crm.service.Storage.Exception.StorageException;
import com.michal.crm.service.Storage.Exception.StorageFileNotFoundException;
import com.michal.crm.service.Storage.StorageService;
import com.michal.crm.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping(value = "/file")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    private CacheService cacheService;
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private ContactsService contactsService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listUploadedFiles(Model model) {
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("filesList", storageService.loadFilesInfo(StorageType.DOC));
        model.addAttribute("isDoc", true);
        return "uploadForm";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename, @RequestParam("isDoc") boolean isDoc) {

        Resource file;
        if (isDoc) {
            file = storageService.loadAsResource(filename, StorageType.DOC);
        } else {
            file = storageService.loadAsResource(filename, StorageType.IMG);
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + file.getFilename() + "\"").body(file);
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.store(file, StorageType.DOC);
        redirectAttributes.addFlashAttribute("message", file.getOriginalFilename() + " was successfully uploaded!");
        redirectAttributes.addFlashAttribute("userCacheInfo", cacheService.getUserInfo());
        return "redirect:/file/";
    }

    @RequestMapping(value = "/image", method = RequestMethod.GET)
    public String listUploadedImg(Model model) {
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("filesList", storageService.loadFilesInfo(StorageType.IMG));
        model.addAttribute("isDoc", false);
        return "uploadForm";
    }

    @RequestMapping(value = "/image", method = RequestMethod.POST)
    public String handleImgUpload(@RequestParam("file") MultipartFile file,
                                  RedirectAttributes redirectAttributes) {
        storageService.store(file, StorageType.IMG);
        redirectAttributes.addFlashAttribute("message", file.getOriginalFilename() + " was successfully uploaded!");
        redirectAttributes.addFlashAttribute("userCacheInfo", cacheService.getUserInfo());
        return "redirect:/file/image";
    }

    @RequestMapping(value = "/deleteFile")
    public String deleteFile(@RequestParam("fileId") int fileId, RedirectAttributes redirectAttributes) throws IOException {
        Files file = storageService.loadFileInfo(fileId);
        storageService.divideFileWithAllUser(file);
        storageService.deleteFile(file);
        redirectAttributes.addFlashAttribute("message", file.getName() + " was successfully deleted!");
        return "redirect:/file/";
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.GET)
    public String uploadImage(Model model, @RequestParam("imageUse") ImageUseType imageUseType, @RequestParam("itemId") int itemId) {
        model.addAttribute("itemId", itemId);
        model.addAttribute("imageUse", imageUseType);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "uploadImgForm";
    }

    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST)
    public String handleImgUpload(@RequestParam("file") MultipartFile file,
                                  @RequestParam("imageUse") ImageUseType imageUseType, @RequestParam("itemId") int itemId) {
        Files newFile = storageService.store(file, StorageType.IMG);
        switch (imageUseType) {
            case USER:
                userService.saveNewImage(newFile);
                cacheService.evictAllCacheValues();
                return "redirect:/index";
            case CONTACT:
                contactsService.saveNewImage(newFile, itemId);
                return "redirect:/contacts/contactDetail?contId=" + itemId;
            case COMPANY:
                companyService.saveNewImage(newFile, itemId);
                return "redirect:/company/companyDetail?compId=" + itemId;
            default:
                return "redirect:/index";
        }
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageException(StorageException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("err_message", ex.getMessage());
        return "redirect:/file/";
    }
}
