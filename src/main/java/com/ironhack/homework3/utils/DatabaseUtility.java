package com.ironhack.homework3.utils;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.dao.queryInterfaces.*;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import com.ironhack.homework3.repository.*;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Setter
public class DatabaseUtility {
    @Autowired
    private LeadRepository leadRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private OpportunityRepository opportunityRepository;
    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    public DatabaseUtility() {
    }

    //creating new lead
    public void addLead(String name, String phoneNumber, String email, String companyName, SalesRep salesRep) {
        Lead newLead = new Lead(name, phoneNumber, email, companyName, salesRep);
        var allLeads = leadRepository.findAll();
        for (var lead : allLeads) {
            if (lead.equals(newLead)) {
                return;
            }
        }
        leadRepository.save(newLead);
    }

    // ==================== Removes Lead from HashMap for Leads====================
    public void removeLead(Integer id) {
        leadRepository.deleteById(id);
    }

    // Search a lead from id. If it doesn't exist throw exception
    public Lead lookupLeadId(Integer id) {
        Optional<Lead> lead = leadRepository.findById(id);
        if (lead.isEmpty()) {
            throw new IllegalArgumentException("There is no Lead with id " + id);
        } else {
            return lead.get();
        }
    }

    // Search a opportunity from id. If it doesn't exist throw exception
    public Opportunity lookupOpportunityId(int id) {
        Optional<Opportunity> opportunity = opportunityRepository.findById(id);
        if (opportunity.isEmpty()) {
            throw new IllegalArgumentException("There is no Opportunity with id " + id);
        } else {
            return opportunity.get();
        }
    }

    // Search a contact from id. If it doesn't exist throw exception
    public Contact lookupContactId(int id) {
        Optional<Contact> contact = contactRepository.findById(id);
        if (contact.isEmpty()) {
            throw new IllegalArgumentException("There is no Contact with id " + id);
        } else {
            return contact.get();
        }
    }

    // Search a account from id. If it doesn't exist throw exception
    public Account lookupAccountId(int id) {
        Optional<Account> account = accountRepository.findById(id);
        if (account.isEmpty()) {
            throw new IllegalArgumentException("There is no Account with id " + id);
        } else {
            return account.get();
        }
    }

    // Search a salesrep from id. If it doesn't exist throw exception
    public SalesRep lookupSalesRepId(int id) {
        Optional<SalesRep> salesRep = salesRepRepository.findById(id);
        if (salesRep.isEmpty()) {
            throw new IllegalArgumentException("There is no SalesRep with id " + id);
        } else {
            return salesRep.get();
        }
    }

    // ==================== Adds new Contact to HashMap for Contacts====================

    //creating new contact (from lead)
    public int addContact(Integer id, Account account) {
        Optional<Lead> leadToConvert = leadRepository.findById(id);
        if (leadToConvert.isEmpty()) {
            throw new IllegalArgumentException("There is no lead with id " + id + "! Unable to convert");
        }
        Contact newContact = new Contact(leadToConvert.get().getName(),
                leadToConvert.get().getPhoneNumber(),
                leadToConvert.get().getEmail(),
                leadToConvert.get().getCompanyName(), account);
        contactRepository.save(newContact);
        removeLead(id);
        return newContact.getId();
    }

    //creating new opportunity
    public Opportunity addOpportunity(Product product, int quantity, Contact decisionMaker, Account account, SalesRep salesRep) {
        Opportunity newOpportunity = new Opportunity(product, quantity, decisionMaker, Status.OPEN, account, salesRep);
        opportunityRepository.save(newOpportunity);
        return newOpportunity;
    }

    //second version with creating new account
    public Account addAccount(Industry industry, int employeeCount, String city, String country) {
        Account newAccount = new Account(industry, employeeCount, city, country);
        accountRepository.save(newAccount);
        return newAccount;
    }

