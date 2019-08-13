package com.michal.crm.controller;

import com.michal.crm.dto.MeetingSumm;
import com.michal.crm.dto.TaskSumm;
import com.michal.crm.model.ContactHistory;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.auxObjects.UserCacheInfo;
import com.michal.crm.model.summaries.ContactNotes;
import com.michal.crm.model.types.MyEventType;
import com.michal.crm.service.ActivityService;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.CompanyService;
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
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CompanyService companyService;

    @RequestMapping(value = "/listContacts")
    public String listContactsPage(Model model) {
        UserCacheInfo cache = cacheService.getUserInfo();
        List<ContactHistory> contactHistoryList = contactsService.getContactHistory(cache.getUserId());
        model.addAttribute("contactHistory", contactHistoryList);
        model.addAttribute("searchedCont", contactsService.getTopTen());
        model.addAttribute("userCacheInfo", cache);
        return "listContacts";
    }

    @RequestMapping(value = "/searchContacts", method = RequestMethod.GET)
    public String searchContacts(Model model, @RequestParam(value = "searchName") String name) {
        UserCacheInfo cache = cacheService.getUserInfo();
        List<Contacts> contactsList = contactsService.searchContacts(name);
        List<ContactHistory> contactHistoryList = contactsService.getContactHistory(cache.getUserId());
        model.addAttribute("searchedCont", contactsList);
        model.addAttribute("userCacheInfo", cache);
        model.addAttribute("contactHistory", contactHistoryList);
        return "listContacts";
    }

    @RequestMapping(value = "/contactDetail")
    public String contactDetail(Model model, @ModelAttribute(value = "contId") int contId) {
        UserCacheInfo cache = cacheService.getUserInfo();
        Contacts contact = contactsService.getContactById(contId);
        contactsService.writeHistory(contact, cache.getUserId());
        List<ContactNotes> contactNotes = contactsService.getNotes(contId);

        model.addAttribute("noteList", contactNotes);
        model.addAttribute("contact", contact);
        model.addAttribute("contId", contId);
        model.addAttribute("userCacheInfo", cache);
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

    @RequestMapping(value = "/contactTasksAndMeetings")
    public String contactTasksAndMeetingsPage(Model model, @ModelAttribute(value = "contId") int contId) {
        List<MeetingSumm> meetingSummList = activityService.getMeetingSumm(MyEventType.ALL, contId);
        List<TaskSumm> taskSummList = activityService.getTaskSumm(MyEventType.ALL, contId);
        model.addAttribute("contId", contId);
        model.addAttribute("meetingSummList", meetingSummList);
        model.addAttribute("taskSummList", taskSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactTasksAndMeetings";
    }

    @RequestMapping(value = "/contactNewCont")
    public String contactNewCont(Model model) {
        model.addAttribute("cont", new Contacts());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactNewCont";
    }

    @RequestMapping(value = "/contactEditCont")
    public String contactEditCont(Model model, @ModelAttribute(value = "contId") int contId) {
        Contacts contact = contactsService.getContactById(contId);
        model.addAttribute("cont", contact);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactEditCont";
    }

    @RequestMapping(value = "/saveCont")
    public String saveCont(@ModelAttribute(value = "cont") Contacts contact) {
        int contId = contactsService.addNewContact(contact);
        return "redirect:/contacts/contactDetail?contId=" + contId;
    }

    @RequestMapping(value = "/saveContChanges")
    public RedirectView saveContChanges(RedirectAttributes attributes, @ModelAttribute(value = "cont") Contacts contact) {
        contactsService.editContact(contact);
        attributes.addFlashAttribute("contId", contact.getId());
        return new RedirectView("contactDetail");
    }

    @RequestMapping(value = "/contactDelete")
    public RedirectView deleteCont(@ModelAttribute(value = "contId") int contId) {
        contactsService.deleteContact(contId);
        return new RedirectView("listContacts");
    }

    @RequestMapping(value = "/newCompany")
    public String newCompany(Model model, @ModelAttribute(value = "contId") int contId) {
        Contacts contact = contactsService.getContactById(contId);
        model.addAttribute("contact", contact);
        model.addAttribute("searchedComp", companyService.getTopTen());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactNewCompany";
    }

    @RequestMapping(value = "/searchCompany")
    public String searchCompany(Model model, @RequestParam(value = "searchName") String name, @ModelAttribute(value = "id") int contId) {
        Contacts contact = contactsService.getContactById(contId);
        model.addAttribute("contact", contact);
        model.addAttribute("searchedComp", companyService.searchCompany(name));
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "contactNewCompany";
    }

    @RequestMapping(value = "/addCompanyToCont")
    public String addCompany(@ModelAttribute(value = "compId") int compId, @ModelAttribute(value = "contId") int contId) {
        companyService.addContactToCompany(compId, contId);
        return "redirect:/contacts/newCompany?contId=" + contId;
    }
}
