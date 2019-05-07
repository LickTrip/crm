package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;
import com.michal.crm.dto.MeetingSumm;
import com.michal.crm.dto.TaskSumm;
import com.michal.crm.model.auxObjects.ActivityId;
import com.michal.crm.model.summaries.MeetingContacts;
import com.michal.crm.model.summaries.TaskContacts;
import com.michal.crm.model.types.MyEventType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ActivityService {
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
    private ContactsService contactsService;

    /**
     * Method get list of meeting in specific time plus aggregated contacts
     *
     * @param eventType Today, Actual, Unmet, History
     */
    public List<MeetingSumm> getMeetingSumm(MyEventType eventType, int contId) {
        List<MeetingSumm> meetingSumm = new ArrayList<>();
        List<Meetings> meetings = new ArrayList<>();

        switch (eventType) {
            case TODAY:
                meetings = getTodayMeetings();
                break;
            case ACTUAL:
                if (contId > 0){
                    meetings = getActualMeetingsByContact(contId);
                }else {
                    meetings = getActualMeetings();
                }
                break;
            case UNMET:
                meetings = getUnmetMeetings();
                break;
            case HISTORY:
                meetings = getHistoryMeetings();
                break;
        }

        for (Meetings meeting :
                meetings) {
            List<MeetingContacts> meetingContacts = meetingContactsRepository.findMeetingContactsByMeetingId(meeting.getId());
            List<Contacts> contacts = new ArrayList<>();
            for (MeetingContacts meetingC :
                    meetingContacts) {
                contacts.add(meetingC.getContact());
            }
            String note = meeting.getNote();
            String preview = note.length() < 30 ? note : note.substring(0, 30);
            meetingSumm.add(new MeetingSumm(meeting, contacts, preview));
        }
        return meetingSumm;
    }

    /**
     * Method get list of tasks in specific time plus aggregated contacts
     *
     * @param eventType Today, Actual, Unmet, History
     */
    public List<TaskSumm> getTaskSumm(MyEventType eventType, int contId) {
        List<TaskSumm> taskSumm = new ArrayList<>();
        List<Tasks> tasks = new ArrayList<>();

        switch (eventType) {
            case TODAY:
                tasks = getTodayTasks();
                break;
            case ACTUAL:
                if (contId > 0) {
                    tasks = getActualTasksByContact(contId);
                }else {
                    tasks = getActualTasks();
                }
                break;
            case UNMET:
                tasks = getUnmetTasks();
                break;
            case HISTORY:
                tasks = getHistoryTasks();
                break;
        }
        for (Tasks task :
                tasks) {
            List<TaskContacts> taskContacts = taskContactsRepository.findTaskContactsByTaskId(task.getId());
            List<Contacts> contacts = new ArrayList<>();
            for (TaskContacts taskC :
                    taskContacts) {
                contacts.add(taskC.getContact());
            }
            String note = task.getNote();
            String preview = note.length() < 30 ? note : note.substring(0, 30);
            taskSumm.add(new TaskSumm(task, contacts, preview));
        }
        return taskSumm;
    }

    public void deleteContact(int contactId, ActivityId activityId) {
        Contacts contact = contactsRepository.findById(contactId).get();
        if (activityId.isTask()) {
            Tasks task = getTaskById(activityId.getId());
            TaskContacts taskContacts = taskContactsRepository.getTaskContact(contact, task);
            taskContactsRepository.delete(taskContacts);
        } else {
            Meetings meeting = getMeetingById(activityId.getId());
            MeetingContacts meetingContacts = meetingContactsRepository.getMeetingContact(contact, meeting);
            meetingContactsRepository.delete(meetingContacts);
        }
    }

    public void completeMeeting(Integer id) {
        Meetings meeting = getMeetingById(id);
        meeting.setComplete(true);
        meetingsRepository.save(meeting);
    }

    public void completeTask(Integer id) {
        Tasks task = getTaskById(id);
        task.setComplete(true);
        tasksRepository.save(task);
    }

    public void deleteMeeting(Integer id) {
        Meetings meeting = getMeetingById(id);
        List<MeetingContacts> meetingsList = meetingContactsRepository.findMeetingContactsByMeetingId(id);
        meetingsRepository.delete(meeting);
        for (MeetingContacts meet :
                meetingsList) {
            meetingContactsRepository.delete(meet);
        }
    }

    public void deleteTask(Integer id) {
        Tasks task = getTaskById(id);
        List<TaskContacts> contactsList = taskContactsRepository.findTaskContactsByTaskId(id);
        tasksRepository.delete(task);
        for (TaskContacts tsk :
                contactsList) {
            taskContactsRepository.delete(tsk);
        }
    }

    public void undoTask(Integer id) {
        Tasks task = getTaskById(id);
        task.setComplete(false);
        tasksRepository.save(task);
    }

    public void undoMeeting(Integer id) {
        Meetings meeting = getMeetingById(id);
        meeting.setComplete(false);
        meetingsRepository.save(meeting);
    }

    public Meetings getMeetingById(Integer id) {
        Optional<Meetings> meetings = meetingsRepository.findById(id);
        return meetings.get();
    }

    public Tasks getTaskById(Integer id) {
        Optional<Tasks> tasks = tasksRepository.findById(id);
        return tasks.get();
    }

    private List<Meetings> getTodayMeetings() {
        return meetingsRepository.getTodayMeetings(getTodayNight(), getTodayMorning());
    }

    private List<Meetings> getActualMeetings() {
        return meetingsRepository.getActualMeetings(new Date(), false);
    }

    private List<Meetings> getActualMeetingsByContact(int contId) {
        Contacts contact = contactsService.getContactById(contId);
        List<MeetingContacts> meetCont = meetingContactsRepository.getAllContactsMeetings(contact);
        List<Meetings> contMeet = new ArrayList<>();
        for (MeetingContacts item : meetCont
        ) {
            Meetings meeting = item.getMeeting();
            if (!meeting.isComplete()) {
                contMeet.add(meeting);
            }
        }
        return contMeet;
    }

    private List<Meetings> getHistoryMeetings() {
        return meetingsRepository.getHistoryMeetings(true);
    }

    private List<Meetings> getUnmetMeetings() {
        return meetingsRepository.getUnmetMeetings(new Date(), false);
    }

    private List<Tasks> getTodayTasks() {
        return tasksRepository.getTodayTasks(getTodayNight(), getTodayMorning());
    }

    private List<Tasks> getActualTasks() {
        return tasksRepository.getActualTasks(new Date(), false);
    }

    private List<Tasks> getActualTasksByContact(int contId) {
        Contacts contact = contactsService.getContactById(contId);
        List<TaskContacts> tasksCont = taskContactsRepository.getAllContactsTasks(contact);
        List<Tasks> contTasks = new ArrayList<>();
        for (TaskContacts item : tasksCont
        ) {
            Tasks task = item.getTask();
            if (!task.getComplete()) {
                contTasks.add(task);
            }
        }

        return contTasks;
    }

    private List<Tasks> getHistoryTasks() {
        return tasksRepository.getHistoryTasks(true);
    }

    private List<Tasks> getUnmetTasks() {
        return tasksRepository.getUnmetTasks(new Date(), false);
    }

    private Date getTodayNight() {
        Calendar calToday = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calToday.set(Calendar.HOUR_OF_DAY, 23);
        calToday.set(Calendar.MINUTE, 59);
        Date today = calToday.getTime();
        return today;
    }

    private Date getTodayMorning() {
        Calendar calToday = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        calToday.set(Calendar.HOUR_OF_DAY, 00);
        calToday.set(Calendar.MINUTE, 00);
        calToday.set(Calendar.MILLISECOND, 000);
        Date today = calToday.getTime();
        return today;
    }
}