    // ==================== Converts Lead -> calls: addOpportunity, addAccount, addContact, removeLead====================
    public void convertLead(Integer id, Product product, int quantity, Industry industry, int employeeCount, String city, String country) {
        Account account = addAccount(industry, employeeCount, city, country);
        SalesRep salesRep = getLeadById(id).getSalesRep();
        try {
            id = addContact(id, account);
            Optional<Contact> decisionMaker = contactRepository.findById(id);

            if (decisionMaker.isPresent()){
                Opportunity newOpportunity = addOpportunity(product, quantity, decisionMaker.get(), account, salesRep);
            }else {
                PrinterMenu.setWarning("Something went wrong, Lead converted but Contacted unable to be fetched!");
            }
        } catch (IllegalArgumentException e) {
            PrinterMenu.setWarning(e.getMessage());
        }
    }

    // Convert lead with existing account
    public void convertLead(Integer id, Product product, int quantity, int accountId) {
        Optional<Account> account = accountRepository.findById(accountId);
        SalesRep salesRep = getLeadById(id).getSalesRep();
        if (account.isPresent()){
            try {
                id = addContact(id, account.get());
                Optional<Contact> decisionMaker = contactRepository.findById(id);
                if (decisionMaker.isPresent()){
                    Opportunity newOpportunity = addOpportunity(product, quantity, decisionMaker.get(), account.get(), salesRep);
                } else {
                    PrinterMenu.setWarning("Something went wrong, Lead converted but Contacted unable to be fetched!");
                }
            } catch (IllegalArgumentException e) {
                PrinterMenu.setWarning(e.getMessage());
            }
        } else {
            PrinterMenu.setWarning("Account with id " + accountId + " was not found!");
        }
//        Contact decisionMaker = contactRepository.getById(id);
    }


    public SalesRep addSalesRep(String name){
        SalesRep salesRep = new SalesRep(name);
        salesRepRepository.save(salesRep);
        return salesRep;
    }

    // Method to check if a lead exists with a specific id
    public boolean hasLead(int id) {
        return leadRepository.findById(id).isPresent();
    }

    // Method to check if a contact exists with a specific id
    public boolean hasContact(int id) {
        return contactRepository.findById(id).isPresent();
    }

    // Method to check if a account exists with a specific id
    public boolean hasAccount(int id) {
        return accountRepository.findById(id).isPresent();
    }

    // Method to check if a opportunity exists with a specific id
    public boolean hasOpportunity(int id) {
        return opportunityRepository.findById(id).isPresent();
    }

    //Method to check if a salesRep exists with a specific id
    public boolean hasSalesRep(int id) {
        return salesRepRepository.findById(id).isPresent();
    }

    public Lead getLeadById(int id) {
        return leadRepository.findById(id).orElse(null);
    }

    public Account getAccountById(int id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Opportunity getOpportunityById(int id) { return opportunityRepository.findById(id).orElse(null); }

    public SalesRep getSalesRepById(int id) {
        return salesRepRepository.findById(id).orElse(null);
    }

    public List<Lead> getAllLeads(){
        return leadRepository.findAll();
    }

    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }

