package com.michal.crm.service;

import com.michal.crm.dao.ContactNotesRepository;
import com.michal.crm.dao.ContactsRepository;
import com.michal.crm.model.Contacts;
import com.michal.crm.model.summaries.ContactNotes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContactsService {

    @Autowired
    private ContactsRepository contactsRepository;

    @Autowired
    private ContactNotesRepository contactNotesRepository;

    public List<Contacts> searchContacts(String name) {
        return contactsRepository.getContactsByName(name);
    }

    public Contacts getContactById(int contactId){
        return contactsRepository.findById(contactId).get();
    }

    public List<ContactNotes> getNotes(int contactId){ return contactNotesRepository.findContactNotesByContactId(contactId);}

    public ContactNotes getNoteById(int noteId) {return contactNotesRepository.findById(noteId).get(); }

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
        contactNotesRepository.save(newOne);
    }

    public void deleteNote(int noteId) {
        ContactNotes oneToDelete = getNoteById(noteId);
        contactNotesRepository.delete(oneToDelete);
    }
}
