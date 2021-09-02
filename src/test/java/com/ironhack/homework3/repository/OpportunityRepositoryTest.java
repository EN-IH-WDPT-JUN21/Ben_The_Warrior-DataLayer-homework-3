package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class OpportunityRepositoryTest {

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
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
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(100);
        contactRepository.save(contact);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN);
        opportunityRepository.save(opportunity);
        var OpportunityCountAfterSave = opportunityRepository.count();
        assertEquals(1, OpportunityCountAfterSave - OpportunityCountBeforeSave);

    }
}