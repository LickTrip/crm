package com.michal.crm.controller;

import com.michal.crm.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/activityComplete")
    public RedirectView meetingComplete(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        completeActivity(id, isTask);
        return getMyActView(pageNo, isTask);
    }

    @RequestMapping(value = "/activityContactComplete")
    public RedirectView activityContactComplete(RedirectAttributes attributes, @RequestParam(value = "id") Integer id, @RequestParam(value = "contId") Integer contId, @RequestParam(value = "isTask") boolean isTask) {
        completeActivity(id, isTask);
        attributes.addFlashAttribute("contId", contId);
        return new RedirectView("/contacts/contactTasksAndMeetings");
    }

    @RequestMapping(value = "/deleteActivity")
    public RedirectView deleteMeeting(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        deleteActivity(id, isTask);
        return getMyActView(pageNo, isTask);
    }

    @RequestMapping(value = "/deleteContactActivity")
    public RedirectView deleteContactActivity(RedirectAttributes attributes, @RequestParam(value = "id") Integer id, @RequestParam(value = "contId") Integer contId, @RequestParam(value = "isTask") boolean isTask) {
        deleteActivity(id, isTask);
        attributes.addFlashAttribute("contId", contId);
        return new RedirectView("/contacts/contactTasksAndMeetings");
    }

    @RequestMapping(value = "/undoActivity")
    public RedirectView undoMeeting(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        undoActivity(id, isTask);
        return getMyActView(pageNo, isTask);
    }

    @RequestMapping(value = "/undoContactActivity")
    public RedirectView undoContactActivity(RedirectAttributes attributes, @RequestParam(value = "id") Integer id, @RequestParam(value = "contId") Integer contId, @RequestParam(value = "isTask") boolean isTask) {
        undoActivity(id, isTask);
        attributes.addFlashAttribute("contId", contId);
        return new RedirectView("/contacts/contactTasksAndMeetings");
    }

    /**
     * @param pageNo const 1 = index, 2 = actual task/meeting, 3 = unmet activities, 4 = history
     * @param isTask true = task, false = meeting
     * @return view of page
     */
    private RedirectView getMyActView(int pageNo, boolean isTask) {
        switch (pageNo) {
            case 1:
                return new RedirectView("index");
            case 2:
                if (isTask)
                    return new RedirectView("actualTasks");
                else
                    return new RedirectView("actualMeetings");
            case 3:
                return new RedirectView("unmetActivities");
            case 4:
                return new RedirectView("historyActivities");
            default:
                return new RedirectView("index");
        }
    }

    private void completeActivity(int activityId, boolean isTask) {
        if (isTask)
            activityService.completeTask(activityId);
        else
            activityService.completeMeeting(activityId);
    }

    private void deleteActivity(int activityId, boolean isTask) {
        if (isTask)
            activityService.deleteTask(activityId);
        else
            activityService.deleteMeeting(activityId);
    }

    private void undoActivity(int activityId, boolean isTask) {
        if (isTask)
            activityService.undoTask(activityId);
        else
            activityService.undoMeeting(activityId);
    }
}
