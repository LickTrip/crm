package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.*;
import com.michal.crm.model.summaries.*;
import com.michal.crm.model.types.*;
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
    @Autowired
    private ContactHistoryRepository contactHistoryRepository;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private ContactFilesRepository contactFilesRepository;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyHistoryRepository companyHistoryRepository;
    @Autowired
    private CompanyContactsRepository companyContactsRepository;

    public void createTestData() throws ParseException {
        Users user1 = new Users("Michal", "Lapcaz", "admin", "heslo", null, null, 721721721, AcademicDegreeTypes.Bc, "Lick Trip", null, "GOD", new Date(), "R4Nd0m69");
        user1 = usersRepository.save(user1);

        Addresses addressUsr = new Addresses(user1,"Czech republic", "Hradec Kralove", "Pricna", 666, 50001);
        addressUsr = addressesRepository.save(addressUsr);
        Addresses address1 = new Addresses(user1,"Czech republic", "Hradec Kralove", "Pricna", 666, 50001);
        Addresses address2 = new Addresses(user1,"America", "NY", "Low Deck", 337, 33373);

        UserEmailConfig emailConfig1 = new UserEmailConfig(user1,"cmr666.tst@gmail.com", "hesloheslo123", "smtp.gmail.com", 587, EmailProtocolTypes.SMTP);
        emailConfig1 = emailConfigRepository.save(emailConfig1);

        UserFtpConfig ftpConfig1 = new UserFtpConfig();
        ftpConfig1.setUser(user1);
        ftpConfig1 = ftpConfigRepository.save(ftpConfig1);

        user1.setEmailConfig(emailConfig1);
        user1.setFtpConfig(ftpConfig1);
        user1.setAddress(addressUsr);
        user1 = usersRepository.save(user1);

        Contacts contact1 = new Contacts(user1,"Michal", "Lapcaz", AcademicDegreeTypes.Bc, null, address1, "GOD", "michal.zacpal@uhk.cz", 666666666, new Date());
        Contacts contact2 = new Contacts(user1,"John", "Smith", AcademicDegreeTypes.Ing, null, address2, "Agent", "michal.zacpal@uhk.cz", 388388388, new Date());

        Tasks tasks1 = new Tasks(user1,"Nakup", new Date(), new Date(), "Koupit rohlik a maslo", MyPriorityType.VERY_HIGH, false);
        Tasks tasks2 = new Tasks(user1,"Cviceni", new Date(), new Date(), "Makat jako divej", MyPriorityType.MEDIUM, true);

        Meetings meetings1 = new Meetings(user1,"Milion baby",new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2018-02-01"), "Vydelame chechtaky", "Palawan", false);
        Meetings meetings2 = new Meetings(user1,"Hell Yeah",new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"), new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"), "Obcas to tak chodi", "HK", false);
        Meetings meetings3 = new Meetings(user1,"Here is not Hero", new Date(), new Date(), "Neni vse jak vypada Neni vse jak vypada Neni vse jak vypada Neni vse jse jak vypada ", "NY", false);

        TaskContacts taskContacts1 = new TaskContacts(tasks1, contact1, user1);

        MeetingContacts meetingContacts1 = new MeetingContacts(meetings1,contact1, user1);
        MeetingContacts meetingContacts2 = new MeetingContacts(meetings1,contact2, user1);
        MeetingContacts meetingContacts3 = new MeetingContacts(meetings3,contact2, user1);

        ContactNotes contactNotes1 = new ContactNotes(contact1, "lala la la la laaaaaa", new SimpleDateFormat("yyyy-MM-dd").parse("2019-10-15"), user1);
        ContactNotes contactNotes2 = new ContactNotes(contact1, "kolo ***** rovno jak se to rymuje", new SimpleDateFormat("yyyy-MM-dd").parse("2018-11-08"), user1);
        ContactNotes contactNotes3 = new ContactNotes(contact1, "poznamka o nicem", new SimpleDateFormat("yyyy-MM-dd").parse("2007-01-21"), user1);

        ContactHistory contactHistory1 = new ContactHistory(contact1, new SimpleDateFormat("yyyy-MM-dd HH:mm").parse("2018-02-01 22:30"), user1);
        ContactHistory contactHistory2 = new ContactHistory(contact2, new Date(), user1);

        Files file1 = new Files(user1, "a", "text", 21, "a.txt", new Date(), StorageType.DOC);
        Files file2 = new Files(user1, "obj_popis", "obrazek", 100, "D:\\IdeaProjects\\CRM\\crm\\users_files\\R4Nd0m69\\doc\\obj_popis.txt", new Date(), StorageType.IMG);

        ContactFiles contactFiles1 = new ContactFiles(file1, contact1, user1);
        ContactFiles contactFiles2 = new ContactFiles(file2, contact1, user1);

        Company company1 = new Company(user1, "Xlaab", address1, "email@email.com", 555666999, null);
        Company company2 = new Company(user1, "Piskoviste", address2, "piskoviste@email.com", 555666999, null);

        CompanyHistory companyHistory1 = new CompanyHistory(company1, user1, new Date());
        CompanyHistory companyHistory2 = new CompanyHistory(company2, user1, new Date());

        CompanyContacts companyContacts1 = new CompanyContacts(company1, contact1, user1);
        CompanyContacts companyContacts2 = new CompanyContacts(company1, contact2, user1);

        addressesRepository.save(address1);
        addressesRepository.save(address2);

        contact1 = contactsRepository.save(contact1);
        contact2 = contactsRepository.save(contact2);
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
        contactHistoryRepository.save(contactHistory1);
        contactHistoryRepository.save(contactHistory2);
        filesRepository.save(file1);
        filesRepository.save(file2);
        contactFilesRepository.save(contactFiles1);
        contactFilesRepository.save(contactFiles2);
        company1 = companyRepository.save(company1);
        company2 = companyRepository.save(company2);
        contact1.setCompany(company1);
        contact2.setCompany(company1);
        contactsRepository.save(contact1);
        contactsRepository.save(contact2);
        companyHistoryRepository.save(companyHistory1);
        companyHistoryRepository.save(companyHistory2);
        companyContactsRepository.save(companyContacts1);
        companyContactsRepository.save(companyContacts2);

    }
}
