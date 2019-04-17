package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.*;
import com.michal.crm.model.summaries.ContactNotes;
import com.michal.crm.model.summaries.MeetingContacts;
import com.michal.crm.model.summaries.TaskContacts;
import com.michal.crm.model.types.AcademicDegreeTypes;
import com.michal.crm.model.types.EmailProtocolTypes;
import com.michal.crm.model.types.MyPriorityType;
import com.michal.crm.model.types.RoleTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class TestDataService {

    @Autowired
    private ContactsRepository contactsRepository;
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private TasksRepository tasksRepository;
    @Autowired
    private MeetingsRepository meetingsRepository;
    @Autowired
    private TaskContactsRepository taskContactsRepository;
    @Autowired
    private MeetingContactsRepository meetingContactsRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private EmailConfigRepository emailConfigRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private FtpConfigRepository ftpConfigRepository;
    @Autowired
    private ContactNotesRepository contactNotesRepository;

    public void createTestData() throws ParseException {
        Addresses address1 = new Addresses("Czech republic", "Hradec Kralove", "Pricna", 666, 50001);
        Addresses address2 = new Addresses("America", "NY", "Low Deck", 337, 33373);

        UserEmailConfig emailConfig1 = new UserEmailConfig("cmr666.tst@gmail.com", "hesloheslo123", "smtp.gmail.com", 587, EmailProtocolTypes.SMTP);

        UserFtpConfig ftpConfig1 = new UserFtpConfig();

        Users user1 = new Users("Michal", "Lapcaz", "admin", "heslo", emailConfig1, ftpConfig1, 721721721, AcademicDegreeTypes.Bc, "Lick Trip", address1, "GOD", new Date());

        Contacts contact1 = new Contacts("Michal", "Lapcaz", AcademicDegreeTypes.Bc, "Lick Trip", address1, "GOD", "michal.zacpal@uhk.cz", 666666666, new Date());
        Contacts contact2 = new Contacts("John", "Smith", AcademicDegreeTypes.Ing, "KnowWhere", address2, "Agent", "michal.zacpal@uhk.cz", 388388388, new Date());

        Tasks tasks1 = new Tasks("Nakup", new Date(), new Date(), "Koupit rohlik a maslo", MyPriorityType.VERY_HIGH, false);
        Tasks tasks2 = new Tasks("Cviceni", new Date(), new Date(), "Makat jako divej", MyPriorityType.MEDIUM, true);

        Meetings meetings1 = new Meetings("Milion baby",new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-01"), "Vydelame chechtaky", "Palawan", false);
        Meetings meetings2 = new Meetings("Hell Yeah",new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"), new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"), "Obcas to tak chodi", "HK", false);
        Meetings meetings3 = new Meetings("Here is not Hero", new Date(), new Date(), "Neni vse jak vypada Neni vse jak vypada Neni vse jak vypada Neni vse jse jak vypada ", "NY", false);

        TaskContacts taskContacts1 = new TaskContacts(tasks1, contact1);

        MeetingContacts meetingContacts1 = new MeetingContacts(meetings1,contact1);
        MeetingContacts meetingContacts2 = new MeetingContacts(meetings1,contact2);
        MeetingContacts meetingContacts3 = new MeetingContacts(meetings3,contact2);

        ContactNotes contactNotes1 = new ContactNotes(contact1, "lala la la la laaaaaa", new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"));
        ContactNotes contactNotes2 = new ContactNotes(contact1, "kolo ***** rovno jak se to rymuje", new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-08"));
        ContactNotes contactNotes3 = new ContactNotes(contact1, "poznamka o nicem", new SimpleDateFormat("yyyy-MM-dd").parse("2007-01-21"));

        addressesRepository.save(address1);
        addressesRepository.save(address2);
        emailConfigRepository.save(emailConfig1);
        ftpConfigRepository.save(ftpConfig1);
        user1 = usersRepository.save(user1);
        contactsRepository.save(contact1);
        contactsRepository.save(contact2);
        contactNotesRepository.save(contactNotes1);
        contactNotesRepository.save(contactNotes2);
        contactNotesRepository.save(contactNotes3);
        tasksRepository.save(tasks1);
        tasksRepository.save(tasks2);
        tasksRepository.save(tasks2);
        meetingsRepository.save(meetings1);
        meetingsRepository.save(meetings2);
        meetingsRepository.save(meetings3);
        taskContactsRepository.save(taskContacts1);
        meetingContactsRepository.save(meetingContacts1);
        meetingContactsRepository.save(meetingContacts2);
        meetingContactsRepository.save(meetingContacts3);

        Role role1 = new Role(user1.getId(), RoleTypes.ADMIN);
        roleRepository.save(role1);
    }
}
