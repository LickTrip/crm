package com.michal.crm.controller;

import com.michal.crm.dto.MeetingSumm;
import com.michal.crm.dto.TaskSumm;
import com.michal.crm.model.auxObjects.UserCacheInfo;
import com.michal.crm.model.types.MyEventType;
import com.michal.crm.service.ActivityService;
import com.michal.crm.service.CacheService;
import com.michal.crm.service.Storage.StorageService;
import com.michal.crm.service.TestDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.text.ParseException;
import java.util.List;

@Controller
public class WebController {

    @Autowired
    private TestDataService testDataService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private CacheService cacheService;

    private final StorageService storageService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public WebController(StorageService storageService) {
        this.storageService = storageService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void doSomethingAfterStartup() throws ParseException {
        //testDataService.createTestData();
        //logger.info("*** Insert test data to DB - OK ***");
        logger.info("*** Start app - OK ***");
    }

    @RequestMapping(value = "/")
    public RedirectView redirectToIndex() {
        storageService.init();
        return new RedirectView("/index");
    }

    @RequestMapping(value = "/index")
    public String indexPage(Model model) {

        List<MeetingSumm> meetingSummList = activityService.getMeetingSumm(MyEventType.TODAY, 0);
        List<TaskSumm> taskSummList = activityService.getTaskSumm(MyEventType.TODAY, 0);
        model.addAttribute("meetingSummList", meetingSummList);
        model.addAttribute("taskSummList", taskSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "index";
    }

    @RequestMapping(value = "/actualMeetings")
    public String showActualMeetings(Model model) {
        UserCacheInfo userCacheInfo = cacheService.getUserInfo();
        List<MeetingSumm> meetingSummList = activityService.getMeetingSumm(MyEventType.ACTUAL, 0);
        model.addAttribute("meetingSummList", meetingSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "actualMeetings";
    }

    @RequestMapping(value = "/actualTasks")
    public String showActualTasks(Model model) {
        List<TaskSumm> taskSummList = activityService.getTaskSumm(MyEventType.ACTUAL, 0);
        model.addAttribute("taskSummList", taskSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "actualTasks";
    }

    @RequestMapping(value = "/unmetActivities")
    public String showUnmetActivities(Model model) {
        List<MeetingSumm> meetingSummList = activityService.getMeetingSumm(MyEventType.UNMET, 0);
        List<TaskSumm> taskSummList = activityService.getTaskSumm(MyEventType.UNMET, 0);
        model.addAttribute("meetingSummList", meetingSummList);
        model.addAttribute("taskSummList", taskSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "unmetActivities";
    }

    @RequestMapping(value = "/historyActivities")
    public String showHistoryActivities(Model model) {
        List<MeetingSumm> meetingSummList = activityService.getMeetingSumm(MyEventType.HISTORY, 0);
        List<TaskSumm> taskSummList = activityService.getTaskSumm(MyEventType.HISTORY, 0);
        model.addAttribute("meetingSummList", meetingSummList);
        model.addAttribute("taskSummList", taskSummList);
        model.addAttribute("userCacheInfo", cacheService.getUserInfo());
        return "historyActivities";
    }

    @RequestMapping(value = "/login")
    public String login() {
        cacheService.evictAllCacheValues();
        return "login";
    }


}
