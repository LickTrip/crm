package com.michal.crm.controller;

import com.michal.crm.model.Contacts;
import com.michal.crm.model.summaries.ContactNotes;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import javax.jws.WebParam;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "/contacts")
public class ContactsController {

    @Autowired
    private ContactsService contactsService;
    @Autowired
    private CacheService cacheService;

   // private int noteIdCash;
    //private int contactIdCash;

    @RequestMapping(value = "/listContacts")
    public String listContactsPage(Model model) {

        model.addAttribute("searchedCont", new ArrayList<>());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "listContacts";
    }

    @RequestMapping(value = "/searchContacts", method = RequestMethod.GET)
    public String searchContacts(Model model, @RequestParam(value = "searchName") String name) {
        List<Contacts> contactsList = contactsService.searchContacts(name);

        model.addAttribute("searchedCont", contactsList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "listContacts";
    }

    @RequestMapping(value = "/contactDetail")
    public String contactDetail(Model model, @ModelAttribute(value = "contId") int contId) {

        Contacts contact = contactsService.getContactById(contId);
        List<ContactNotes> contactNotes = contactsService.getNotes(contId);
        //contactIdCash = contId;
        model.addAttribute("noteList", contactNotes);
        model.addAttribute("contact", contact);
        model.addAttribute("contId", contId);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactDetail";
    }

    @RequestMapping(value = "/newOneNote")
    public String newOneNote(Model model, @ModelAttribute(value = "contId") int contId) {
        model.addAttribute("myNote", new ContactNotes());
        model.addAttribute("isEdit", false);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("contId", contId);
        return "contactDetailNote";
    }

    @RequestMapping(value = "/noteEdit")
    public String noteEdit(Model model, @ModelAttribute(value = "noteId") int noteId, @ModelAttribute(value = "contId") int contId) {
        ContactNotes myNote = contactsService.getNoteById(noteId);

        model.addAttribute("myNote", myNote);
        model.addAttribute("isEdit", true);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        model.addAttribute("contId", contId);
        model.addAttribute("noteId", noteId);
        return "contactDetailNote";
    }

    @RequestMapping(value = "/addNote")
    public RedirectView addNote(RedirectAttributes redirectAttributes, @ModelAttribute(value = "myNote") ContactNotes myNote, @RequestParam(value = "isEdit") boolean isEdit,
                                @ModelAttribute(value = "contId") int contId) {
        if (isEdit) {
            contactsService.editNote(myNote);
        } else {
            contactsService.addNewNote(myNote, contId);
        }
        redirectAttributes.addFlashAttribute("contId", contId);

        return new RedirectView("contactDetail");
    }

    @RequestMapping(value = "/noteDelete")
    public RedirectView deleteNote(RedirectAttributes redirectAttributes, @ModelAttribute(value = "contId") int contId, @ModelAttribute(value = "noteId") int noteId) {
        contactsService.deleteNote(noteId);

        redirectAttributes.addFlashAttribute("contId", contId);
        return new RedirectView("contactDetail");
    }
}
