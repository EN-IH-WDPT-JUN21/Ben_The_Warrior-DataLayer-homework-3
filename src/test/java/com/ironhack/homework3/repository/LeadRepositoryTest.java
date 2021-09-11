package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import com.ironhack.homework3.dao.classes.SalesRep;
import com.ironhack.homework3.dao.main.MainMenuAutowired;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

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
class LeadRepositoryTest {

    @MockBean
    private MainMenuAutowired mainMenuAutowired;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

    private SalesRep sr;

    @BeforeEach
    void setUp() {
        sr = new SalesRep("Sales Guy");
        salesRepRepository.save(sr);
        var l1 = new Lead("Joe", "999999999", "joe@mail.com", "New Company", sr);
        leadRepository.save(l1);
        var l2 = new Lead("Anna", "888888888", "anna@mail.com", "Another Company", sr);
        leadRepository.save(l2);
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
    }

    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        Lead l3 = new Lead("John Smith", "2460247246", "johnthewarrior@fighters.com", "The Smiths", sr);
        leadRepository.save(l3);
        assertEquals("Id: 3, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The Smiths", l3.toString());
    }

    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, leadRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateLead_addNewLead_savedInRepository() {
        var initialSize = leadRepository.count();
        leadRepository.save(new Lead("Robin Smith", "25782482", "robin@fighters.com", "The Smiths", sr));
        assertEquals(initialSize + 1, leadRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadLead_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = leadRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadLead_findById_returnsObjectsWithId() {
        var l3 = new Lead("Sara Smith", "395935793579", "sara@fighters.com", "The Smiths", sr);
        leadRepository.save(l3);
        var storedLead = leadRepository.findById(3);
        if (storedLead.isPresent()) {
            assertEquals(3, storedLead.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateLead_changePhoneNr_newPhoneNrEqualsDefinedPhoneNr() {
        var l3 = new Lead("Arthur Smith", "579749679", "arthur@fighters.com", "The Smiths", sr);
        leadRepository.save(l3);
        var storedLead = leadRepository.findById(3);
        if (storedLead.isPresent()) {
            storedLead.get().setPhoneNumber("91000000");
            leadRepository.save(storedLead.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredLead = leadRepository.findById(3);
        if (updatedStoredLead.isPresent()) {
            assertEquals("91000000", updatedStoredLead.get().getPhoneNumber());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteLead_deleteLead_deletedFromRepository() {
        var l3 = new Lead("Christina Smith", "3333333333", "christina@fighters.com", "The Smiths", sr);
        leadRepository.save(l3);
        var initialSize = leadRepository.count();
        leadRepository.deleteById(3);
        assertEquals(initialSize - 1, leadRepository.count());
    }


}