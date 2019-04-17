package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.dto.ActivityDto;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;
import com.michal.crm.model.auxObjects.ActivityId;
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


    public ActivityId addNewActivity(ActivityDto activityDto) {
        ActivityId activityId = new ActivityId();
        if (activityDto.getTask() != null) {
            activityId.setTask(true);
            Tasks myTask = activityDto.getTask();
            tasksRepository.save(myTask);
            activityId.setId(myTask.getId());
        } else {
            activityId.setTask(false);
            Meetings myMeeting = activityDto.getMeeting();
            meetingsRepository.save(myMeeting);
            activityId.setId(myMeeting.getId());
        }
        return activityId;
    }

    public void saveActCont(int contactId, ActivityId activityId) {
        Contacts contact = contactsRepository.findById(contactId).get();
        if (activityId.isTask()) {
            Tasks task = activityService.getTaskById(activityId.getId());
            TaskContacts taskContacts = new TaskContacts(task, contact);
            taskContactsRepository.save(taskContacts);
        } else {
            Meetings meeting = activityService.getMeetingById(activityId.getId());
            MeetingContacts meetingContacts = new MeetingContacts(meeting, contact);
            meetingContactsRepository.save(meetingContacts);
        }
    }

    public List<Contacts> getAddedCont(ActivityId activityId) {
        List<Contacts> contactsList = new ArrayList<>();
        if (activityId.isTask()) {
            List<TaskContacts> taskContactsList = taskContactsRepository.findTaskContactsByTaskId(activityId.getId());
            for (TaskContacts tCon : taskContactsList) {
                contactsList.add(tCon.getContact());
            }
        } else {
            List<MeetingContacts> meetingContactsList = meetingContactsRepository.findMeetingContactsByMeetingId(activityId.getId());
            for (MeetingContacts mCon : meetingContactsList) {
                contactsList.add(mCon.getContact());
            }
        }
        return contactsList;
    }

    public void editActivity(ActivityDto activityDto, ActivityId activityIdCash) {
        if(activityIdCash.isTask()){
            Tasks task = activityDto.getTask();
            task.setId(activityIdCash.getId());
            tasksRepository.save(task);

        }else{
            activityDto.getMeeting().setId(activityIdCash.getId());
            meetingsRepository.save(activityDto.getMeeting());
        }
    }
}
