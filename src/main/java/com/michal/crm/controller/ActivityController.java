package com.michal.crm.controller;

import com.michal.crm.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class ActivityController {
    @Autowired
    private ActivityService activityService;

    @RequestMapping(value = "/activityComplete")
    public RedirectView meetingComplete(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        if (isTask)
            activityService.completeTask(id);
        else
            activityService.completeMeeting(id);
        return getMyActView(pageNo, isTask);
    }

    @RequestMapping(value = "/deleteActivity")
    public RedirectView deleteMeeting(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        if (isTask)
            activityService.deleteTask(id);
        else
            activityService.deleteMeeting(id);
        return getMyActView(pageNo, isTask);
    }

    @RequestMapping(value = "/undoActivity")
    public RedirectView undoMeeting(@RequestParam(value = "id") Integer id, @RequestParam(value = "pageNo") int pageNo, @RequestParam(value = "isTask") boolean isTask) {
        if (isTask)
            activityService.undoTask(id);
        else
            activityService.undoMeeting(id);
        return getMyActView(pageNo, isTask);
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
}
