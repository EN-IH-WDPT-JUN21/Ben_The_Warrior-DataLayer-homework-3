package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
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
class OpportunityRepositoryTest {

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(100);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID,30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
    }

    @Test
    void testToString() {
        Contact c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        Opportunity o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN);
        assertEquals("Id: null, Product: HYBRID, Quantity: 30000, Decision Maker: John Smith, Status: OPEN", o.toString());
    }

    @Test
    void saveANewContact(){
        var OpportunityCountBeforeSave = opportunityRepository.count();
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contact.setId(101);
        contactRepository.save(contact);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON);
        opportunityRepository.save(opportunity);
        var OpportunityCountAfterSave = opportunityRepository.count();
        assertEquals(1, OpportunityCountAfterSave - OpportunityCountBeforeSave);
    }

}