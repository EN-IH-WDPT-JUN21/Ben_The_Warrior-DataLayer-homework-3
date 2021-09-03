package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import com.ironhack.homework3.dao.classes.SalesRep;
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
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test",
        "spring.datasource.driverClassName=org.h2.Driver",
        "spring.h2.console.enabled=true",
        "spring.datasource.username=sa",
        "spring.datasource.password=sa",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.show-sql=true",
        "spring.jpa.hibernate.ddl-auto=create"
})
class LeadRepositoryTest {

    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    private LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
        var sr = new SalesRep("Sales Guy");
        salesRepRepository.save(sr);
        var l1 = new Lead("Joe", "999999999", "joe@mail.com", "New Company", sr);
        leadRepository.save(l1);
        var l2 = new Lead("Anna", "888888888", "anna@mail.com", "Another Company");
        leadRepository.save(l2);
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
    }


    // ============================== POJO Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        var sr = new SalesRep("Sales Girl");
        salesRepRepository.save(sr);
        Lead l = new Lead("John Smith", "2460247246", "johnthewarrior@fighters.com", "The Smiths", sr);
        leadRepository.save(l);
        assertEquals("Id: 3, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The Smiths, Sales Representative: Sales Girl", l.toString());
    }

    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, leadRepository.count());
    }

    // ==================== Create ====================
//    @Test
//    void saveANewLead() {
//        var LeadCountBeforeSave = leadRepository.count();
//        var lead = new Lead(100, "Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        leadRepository.save(lead);
//        var LeadCountAfterSave = leadRepository.count();
//        assertEquals(1, LeadCountAfterSave - LeadCountBeforeSave);
//        leadRepository.delete(lead);
//    }

    @Test
    @Order(3)
    void testCreateLead_addNewLead_savedInRepository() {
        var initialSize = leadRepository.count();
        leadRepository.save(new Lead("Robin Smith", "25782482", "robin@fighters.com", "The Smiths"));
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
        var l1 = new Lead("Sara Smith", "395935793579", "sara@fighters.com", "The Smiths");
        leadRepository.save(l1);
        var storedLead = leadRepository.findById(3);
        if (storedLead.isPresent()) {
            assertEquals(3, storedLead.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateLead_changePhoneNr_newPhoneNrEqualsDefinedPhoneNr() {
        var l1 = new Lead("Arthur Smith", "579749679", "arthur@fighters.com", "The Smiths");
        leadRepository.save(l1);
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
        var l1 = new Lead("Christina Smith", "3333333333", "christina@fighters.com", "The Smiths");
        leadRepository.save(l1);
        var initialSize = leadRepository.count();
        leadRepository.deleteById(3);
        assertEquals(initialSize - 1, leadRepository.count());
    }


    // ============================== Relations Testing ==============================
    // ==================== with SalesRep ====================
    @Test
    @Order(7)
    void testCheckForSalesRep() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var l1 = new Lead("New Lead", "999999999", "newemail@mail.com", "CN");
        l1.setSalesRep(sr1);
        leadRepository.save(l1);

        var storedLead = leadRepository.findByIdJoinedSalesRep(3);
        if (storedLead.isPresent()) {
            assertEquals("New Sales Girl", storedLead.get().getSalesRep().getName());
        } else throw new TestInstantiationException("Id not found");
    }


}
