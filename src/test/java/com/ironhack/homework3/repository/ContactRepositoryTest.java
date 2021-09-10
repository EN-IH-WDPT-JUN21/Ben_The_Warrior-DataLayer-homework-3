package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Lead;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)   // Resets DB and id generation (slower)
//@TestPropertySource(properties = {      // For testing it uses a "datalayer_tests" database and the same user
//        "spring.datasource.url=jdbc:mysql://localhost:3306/datalayer_tests",
//        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
//        "spring.datasource.username=Ben",
//        "spring.datasource.password=Password-123",
//        "spring.jpa.hibernate.ddl-auto=create-drop",
//        "spring.datasource.initialization-mode=never"   // Doesn't initialize schema.sql
//})
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
        contactRepository.save(c);
        assertEquals("Id: 1, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The smiths", c.toString());
    }

    @Test
    void saveANewContact() {
        var ContactCountBeforeSave = contactRepository.count();
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contactRepository.save(contact);
        var ContactCountAfterSave = contactRepository.count();
        assertEquals(1, ContactCountAfterSave - ContactCountBeforeSave);
        contactRepository.delete(contact);
    }
}