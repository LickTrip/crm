package com.michal.crm;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry){
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login").setViewName("login");
        registry.addViewController("/index").setViewName("index");
        registry.addViewController("/newEmail").setViewName("newEmail");
        registry.addViewController("/allEmail").setViewName("allEmail");
        registry.addViewController("/actualMeetings").setViewName("actualMeetings");
        registry.addViewController("/actualTasks").setViewName("actualTasks");
        registry.addViewController("/historyActivities").setViewName("historyActivities");
        registry.addViewController("/unmetActivities").setViewName("unmetActivities");
        registry.addViewController("/meetingCompleteIndex").setViewName("index");
        registry.addViewController("/actFactory/newActivity").setViewName("newActivity");
        registry.addViewController("/actFactory/newActAddCont").setViewName("newActAddCont");
        registry.addViewController("/profile/profileHome").setViewName("profileHome");
        registry.addViewController("/profile/profileEditInfo").setViewName("profileEditInfo");
        registry.addViewController("/profile/profileEmail").setViewName("profileEmail");
        registry.addViewController("/profile/profileEditEmail").setViewName("profileEditEmail");
        registry.addViewController("/profile/profileNewPass").setViewName("profileNewPass");
        registry.addViewController("/profile/profileFtp").setViewName("profileFtp");
        registry.addViewController("/profile/profileEditFtp").setViewName("profileEditFtp");
        registry.addViewController("/registration/createUser").setViewName("createUser");
        registry.addViewController("/contacts/listContacts").setViewName("listContacts");
        registry.addViewController("/contacts/contactDetail").setViewName("contactDetail");
        registry.addViewController("/contacts/contactDetailNote").setViewName("contactDetailNote");
        registry.addViewController("/contacts/contactTasksAndMeetings").setViewName("contactTasksAndMeetings");
        registry.addViewController("/contacts/contactNewCont").setViewName("contactNewCont");
        registry.addViewController("/contacts/contactEditCont").setViewName("contactEditCont");
        registry.addViewController("/file/").setViewName("uploadForm");
    }
}