    public List<Opportunity> getAllOpportunities() {
        return opportunityRepository.findAll();
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<SalesRep> getAllSalesRep() {
        return salesRepRepository.findAll();
    }

    // ==================== QUERY METHODS ====================
    // BY SALESREP

    public List<ICountBySalesRep> getLeadsCountBySalesRep(){
        return salesRepRepository.countLeadsBySalesRep();
    }
    public List<ICountBySalesRep> getOpportunitiesCountBySalesRep(){
        return salesRepRepository.countOpportunitiesBySalesRep();
    }
    public List<ICountBySalesRep> getOpportunitiesCountBySalesRep_With_ClosedWonStatus(){
        return salesRepRepository.countOpportunitiesByClosedWonBySalesRep();
    }
    public List<ICountBySalesRep> getOpportunitiesCountBySalesRep_With_ClosedLostStatus(){
        return salesRepRepository.countOpportunitiesByClosedLostBySalesRep();
    }
    public List<ICountBySalesRep> getOpportunitiesCountBySalesRep_With_OpenStatus(){
        return salesRepRepository.countOpportunitiesByOpenBySalesRep();
    }

    // BY PRODUCT

    public List<IOpportunityProduct>  getCountByProduct(){
        return opportunityRepository.countOpportunitiesByProduct();
    }
    public List<IOpportunityProduct> getCountByProduct_With_ClosedWonStatus(){
        return opportunityRepository.countOpportunitiesClosedWonByProduct();
    }
    public List<IOpportunityProduct> getCountByProduct_With_ClosedLostStatus(){
        return opportunityRepository.countOpportunitiesClosedLostByProduct();
    }
    public List<IOpportunityProduct> getCountByProduct_With_OpenStatus(){
        return opportunityRepository.countOpportunitiesOpenByProduct();
    }

    // BY COUNTRY
    public List<IOpportunityCountryOrCityCount> getCountByCountry() {
        return accountRepository.countByCountry();
    }
    public List<IOpportunityCountryOrCityCount> getCountClosedWonByCountry() {
        return accountRepository.countClosedWonByCountry();
    }
    public List<IOpportunityCountryOrCityCount> getCountClosedLostByCountry() {
        return accountRepository.countClosedLostByCountry();
    }
    public List<IOpportunityCountryOrCityCount> getCountOpenByCountry() {
        return accountRepository.countOpenByCountry();
    }

    // BY CITY
    public List<IOpportunityCountryOrCityCount> getCountByCity() {
        return accountRepository.countByCity();
    }
    public List<IOpportunityCountryOrCityCount> getCountClosedWonByCity() {
        return accountRepository.countClosedWonByCity();
    }
    public List<IOpportunityCountryOrCityCount> getCountClosedLostByCity() {
        return accountRepository.countClosedLostByCity();
    }
    public List<IOpportunityCountryOrCityCount> getCountOpenByCity() {
        return accountRepository.countOpenByCity();
    }

    // BY INDUSTRY
    public List<IOpportunityIndustryCount> getCountByIndustry() {
            return accountRepository.countByIndustry();
    }
    public List<IOpportunityIndustryCount> getCountClosedWonByIndustry() {
            return accountRepository.countClosedWonByIndustry();
    }
    public List<IOpportunityIndustryCount> getCountClosedLostByIndustry() {
            return accountRepository.countClosedLostByIndustry();
    }
    public List<IOpportunityIndustryCount> getCountOpenByIndustry() {
            return accountRepository.countOpenByIndustry();
    }

    // EMPLOYEECOUNT STATES
    public double getMeanEmployeeCount() {
        try {
            return accountRepository.meanEmployeeCount();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0.0;
        }
    }
    public double getMedianEmployeeCount() {
        try {
            return Utils.getMedianValue(accountRepository.orderedListOfEmployeeCount());
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0.0;
        }
    }
    public int getMaxEmployeeCount() {
        try {
            return accountRepository.maxEmployeeCount();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0;
        }
    }
    public int getMinEmployeeCount() {
        try {
            return accountRepository.minEmployeeCount();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0;
        }
    }

    // QUANTITY STATES
    public double getMeanQuantity() {
        try {
            return opportunityRepository.meanQuantity();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, opportunities might not be present!");
            return 0.0;
        }
    }
    public double getMedianQuantity() {
        try {
            return Utils.getMedianValue(opportunityRepository.orderedListOfQuantities());
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, opportunities might not be present!");
            return 0.0;
        }
    }
    public int getMaxQuantity() {
        try {
            return opportunityRepository.maxQuantity();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, opportunities might not be present!");
            return 0;
        }
    }
    public int getMinQuantity() {
        try {
            return opportunityRepository.minQuantity();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, opportunities might not be present!");
            return 0;
        }
    }

    // OPPORTUNITY STATES
    public double getMeanOppsPerAccount() {
        try {
            return accountRepository.meanOpportunities();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0.0;
        }
    }

    public double getMedianOppsPerAccount() {
        try {
            return Utils.getMedianValue(accountRepository.orderListOfOpportunities());
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0.0;
        }
    }

    public int getMaxOppsPerAccount() {
        try {
            return accountRepository.maxOpportunities();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0;
        }
    }

    public int getMinOppsPerAccount() {
        try {
            return accountRepository.minOpportunities();
        } catch (Exception e) {
            PrinterMenu.setWarning("Something went wrong, accounts might not be present!");
            return 0;
        }
    }


    public void setOpportunityStatus(int id, Status status) {
        Optional<Opportunity> opportunity = opportunityRepository.findById(id);
        if (opportunity.isPresent()){
            opportunity.get().setStatus(status);
            opportunityRepository.save(opportunity.get());
        } else {
            throw new IllegalArgumentException("There is no Opportunity with id " + id);
        }
    }
}





