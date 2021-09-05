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
class SalesRepRepositoryTest {

    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        var sr1 = new SalesRep("Sales Guy 1");
        salesRepRepository.save(sr1);
        var sr2 = new SalesRep("Sales Guy 2");
        salesRepRepository.save(sr2);
    }

    @AfterEach
    void tearDown() {
        leadRepository.deleteAll();
        opportunityRepository.deleteAll();
        salesRepRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }


    // ============================== POJO Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var sr = new SalesRep("New Sales Guy");
        salesRepRepository.save(sr);
        assertEquals("Id: 3, Name: New Sales Guy", sr.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, salesRepRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateSalesRep_addNewSalesRep_savedInRepository() {
        var initialSize = salesRepRepository.count();
        salesRepRepository.save(new SalesRep("New Sales Girl"));
        assertEquals(initialSize + 1, salesRepRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadSalesRep_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = salesRepRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadSalesRep_findById_returnsObjectsWithId() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var storedSr = salesRepRepository.findById(3);
        if (storedSr.isPresent()) {
            assertEquals(3, storedSr.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateSalesRep_changeName_newNameEqualsDefinedName() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var storedSr = salesRepRepository.findById(3);
        if (storedSr.isPresent()) {
            storedSr.get().setName("Old Sales Girl");
            salesRepRepository.save(storedSr.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredSr = salesRepRepository.findById(3);
        if (updatedStoredSr.isPresent()) {
            assertEquals("Old Sales Girl", updatedStoredSr.get().getName());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteSalesRep_deleteSalesRep_deletedFromRepository() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var initialSize = salesRepRepository.count();
        salesRepRepository.deleteById(3);
        assertEquals(initialSize - 1, salesRepRepository.count());
    }


    // ============================== Relations Testing ==============================
    // ==================== with Leads ====================
    @Test
    @Order(7)
    void testCheckForLeads() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var l1 = new Lead("New Lead", "999999999", "newemail@mail.com", "CN");
        l1.setSalesRep(sr1);
        leadRepository.save(l1);

        var storedSr = salesRepRepository.findByIdJoinedLead(3);
        if (storedSr.isPresent()) {
            assertEquals("New Lead", storedSr.get().getLeadList().get(0).getName());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with Opportunities ====================
    @Test
    @Order(7)
    void testCheckForOpportunities() {
        var sr1 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr1);
        var a1 = new Account(Industry.MEDICAL, 350, "Rio de Janeiro", "Brazil");
        accountRepository.save(a1);
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN", a1);
        contactRepository.save(c1);
        var o1 = new Opportunity(Product.HYBRID, 6, c1, Status.OPEN, a1, sr1);
        opportunityRepository.save(o1);

        var storedSr = salesRepRepository.findByIdJoinedOpportunity(3);
        if (storedSr.isPresent()) {
            assertEquals(6, storedSr.get().getOpportunityList().get(0).getQuantity());
        } else throw new TestInstantiationException("Id not found");
    }


    // ============================== Custom Queries Testing ==============================


}
