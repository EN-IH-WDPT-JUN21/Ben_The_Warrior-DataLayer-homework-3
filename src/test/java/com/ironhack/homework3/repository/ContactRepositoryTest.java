package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Lead;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ContactRepositoryTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

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
        assertEquals("Id: null, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The smiths", c.toString());
    }

    @Test
    void saveANewContact(){
        var ContactCountBeforeSave = contactRepository.count();
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(200);
        contactRepository.save(contact);
        var ContactCountAfterSave = contactRepository.count();
        assertEquals(1, ContactCountAfterSave - ContactCountBeforeSave);
        contactRepository.delete(contact);
    }
}