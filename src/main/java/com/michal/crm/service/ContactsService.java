package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.*;
import com.michal.crm.model.summaries.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ContactsService {

    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private ContactNotesRepository contactNotesRepository;
    @Autowired
    private ContactHistoryRepository contactHistoryRepository;
    @Autowired
    private ContactFilesRepository contactFilesRepository;
    @Autowired
    private MeetingContactsRepository meetingContactsRepository;
    @Autowired
    private TaskContactsRepository taskContactsRepository;
    @Autowired
    private ContactCommRepository contactCommRepository;
    @Autowired
    private MeetingsRepository meetingsRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private FilesRepository filesRepository;

    public List<Contacts> searchContacts(String name) {
        return contactsRepository.getContactsByName(name, userService.getLoggedUser());
    }

    public Contacts getContactById(int contactId) {
        return contactsRepository.findContactsByIdAndUser(contactId, userService.getLoggedUser());
    }

    public List<ContactNotes> getNotes(int contactId) {
        return contactNotesRepository.findContactNotesByContactIdAndUser(contactId,userService.getLoggedUser());
    }

    public ContactNotes getNoteById(int noteId) {
        return contactNotesRepository.findContactNotesByIdAndUser(noteId, userService.getLoggedUser());
    }

    public List<Contacts> getContactsWithPairFile(Files file){
        List<ContactFiles> contactFilesList = contactFilesRepository.findContactFilesByFileAndUser(file, userService.getLoggedUser());
        List<Contacts> contactsList = new ArrayList<>();
        for (ContactFiles cf: contactFilesList
             ) {
            contactsList.add(cf.getContact());
        }
        return contactsList;
    }

    public void editNote(ContactNotes myNote) {
        ContactNotes oldOne = getNoteById(myNote.getId());
        oldOne.setText(myNote.getText());
        oldOne.setCreateDate(new Date());
        contactNotesRepository.save(oldOne);
    }

    public void addNewNote(ContactNotes myNote, int contId) {
        ContactNotes newOne = new ContactNotes();
        Contacts cont = getContactById(contId);
        newOne.setText(myNote.getText());
        newOne.setContact(cont);
        newOne.setUser(userService.getLoggedUser());
        contactNotesRepository.save(newOne);
    }

    public void deleteNote(int noteId) {
        ContactNotes oneToDelete = getNoteById(noteId);
        contactNotesRepository.delete(oneToDelete);
    }

    public int addNewContact(Contacts contact){
        Users user = userService.getLoggedUser();
        Addresses address = contact.getAddress();
        address.setUser(user);
        address = addressesRepository.save(address);
        contact.setAddress(address);
        contact.setUser(user);
        contact = contactsRepository.save(contact);
        return contact.getId();
    }

    public void editContact(Contacts contact){
        Contacts oldContact = getContactById(contact.getId());
        contact.getAddress().setId(oldContact.getAddress().getId());
        contact.setImage(oldContact.getImage());
        contact.setUser(oldContact.getUser());
        contact.setCompany(oldContact.getCompany());
        contactsRepository.save(contact);
    }

    public List<ContactHistory> getContactHistory(int userId){
        Users user = userService.getUserById(userId);
        return contactHistoryRepository.findContactHistoriesByUserOrderByCreateDateDesc(user);
    }

    public void writeHistory(Contacts contact, int userId) {
        List<ContactHistory> contactHistoryList = getContactHistory(userId);

        for (ContactHistory history : contactHistoryList
        ) {
            if(contact == history.getContact()){
                contactHistoryRepository.delete(history);
            }
        }
        Users user = userService.getUserById(userId);
        ContactHistory history = new ContactHistory();
        history.setUser(user);
        history.setContact(contact);
        contactHistoryRepository.save(history);
    }

    public void deleteContact(int contId) {
        Contacts contact = getContactById(contId);

        deleteAllContactsFiles(contact, false);
        deleteAllContactsHistory(contact);
        deleteAllContactsNotes(contId);
        deleteAllContactsMeetings(contact);
        deleteAllContactsTasks(contact);
        deleteAllContactsComm(contact);

        addressesRepository.delete(contact.getAddress());
        if (contact.getImage() != null){
            filesRepository.delete(contact.getImage());
        }
        contactsRepository.delete(contact);
    }

    public List<Contacts> getTopTen(){
        return contactsRepository.findTop10ByUser(userService.getLoggedUser());
    }

    public void saveNewImage(Files file, int contId){
        Contacts contact = getContactById(contId);
        contact.setImage(file);
        contactsRepository.save(contact);
    }

    private void deleteAllContactsFiles(Contacts contact, boolean deleteAll){
        List<ContactFiles> contactFilesList = contactFilesRepository.findContactFilesByContactAndUser(contact, userService.getLoggedUser());
        if (!contactFilesList.isEmpty()){
            if (!deleteAll){
                contactFilesRepository.deleteAll(contactFilesList);
            }else{
                for (ContactFiles contFile: contactFilesList
                     ) {
                    //TODO dodelat mazanai jak zaznamu z db tak fycicky z uloziste, to same pravdepodobne pro email
                }
            }
        }
    }
    private void deleteAllContactsHistory(Contacts contact){
        List<ContactHistory> contactHistoryList = contactHistoryRepository.findContactHistoriesByContactAndUser(contact, userService.getLoggedUser());
        if (!contactHistoryList.isEmpty()){
            contactHistoryRepository.deleteAll(contactHistoryList);
        }
    }
    private void deleteAllContactsNotes(int contId){
        List<ContactNotes> contactNotesList = contactNotesRepository.findContactNotesByContactIdAndUser(contId, userService.getLoggedUser());
        if (!contactNotesList.isEmpty()){
            contactNotesRepository.deleteAll(contactNotesList);
        }
    }

    private void deleteAllContactsMeetings(Contacts contact){
        Users user = userService.getLoggedUser();
        List<MeetingContacts> meetingContactsList = meetingContactsRepository.getAllContactsMeetings(contact, user);
        for (MeetingContacts metCont: meetingContactsList
             ) {
            Meetings meeting = metCont.getMeeting();
            meetingContactsRepository.delete(metCont);
            List<MeetingContacts> meetingsWithOtherCont = meetingContactsRepository.findMeetingContactsByMeetingIdAndUser(meeting.getId(), user);
            if (meetingsWithOtherCont.isEmpty()){
                meetingsRepository.delete(meeting);
            }
        }
    }

    private void deleteAllContactsTasks(Contacts contact){
        Users user = userService.getLoggedUser();
        List<TaskContacts> taskContactsList = taskContactsRepository.getAllContactsTasks(contact, user);
        for (TaskContacts taskCont: taskContactsList
        ) {
            Tasks task = taskCont.getTask();
            taskContactsRepository.delete(taskCont);
            List<TaskContacts> tasksWithOtherCont = taskContactsRepository.findTaskContactsByTaskIdAndUser(task.getId(), user);
            if (tasksWithOtherCont.isEmpty()){
                tasksRepository.delete(task);
            }
        }
    }

    private void deleteAllContactsComm(Contacts contact){
        List<ContactComm> contactCommList = contactCommRepository.findContactCommsByContactAndUser(contact, userService.getLoggedUser());
        if (!contactCommList.isEmpty()){
            contactCommRepository.deleteAll(contactCommList);
        }
    }
}
