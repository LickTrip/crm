package com.michal.crm.controller;

import com.michal.crm.model.Files;
import com.michal.crm.model.types.StorageType;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.Storage.Exception.StorageException;
import com.michal.crm.service.Storage.Exception.StorageFileNotFoundException;
import com.michal.crm.service.Storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/file")
public class FileUploadController {

    private final StorageService storageService;

    @Autowired
    private CacheService cacheService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String listUploadedFiles(Model model) throws IOException {

        //Find files in local storage
//        model.addAttribute("files", storageService.loadAll(StorageType.DOC).map(
//                path -> MvcUriComponentsBuilder.fromMethodName(FileUploadController.class,
//                        "serveFile", path.getFileName().toString()).build().toString())
//                .collect(Collectors.toList()));
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("filesList", storageService.loadFilesInfo(StorageType.DOC));
        return "uploadForm";
    }

    @RequestMapping(value = "/files/{filename:.+}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

        Resource file = storageService.loadAsResource(filename, StorageType.DOC);
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

    @RequestMapping(value = "/deleteFile")
    public String deleteFile(@RequestParam("fileId") int fileId, RedirectAttributes redirectAttributes) throws IOException {
        Files file = storageService.loadFileInfo(fileId);
        storageService.deleteFile(file);
        redirectAttributes.addFlashAttribute("message",file.getName() + " was successfully deleted!");
        return "redirect:/file/";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(StorageException.class)
    public String handleStorageException(StorageException ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("err_message", ex.getMessage());
        return "redirect:/file/";
    }
}
