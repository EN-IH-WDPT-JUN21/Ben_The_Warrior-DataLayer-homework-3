package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.enums.Industry;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)   // Resets DB and id generation (slower)
@TestPropertySource(properties = {      // For testing it uses a "datalayer_tests" database and the same user
        "spring.datasource.url=jdbc:mysql://localhost:3306/datalayer_test",
        "spring.datasource.username=Ben",
        "spring.datasource.password=Password-123",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "spring.datasource.initialization-mode=never"
})
class ContactRepositoryTest {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account a;

    @BeforeEach
    void setUp() {
        a = new Account(Industry.PRODUCE, 300, "Berlin", "Germany");
        accountRepository.save(a);
        var c1 = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a);
        var c2 = new Contact("Rachel", "888888888", "rachel@mail.com", "New Company", a);
        var c3 = new Contact("Anna", "777777777", "anna@mail.com", "New Company", a);
        contactRepository.saveAll(List.of(c1, c2, c3));
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        contactRepository.deleteAll();
    }


    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        var c4 = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths", a);
        contactRepository.save(c4);
        assertEquals("Id: 4, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The smiths", c4.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(3, contactRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateContact_addNewContact_savedInRepository() {
        var initialSize = contactRepository.count();
        contactRepository.save(new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", a));
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
        var c3 = new Contact("Sara Smith", "395935793579", "sara@fighters.com", "The Smiths", a);
        contactRepository.save(c3);
        var storedContact = contactRepository.findById(3);
        if (storedContact.isPresent()) {
            assertEquals(3, storedContact.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateContact_changeEmail_newEmailEqualsDefinedEmail() {
        var c3 = new Contact("Arthur Smith", "579749679", "arthur@fighters.com", "The Smiths", a);
        contactRepository.save(c3);
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
        var c3 = new Contact("Christina Smith", "3333333333", "christina@fighters.com", "The Smiths", a);
        contactRepository.save(c3);
        var initialSize = contactRepository.count();
        contactRepository.deleteById(3);
        assertEquals(initialSize - 1, contactRepository.count());
    }


}