package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.dto.ActivityDto;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.Users;
import com.michal.crm.model.auxObjects.ActivityId;
import com.michal.crm.model.auxObjects.UserCacheInfo;
import com.michal.crm.model.summaries.MeetingContacts;
import com.michal.crm.model.summaries.TaskContacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ActivityFactoryService {
    @Autowired
    private MeetingsRepository meetingsRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private MeetingContactsRepository meetingContactsRepository;
    @Autowired
    private TaskContactsRepository taskContactsRepository;
    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;


    public ActivityId addNewActivity(ActivityDto activityDto, int contId) {
        ActivityId activityId = new ActivityId();
        if (activityDto.getTask() != null) {
            activityId.setTask(true);
            Tasks myTask = activityDto.getTask();
            myTask.setUser(userService.getLoggedUser());
            tasksRepository.save(myTask);
            activityId.setId(myTask.getId());
        } else {
            activityId.setTask(false);
            Meetings myMeeting = activityDto.getMeeting();
            myMeeting.setUser(userService.getLoggedUser());
            meetingsRepository.save(myMeeting);
            activityId.setId(myMeeting.getId());
        }
        activityId.setContId(contId);
        return activityId;
    }

    public void saveActCont(int contactId, ActivityId activityId) {
        Users user = userService.getLoggedUser();
        Contacts contact = contactsRepository.findContactsByIdAndUser(contactId, user);
        if (activityId.isTask()) {
            Tasks task = activityService.getTaskById(activityId.getId());
            TaskContacts taskContacts = new TaskContacts(task, contact, user);
            taskContactsRepository.save(taskContacts);
        } else {
            Meetings meeting = activityService.getMeetingById(activityId.getId());
            MeetingContacts meetingContacts = new MeetingContacts(meeting, contact, user);
            meetingContactsRepository.save(meetingContacts);
        }
    }

    public List<Contacts> getAddedCont(ActivityId activityId) {
        List<Contacts> contactsList = new ArrayList<>();
        if (activityId.isTask()) {
            List<TaskContacts> taskContactsList = taskContactsRepository.findTaskContactsByTaskIdAndUser(activityId.getId(), userService.getLoggedUser());
            for (TaskContacts tCon : taskContactsList) {
                contactsList.add(tCon.getContact());
            }
        } else {
            List<MeetingContacts> meetingContactsList = meetingContactsRepository.findMeetingContactsByMeetingIdAndUser(activityId.getId(), userService.getLoggedUser());
            for (MeetingContacts mCon : meetingContactsList) {
                contactsList.add(mCon.getContact());
            }
        }
        return contactsList;
    }

    public void editActivity(ActivityDto activityDto, ActivityId activityIdCash) {
        if (activityIdCash.isTask()) {
            Tasks task = activityDto.getTask();
            task.setId(activityIdCash.getId());
            task.setUser(userService.getLoggedUser());
            tasksRepository.save(task);

        } else {
            activityDto.getMeeting().setId(activityIdCash.getId());
            activityDto.getMeeting().setUser(userService.getLoggedUser());
            meetingsRepository.save(activityDto.getMeeting());
        }
    }
}
