package com.michal.crm.service;

import com.michal.crm.dao.*;
import com.michal.crm.model.*;
import com.michal.crm.model.summaries.CompanyContacts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private UserService userService;
    @Autowired
    private CompanyRepository companyRepository;
    @Autowired
    private CompanyHistoryRepository companyHistoryRepository;
    @Autowired
    private AddressesRepository addressesRepository;
    @Autowired
    private FilesRepository filesRepository;
    @Autowired
    private CompanyContactsRepository companyContactsRepository;
    @Autowired
    private ContactsRepository contactsRepository;

    public Company getCompanyById(int companyId) {
        return companyRepository.findByIdAndUser(companyId, userService.getLoggedUser());
    }

    public int addCompany(Company company) {
        Addresses address = company.getAddress();
        Users user = userService.getLoggedUser();
        address.setUser(user);
        address = addressesRepository.save(address);
        company.setAddress(address);
        company.setUser(user);
        company = companyRepository.save(company);
        return company.getId();
    }

    public int editCompany(Company company) {
        Company oldCompany = getCompanyById(company.getId());
        company.getAddress().setId(oldCompany.getAddress().getId());
        company.setImage(oldCompany.getImage());
        company.setUser(oldCompany.getUser());
        companyRepository.save(company);
        return company.getId();
    }

    public void deleteCompany(int compId) {
        Company company = getCompanyById(compId);
        addressesRepository.delete(company.getAddress());
        deleteAllCompanyHistory(company);
        deleteAllCompanyContacts(company);
        if (company.getImage() != null) {
            filesRepository.delete(company.getImage());
        }
        companyRepository.delete(company);
    }

    public List<Contacts> getCompanyContacts(Company company) {
        List<CompanyContacts> companyContactsList = companyContactsRepository.findCompanyContactsByCompanyAndUser(company, userService.getLoggedUser());
        List<Contacts> companyList = new ArrayList<>();
        for (CompanyContacts compCont : companyContactsList
        ) {
            companyList.add(compCont.getContact());
        }
        return companyList;
    }

    public void deleteCompanyContact(int compId, int contId) {
        Users user = userService.getLoggedUser();
        Company company = companyRepository.findByIdAndUser(compId, user);
        Contacts contact = contactsRepository.findContactsByIdAndUser(contId, user);
        List<CompanyContacts> companyContacts = companyContactsRepository.findCompanyContactsByContactAndCompanyAndUser(contact, company, user);
        contact.setCompany(null);
        companyContactsRepository.deleteAll(companyContacts);
        contactsRepository.save(contact);
    }

    public List<Company> searchCompany(String name) {
        return companyRepository.getCompanyByName(name, userService.getLoggedUser());
    }

    public List<CompanyHistory> getCompanyHistory() {
        Users user = userService.getLoggedUser();
        return companyHistoryRepository.findCompanyHistoriesByUserOrderByCreateDateDesc(user);
    }

    public void addContactToCompany(int compId, int contId) {
        Users user = userService.getLoggedUser();
        Company company = companyRepository.findByIdAndUser(compId, user);
        Contacts contact = contactsRepository.findContactsByIdAndUser(contId, user);
        if (contact.getCompany() != null) {
            List<CompanyContacts> oldComCont = companyContactsRepository.findCompanyContactsByContactAndCompanyAndUser(contact, contact.getCompany(), user);
            companyContactsRepository.deleteAll(oldComCont);
        }
        contact.setCompany(company);
        CompanyContacts companyContacts = new CompanyContacts(company, contact, user);
        contactsRepository.save(contact);
        companyContactsRepository.save(companyContacts);
    }

    public void writeHistory(Company company) {
        List<CompanyHistory> companyHistoryList = getCompanyHistory();

        for (CompanyHistory history : companyHistoryList
        ) {
            if (company == history.getCompany()) {
                companyHistoryRepository.delete(history);
            }
        }

        CompanyHistory history = new CompanyHistory();
        history.setUser(userService.getLoggedUser());
        history.setCompany(company);
        companyHistoryRepository.save(history);
    }

    public void saveNewImage(Files file, int compId) {
        Company company = getCompanyById(compId);
        company.setImage(file);
        companyRepository.save(company);
    }

    public List<Company> getTopTen() {
        return companyRepository.findTop10ByUser(userService.getLoggedUser());
    }

    private void deleteAllCompanyHistory(Company company) {
        List<CompanyHistory> companyHistoryList = companyHistoryRepository.findCompanyHistoriesByCompanyAndUser(company, userService.getLoggedUser());
        if (!companyHistoryList.isEmpty()) {
            companyHistoryRepository.deleteAll(companyHistoryList);
        }
    }

    private void deleteAllCompanyContacts(Company company) {
        List<CompanyContacts> companyContactsList = companyContactsRepository.findCompanyContactsByCompanyAndUser(company, userService.getLoggedUser());
        companyContactsRepository.deleteAll(companyContactsList);
    }
}
