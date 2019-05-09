package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.Meetings;
import com.michal.crm.model.Tasks;
import com.michal.crm.dto.MeetingSumm;
import com.michal.crm.dto.TaskSumm;
import com.michal.crm.model.Users;
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
    @Autowired
    private UserService userService;

    /**
     * Method get list of meeting in specific time plus aggregated contacts
     *
     * @param eventType Today, Actual, Unmet, History
     */
    public List<MeetingSumm> getMeetingSumm(MyEventType eventType, int contId) {
        List<MeetingSumm> meetingSumm = new ArrayList<>();
        List<Meetings> meetings = new ArrayList<>();
        Users user = userService.getLoggedUser();
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
            case ALL:
                if (contId > 0){
                    meetings = getAllMeetingsByContact(contId);
                }else {
                    //TODO query for all users meetings
                }
                break;
        }

        for (Meetings meeting :
                meetings) {
            List<MeetingContacts> meetingContacts = meetingContactsRepository.findMeetingContactsByMeetingIdAndUser(meeting.getId(), user);
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
        Users user = userService.getLoggedUser();
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
            case ALL:
                if (contId >0){
                    tasks = getAllTasksByContact(contId);
                }else {
                    //TODO query for all users tasks
                }
                break;
        }
        for (Tasks task :
                tasks) {
            List<TaskContacts> taskContacts = taskContactsRepository.findTaskContactsByTaskIdAndUser(task.getId(), user);
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
            TaskContacts taskContacts = taskContactsRepository.getTaskContact(contact, task, userService.getLoggedUser());
            taskContactsRepository.delete(taskContacts);
        } else {
            Meetings meeting = getMeetingById(activityId.getId());
            MeetingContacts meetingContacts = meetingContactsRepository.getMeetingContact(contact, meeting, userService.getLoggedUser());
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
        List<MeetingContacts> meetingsList = meetingContactsRepository.findMeetingContactsByMeetingIdAndUser(id, userService.getLoggedUser());
        meetingsRepository.delete(meeting);
        for (MeetingContacts meet :
                meetingsList) {
            meetingContactsRepository.delete(meet);
        }
    }

    public void deleteTask(Integer id) {
        Tasks task = getTaskById(id);
        List<TaskContacts> contactsList = taskContactsRepository.findTaskContactsByTaskIdAndUser(id, userService.getLoggedUser());
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
        return meetingsRepository.findMeetingsByIdAndUser(id, userService.getLoggedUser());
    }

    public Tasks getTaskById(Integer id) {
        return tasksRepository.findTasksByIdAndUser(id,userService.getLoggedUser());
    }

    private List<Meetings> getTodayMeetings() {
        return meetingsRepository.getTodayMeetings(getTodayNight(), getTodayMorning(), userService.getLoggedUser());
    }

    private List<Meetings> getActualMeetings() {
        return meetingsRepository.getActualMeetings(new Date(), false, userService.getLoggedUser());
    }

    private List<Meetings> getAllMeetingsByContact(int contId){
        Contacts contact = contactsService.getContactById(contId);
        List<MeetingContacts> meetingContacts = meetingContactsRepository.getAllContactsMeetingsOrderByTerm(contact, userService.getLoggedUser());
        List<Meetings> meetingsList = new ArrayList<>();
        for (MeetingContacts metCon: meetingContacts
        ) {
            meetingsList.add(metCon.getMeeting());
        }
        return meetingsList;
    }

    private List<Meetings> getActualMeetingsByContact(int contId) {
        Contacts contact = contactsService.getContactById(contId);
        List<MeetingContacts> meetCont = meetingContactsRepository.getAllContactsMeetings(contact, userService.getLoggedUser());
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
        return meetingsRepository.getHistoryMeetings(true, userService.getLoggedUser());
    }

    private List<Meetings> getUnmetMeetings() {
        return meetingsRepository.getUnmetMeetings(new Date(), false, userService.getLoggedUser());
    }

    private List<Tasks> getTodayTasks() {
        return tasksRepository.getTodayTasks(getTodayNight(), getTodayMorning(), userService.getLoggedUser());
    }

    private List<Tasks> getActualTasks() {
        return tasksRepository.getActualTasks(new Date(), false, userService.getLoggedUser());
    }

    private List<Tasks> getAllTasksByContact(int contId){
        Contacts contact = contactsService.getContactById(contId);
        List<TaskContacts> taskContacts = taskContactsRepository.getAllContactsTasksOrderByTerm(contact, userService.getLoggedUser());
        List<Tasks> tasksList = new ArrayList<>();
        for (TaskContacts taskCon: taskContacts
        ) {
            tasksList.add(taskCon.getTask());
        }
        return tasksList;
    }

    private List<Tasks> getActualTasksByContact(int contId) {
        Contacts contact = contactsService.getContactById(contId);
        List<TaskContacts> tasksCont = taskContactsRepository.getAllContactsTasks(contact, userService.getLoggedUser());
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
        return tasksRepository.getHistoryTasks(true, userService.getLoggedUser());
    }

    private List<Tasks> getUnmetTasks() {
        return tasksRepository.getUnmetTasks(new Date(), false, userService.getLoggedUser());
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
