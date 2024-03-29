package com.michal.crm.controller;

import com.michal.crm.dto.ActivityDto;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.auxObjects.ActivityId;
import com.michal.crm.service.ActivityFactoryService;
import com.michal.crm.service.ActivityService;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.ContactsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/actFactory")
public class ActivityFactoryController {
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityFactoryService activityFactoryService;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ContactsService contactsService;

    private ActivityId activityIdCash;

    @RequestMapping(value = "/newActivity")
    public String newActivity(Model model, @RequestParam(value = "isTask") boolean isTask) {
        activityIdCash = new ActivityId(isTask);
        newActivityModel(model);
        return "newActivity";
    }

    @RequestMapping(value = "/newContactActivity")
    public String newContactActivity(Model model, @RequestParam(value = "contId") int contId, @RequestParam(value = "isTask") boolean isTask){
        activityIdCash = new ActivityId(isTask);
        activityIdCash.setContId(contId);
        newActivityModel(model);
        return "newActivity";
    }

    @RequestMapping(value = "/addActivity")
    public RedirectView addActivity(@ModelAttribute(value = "activityDto") ActivityDto activityDto, @RequestParam(value = "isEdit") boolean isEdit) {
        if (isEdit){
            activityFactoryService.editActivity(activityDto, activityIdCash);
        }else {
            activityIdCash = activityFactoryService.addNewActivity(activityDto, activityIdCash.getContId());
            if(activityIdCash.getContId() > 0){
                activityFactoryService.saveActCont(activityIdCash.getContId(), activityIdCash);
            }
        }
        activityIdCash.setEdit(isEdit);
        return new RedirectView("newActAddCont");
    }

    @RequestMapping(value = "/newActAddCont")
    public String showContactsPage(Model model) {
        addSameContAtt(model);
        model.addAttribute("searchedCont", new ArrayList<>());
        model.addAttribute("showModal", true);
        return "newActAddCont";
    }

    @RequestMapping(value = "/searchContact", method = RequestMethod.GET)
    public String searchContact(Model model, @RequestParam(value = "searchName") String name) {
        List<Contacts> searchedCont = contactsService.searchContacts(name);

        addSameContAtt(model);
        model.addAttribute("searchedCont", searchedCont);
        model.addAttribute("showModal", false);

        return "newActAddCont";
    }

    @RequestMapping(value = "/addContact")
    public String addContact(Model model, @RequestParam(value = "contId") int contId) {
        activityFactoryService.saveActCont(contId, activityIdCash);

        addSameContAtt(model);
        model.addAttribute("searchedCont", new ArrayList<>());
        model.addAttribute("showModal", false);

        return "newActAddCont";
    }

    @RequestMapping(value = "/deleteContact")
    public String deleteContact(Model model, @RequestParam(value = "contId") int contId){
        activityService.deleteContact(contId, activityIdCash);

        addSameContAtt(model);
        model.addAttribute("searchedCont", new ArrayList<>());
        model.addAttribute("showModal", false);

        return "newActAddCont";
    }

    @RequestMapping(value = "/editActivity")
    public String editActivity(Model model, @RequestParam(value = "id") Integer id, @RequestParam(value = "isTask") boolean isTask){
        activityIdCash = new ActivityId(id, isTask);
        editActivityModel(model);
        return "newActivity";
    }

    @RequestMapping(value = "/editContactActivity")
    public String editContactActivity(Model model, @RequestParam(value = "id") Integer id, @RequestParam(value = "contId") int contId, @RequestParam(value = "isTask") boolean isTask){
        activityIdCash = new ActivityId(id, isTask, contId);
        editActivityModel(model);
        return "newActivity";
    }

    private void newActivityModel(Model model){
        model.addAttribute("isTask", activityIdCash.isTask());
        model.addAttribute("isEdit", false);
        model.addAttribute("activityDto", new ActivityDto(new Meetings(), new Tasks()));
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
    }

    private void editActivityModel(Model model){
        if(activityIdCash.isTask()){
            model.addAttribute("activityDto", new ActivityDto(new Meetings(), activityService.getTaskById(activityIdCash.getId())));
        }else {
            model.addAttribute("activityDto", new ActivityDto(activityService.getMeetingById(activityIdCash.getId()), new Tasks()));
        }
        model.addAttribute("isTask", activityIdCash.isTask());
        model.addAttribute("isEdit", true);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
    }


    private void addSameContAtt(Model model){
        if (activityIdCash.isTask()) {
            model.addAttribute("newActivity", activityService.getTaskById(activityIdCash.getId()));
        } else {
            model.addAttribute("newActivity", activityService.getMeetingById(activityIdCash.getId()));
        }
        model.addAttribute("addedCont", activityFactoryService.getAddedCont(activityIdCash));
        model.addAttribute("isEdit", activityIdCash.isEdit());
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());

        if (activityIdCash.getContId() > 0){
            model.addAttribute("contRedir", true);
        }else {
            model.addAttribute("contRedir", false);
        }
        model.addAttribute("redirContId", activityIdCash.getContId());
    }
}
