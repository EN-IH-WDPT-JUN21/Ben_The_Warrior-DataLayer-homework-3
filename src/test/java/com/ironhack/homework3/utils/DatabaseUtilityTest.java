/*package com.ironhack.homework3.utils;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.main.Menu;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.repository.AccountRepository;
import com.ironhack.homework3.repository.ContactRepository;
import com.ironhack.homework3.repository.LeadRepository;
import com.ironhack.homework3.repository.OpportunityRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseUtilityTest {
//    private JsonDatabaseUtility jsonDatabaseUtility;
    private DatabaseUtility initialDatabase;

    @Autowired
    DatabaseUtility databaseUtility;

    @Autowired
    Menu menu;

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @BeforeEach
    void setUp() throws IOException {
//        jsonDatabaseUtility = new JsonDatabaseUtility(leadRepository, contactRepository, accountRepository, opportunityRepository, "dummy");
//        initialDatabase = new JsonDatabaseUtility(leadRepository, contactRepository, accountRepository, opportunityRepository, "dummy");
//        initialDatabase.load();
        databaseUtility = new DatabaseUtility(leadRepository, contactRepository, accountRepository, opportunityRepository);
        initialDatabase = new DatabaseUtility(leadRepository, contactRepository, accountRepository, opportunityRepository);
    }

    @AfterEach
    void tearDown() throws IOException {
//        initialDatabase.save();
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    void setIdForNewLeadTest() {
        int oldId = databaseUtility.getLeadId();
        int newIdSet = databaseUtility.setIdForNewLead();
        int currentId = databaseUtility.getLeadId();
        assertEquals(oldId + 1, newIdSet);
        assertEquals(newIdSet, currentId);
        databaseUtility.addLead("John", "505-098-654", "john@gmail.com", "Xerox");
        assertEquals(currentId + 1, databaseUtility.getLeadId());
    }

    @Test
    void addLeadTest() {
        int leadHashSizeBefore = databaseUtility.getLeadHash().size();
        var leadCountBefore = leadRepository.count();
        databaseUtility.addLead("Ula", "505-098-654", "ula@gmail.com", "Xerox");
        System.out.println("LeadHash: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        int leadHashSizeAfter = databaseUtility.getLeadHash().size();
        var leadCountAfter = leadRepository.count();
        assertEquals(1, leadHashSizeAfter - leadHashSizeBefore);
        assertEquals(1L, leadCountAfter - leadCountBefore);

    }

    @Test
    void removeLeadTest() {
        databaseUtility.addLead("Jane", "505-098-654", "jane@gmail.com", "Xerox");
        databaseUtility.addLead("Adam", "505-100-654", "adam@gmail.com", "Dell");
        System.out.println("Before");
        System.out.println("LeadHash: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        int leadHashSizeBefore = databaseUtility.getLeadHash().size();
        var leadCountBefore = leadRepository.count();
        databaseUtility.removeLead(2);
        int leadHashSizeAfter = databaseUtility.getLeadHash().size();
        var leadCountAfter = leadRepository.count();
        System.out.println("After");
        System.out.println("LeadHash: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        assertEquals(-1, leadHashSizeAfter - leadHashSizeBefore);
        assertEquals(-1L, leadCountAfter - leadCountBefore);
    }


    @Test
    void lookupLeadIdTest() {
        databaseUtility.addLead("Cole", "505-098-654", "cole@gmail.com", "Xerox");
        databaseUtility.addLead("Tim", "505-100-654", "tim@gmail.com", "Dell");
        var leadToFind = databaseUtility.lookupLeadId(2).toString();
        var leadToCheck = leadRepository.getById(2).toString();
        assertEquals(leadToCheck, leadToFind);
        assertEquals("Tim", databaseUtility.lookupLeadId(2).getName());

    }

    @Test
    void addContactTest() {
        int contactHashSizeBefore = databaseUtility.getContactHash().size();
        var contactCountBefore = contactRepository.count();
        databaseUtility.addLead("Mary", "505-098-654", "may@gmail.com", "Xerox");
        databaseUtility.addLead("Lue", "505-100-654", "lue@gmail.com", "Dell");
        System.out.println("Leads before: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        int leadHashSizeBefore = databaseUtility.getLeadHash().size();
        var leadCountBefore = leadRepository.count();
        databaseUtility.addContact(2);
        System.out.println("Contacts after: " + databaseUtility.getContactHash());
        System.out.println("Contact Rep: " + contactRepository.findAll());
        System.out.println("Leads after: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        int leadHashSizeAfter = databaseUtility.getLeadHash().size();
        int contactHashSizeAfter = databaseUtility.getContactHash().size();
        var leadCountAfter = leadRepository.count();
        var contactCountAfter = contactRepository.count();
        assertEquals(1, contactHashSizeAfter - contactHashSizeBefore);
        assertEquals(-1, leadHashSizeAfter - leadHashSizeBefore);
        assertEquals(1, contactCountAfter - contactCountBefore);
        assertEquals(-1, leadCountAfter - leadCountBefore);
    }

    @Test
    void addOpportunityTest() {
        int opportunityHashSizeBefore = databaseUtility.getOpportunityHash().size();
        var opportunityCountBefore = opportunityRepository.count();
        databaseUtility.addLead("Phil", "505-098-654", "phil@gmail.com", "Xerox");
        databaseUtility.addContact(1);
        Contact contact1 = databaseUtility.getContactHash().get(1);
        databaseUtility.addOpportunity(Product.HYBRID, 12, contact1);
        System.out.println("Opportunities after: " + databaseUtility.getOpportunityHash());
        System.out.println("Opportunities Rep: " + opportunityRepository.findAll());
        assertFalse(databaseUtility.getOpportunityHash().isEmpty());
        int opportunityHashSizeAfter = databaseUtility.getOpportunityHash().size();
        var opportunityCountAfter = opportunityRepository.count();
        assertEquals(1, opportunityHashSizeAfter - opportunityHashSizeBefore);
        assertEquals(1, opportunityCountAfter - opportunityCountBefore);
// second opportunity
        int opportunityHashSizeBefore1 = databaseUtility.getOpportunityHash().size();
        var opportunityCountBefore1 = opportunityCountAfter;
        databaseUtility.addLead("Fred", "505-100-654", "fred@gmail.com", "Dell");
        System.out.println("Leads after: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        System.out.println("Contacts after: " + databaseUtility.getContactHash());
        System.out.println("Contact Rep: " + contactRepository.findAll());
        databaseUtility.addContact(2);
        Contact contact2 = databaseUtility.getContactHash().get(2);
        databaseUtility.addOpportunity(Product.FLATBED, 3, contact2);
        System.out.println("Opportunities after: " + databaseUtility.getOpportunityHash());
        System.out.println("Opportunities Rep: " + opportunityRepository.findAll());
        int opportunityHashSizeAfter1 = databaseUtility.getOpportunityHash().size();
        var opportunityCountAfter1 = opportunityRepository.count();
        assertEquals(1, opportunityHashSizeAfter1 - opportunityHashSizeBefore1);
        assertEquals(1, opportunityCountAfter1 - opportunityCountBefore1);
    }

    @Test
    void addAccountTest() {
        int accountHashSizeBefore = databaseUtility.getAccountHash().size();
        var accountCountBefore = accountRepository.count();
        databaseUtility.addLead("Tom", "505-100-654", "tom@gmail.com", "Dell");
        databaseUtility.addContact(1);
        Contact contact1 = databaseUtility.getContactHash().get(1);
        databaseUtility.addOpportunity(Product.HYBRID, 12, contact1);
        Opportunity opportunity1 = databaseUtility.getOpportunityHash().get(1);
        databaseUtility.addAccount(Industry.MEDICAL, 12, "Warsaw", "Poland", contact1, opportunity1);
        System.out.println("After: ");
//        System.out.println("Account hash: " + jsonDatabaseUtility.getAccountHash());
        System.out.println("Account Rep: " + accountRepository.findAll());
        System.out.println("Contacts hash: " + databaseUtility.getContactHash());
        System.out.println("Contact Rep: " + contactRepository.findAll());
        System.out.println("Opportunities hash: " + databaseUtility.getOpportunityHash());
        System.out.println("Opportunities Rep: " + opportunityRepository.findAll());
        assertFalse(databaseUtility.getAccountHash().isEmpty());
        int accountHashSizeAfter = databaseUtility.getAccountHash().size();
        var accountCountAfter = accountRepository.count();
        assertEquals(1, accountHashSizeAfter - accountHashSizeBefore);
        assertEquals(1, accountCountAfter - accountCountBefore);
    }


    @Test
    void convertLeadTest() {
        int contactHashSizeBefore = databaseUtility.getContactHash().size();
        var contactCountBefore = contactRepository.count();
        int opportunityHashSizeBefore = databaseUtility.getOpportunityHash().size();
        var opportunityCountBefore = opportunityRepository.count();
        int accountHashSizeBefore = databaseUtility.getAccountHash().size();
        var accountCountBefore = accountRepository.count();
        databaseUtility.addLead("Walt", "505-098-654", "walt@gmail.com", "Xerox");
        databaseUtility.addLead("Ann", "505-100-654", "ann@gmail.com", "Dell");
        System.out.println("Before: ");
        System.out.println("Leads hash: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        int leadHashSizeBefore = databaseUtility.getLeadHash().size();
        var leadCountBefore = leadRepository.count();
        databaseUtility.convertLead(2, Product.BOX, 10, Industry.ECOMMERCE, 8, "Warsaw", "Poland");
        System.out.println("After: ");
        System.out.println("Contacts hash: " + databaseUtility.getContactHash());
        System.out.println("Contact Rep: " + contactRepository.findAll());
        System.out.println("Leads hash: " + databaseUtility.getLeadHash());
        System.out.println("Lead Rep: " + leadRepository.findAll());
        System.out.println("Opportunities hash: " + databaseUtility.getOpportunityHash());
        System.out.println("Opportunities Rep: " + opportunityRepository.findAll());
//        System.out.println("Accounts hash: " + jsonDatabaseUtility.getAccountHash());
        System.out.println("Account Rep: " + accountRepository.findAll());
        int leadHashSizeAfter = databaseUtility.getLeadHash().size();
        var leadCountAfter = leadRepository.count();
        int contactHashSizeAfter = databaseUtility.getContactHash().size();
        var contactCountAfter = contactRepository.count();
        int opportunityHashSizeAfter = databaseUtility.getOpportunityHash().size();
        var opportunityCountAfter = opportunityRepository.count();
        int accountHashSizeAfter = databaseUtility.getAccountHash().size();
        var accountCountAfter = accountRepository.count();
        assertEquals(1, contactHashSizeAfter - contactHashSizeBefore);
        assertEquals(1, contactCountAfter - contactCountBefore);
        assertEquals(-1, leadHashSizeAfter - leadHashSizeBefore);
        assertEquals(-1, leadCountAfter - leadCountBefore);
        assertEquals(1, opportunityHashSizeAfter - opportunityHashSizeBefore);
        assertEquals(1, opportunityCountAfter - opportunityCountBefore);
        assertEquals(1, accountHashSizeAfter - accountHashSizeBefore);
        assertEquals(1, accountCountAfter - accountCountBefore);
    }

    @Test
    void saveAndLoadJsonDatabaseInJsonFile() throws IOException {
        databaseUtility.addLead("John", "505-098-654", "john@gmail.com", "Xerox");
        databaseUtility.addContact(1);
        Contact contact1 = databaseUtility.getContactHash().get(1);
        databaseUtility.addOpportunity(Product.BOX, 2, contact1);
        Opportunity opportunity1 = databaseUtility.getOpportunityHash().get(1);
        databaseUtility.addAccount(Industry.PRODUCE, 10, "Santiago", "Chile", contact1, opportunity1);
        databaseUtility.addLead("Sara", "505-100-654", "sara@gmail.com", "Dell");
        databaseUtility.addContact(2);
        Contact contact2 = databaseUtility.getContactHash().get(2);
        databaseUtility.addOpportunity(Product.FLATBED, 50, contact2);
        Opportunity opportunity2 = databaseUtility.getOpportunityHash().get(2);
        databaseUtility.addAccount(Industry.MEDICAL, 12, "Warsaw", "Poland", contact2, opportunity2);
        databaseUtility.save();
        File file = new File("src/main/java/com/ironhack/homework_2/database/dummy.json");
        assertTrue(file.exists());

        DatabaseUtility afterSaving = new DatabaseUtility(leadRepository, contactRepository, accountRepository, opportunityRepository, "dummy");
        afterSaving.load();
        assertTrue(databaseUtility.equals(afterSaving));
    }
}*/