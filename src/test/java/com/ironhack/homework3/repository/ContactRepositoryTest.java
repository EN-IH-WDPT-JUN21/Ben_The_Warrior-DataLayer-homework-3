package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //Resets DB and ids, but is slower
@TestPropertySource(properties = {      // For testing it uses a "datalayer_test" database
        "spring.datasource.url=jdbc:mysql://localhost:3306/datalayer_test",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=test",
        "spring.datasource.password=Test-123",
        "spring.jpa.hibernate.ddl-auto=create"
})
class ContactRepositoryTest {

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    SalesRepRepository salesRepRepository;

    @Autowired
    AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        var a = new Account(Industry.MANUFACTURING, 100, "Barcelona", "Spain");
        accountRepository.save(a);
        var c1 = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a);
        contactRepository.save(c1);
        var c2 = new Contact("Anna", "888888888", "anna@mail.com", "Another Company");
        contactRepository.save(c2);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        salesRepRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }


    // ============================== POJO Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        var c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        contactRepository.save(c);
        assertEquals("Id: 3, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The smiths", c.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, contactRepository.count());
    }

    // ==================== Create ====================
//    @Test
//    void saveANewContact() {
//        var ContactCountBeforeSave = contactRepository.count();
//        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        contact.setId(200);
//        contactRepository.save(contact);
//        var ContactCountAfterSave = contactRepository.count();
//        assertEquals(1, ContactCountAfterSave - ContactCountBeforeSave);
//        contactRepository.delete(contact);
//    }

    @Test
    @Order(3)
    void testCreateContact_addNewContact_savedInRepository() {
        var initialSize = contactRepository.count();
        contactRepository.save(new Contact("Robin Smith", "25782482", "robin@fighters.com", "The Smiths"));
        assertEquals(initialSize + 1, contactRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadContact_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = contactRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadContact_findById_returnsObjectsWithId() {
        var c1 = new Contact("Sara Smith", "395935793579", "sara@fighters.com", "The Smiths");
        contactRepository.save(c1);
        var storedContact = contactRepository.findById(3);
        if (storedContact.isPresent()) {
            assertEquals(3, storedContact.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateContact_changeEmail_newEmailEqualsDefinedEmail() {
        var c1 = new Contact("Arthur Smith", "579749679", "arthur@fighters.com", "The Smiths");
        contactRepository.save(c1);
        var storedContact = contactRepository.findById(3);
        if (storedContact.isPresent()) {
            storedContact.get().setEmail("arthur_smith@fighters.com");
            contactRepository.save(storedContact.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredContact = contactRepository.findById(3);
        if (updatedStoredContact.isPresent()) {
            assertEquals("arthur_smith@fighters.com", updatedStoredContact.get().getEmail());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteContact_deleteContact_deletedFromRepository() {
        var c1 = new Contact("Christina Smith", "3333333333", "christina@fighters.com", "The Smiths");
        contactRepository.save(c1);
        var initialSize = contactRepository.count();
        contactRepository.deleteById(3);
        assertEquals(initialSize - 1, contactRepository.count());
    }


    // ============================== Relations Testing ==============================
    // ==================== with Account ====================
    @Test
    @Order(7)
    void testCheckForAccount() {
        var a = new Account(Industry.PRODUCE, 49, "Rome", "Italy");
        accountRepository.save(a);
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN");
        c1.setAccount(a);
        contactRepository.save(c1);

        var storedContact = contactRepository.findByIdJoinedAccount(3);
        if (storedContact.isPresent()) {
            assertEquals("Rome", storedContact.get().getAccount().getCity());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with Opportunity ====================
    @Test
    @Order(7)
    void testCheckForOpportunity() {
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN");
        contactRepository.save(c1);
        var o = new Opportunity(Product.HYBRID, 9, c1, Status.OPEN);
        opportunityRepository.save(o);

        var storedContact = contactRepository.findByIdJoinedOpportunity(3);
        if (storedContact.isPresent()) {
            assertEquals(Status.OPEN, storedContact.get().getOpportunity().getStatus());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with SalesRep ==================== (TODO JA - Might not be needed)
    @Test
    @Order(7)
    void testCheckForSalesRep() {
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN");
        contactRepository.save(c1);
        var sr = new SalesRep("Sales Person");
        salesRepRepository.save(sr);
        var o = new Opportunity(Product.HYBRID, 9, c1, Status.OPEN);
        o.setSalesRep(sr);
        opportunityRepository.save(o);

        var storedContact = contactRepository.findByIdJoinedOpportunitySalesRep(3);
        if (storedContact.isPresent()) {
            assertEquals("Sales Person", storedContact.get().getOpportunity().getSalesRep().getName());
        } else throw new TestInstantiationException("Id not found");
    }


    // ============================== Custom Queries Testing ==============================


}
