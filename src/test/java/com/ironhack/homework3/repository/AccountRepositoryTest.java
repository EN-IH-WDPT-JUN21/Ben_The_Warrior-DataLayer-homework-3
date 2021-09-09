package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    Account a;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();



    }
    /*sth not working....*/

    @Test
    void testToString() {
        Contact c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        c.setId(102);
        contactRepository.save(c);
        Opportunity o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN);
        opportunityRepository.save(o);
        a = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain", List.of(c), List.of(o));
        accountRepository.save(a);
        c.setAccount(a);
        o.setAccountOpp(a);
        contactRepository.save(c);
        opportunityRepository.save(o);
        assertEquals("Id: 1, Industry: ECOMMERCE, Number of Employees: 100, City: Madrid, Country: Spain, Number of Contacts: 1, Number of Opportunities: 1", a.toString());
    }

    /*@Test
    void saveANewAccount(){
        var AccountCountBeforeSave = accountRepository.count();
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(101);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        var AccountCountAfterSave = accountRepository.count();
        contact.setAccount(account);
        contactRepository.save(contact);
        opportunity.setAccountOpp(account);
        opportunityRepository.save(opportunity);
        assertEquals(1, AccountCountAfterSave - AccountCountBeforeSave);
        assertEquals(account.getId(), contact.getAccount().getId());
        assertEquals(account.getId(), opportunity.getAccountOpp().getId());
    }*/
}