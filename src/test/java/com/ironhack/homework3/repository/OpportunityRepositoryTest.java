package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.classes.SalesRep;
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
class OpportunityRepositoryTest {

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SalesRepRepository salesRepRepository;

    @BeforeEach
    void setUp() {
        var a = new Account(Industry.MANUFACTURING, 100, "Barcelona", "Spain");
        accountRepository.save(a);
        var c = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a);
        contactRepository.save(c);
        var sr = new SalesRep("Sales Guy 1");
        salesRepRepository.save(sr);
        var o1 = new Opportunity(Product.BOX, 200, c, Status.CLOSED_WON, a, sr);
        opportunityRepository.save(o1);
        var o2 = new Opportunity(Product.HYBRID, 32, Status.CLOSED_LOST);
        opportunityRepository.save(o2);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        salesRepRepository.deleteAll();
        accountRepository.deleteAll();
    }


    // ============================== POJO Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        var c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The Smiths");
        contactRepository.save(c);
        var sr = new SalesRep("Sales Girl");
        salesRepRepository.save(sr);
        Opportunity o1 = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN);
        o1.setSalesRep(sr);
        opportunityRepository.save(o1);
        assertEquals("Id: 3, Product: HYBRID, Quantity: 30000, Decision Maker: John Smith, Sales Representative: Sales Girl, Status: OPEN", o1.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, opportunityRepository.count());
    }

    // ==================== Create ====================
//    @Test
//    void saveANewContact() {
//        var OpportunityCountBeforeSave = opportunityRepository.count();
//        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        contact.setId(100);
//        contactRepository.save(contact);
//        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN);
//        opportunityRepository.save(opportunity);
//        var OpportunityCountAfterSave = opportunityRepository.count();
//        assertEquals(1, OpportunityCountAfterSave - OpportunityCountBeforeSave);
//    }

    @Test
    @Order(3)
    void testCreateOpportunity_addNewOpportunity_savedInRepository() {
        var initialSize = opportunityRepository.count();
        opportunityRepository.save(new Opportunity(Product.FLATBED, 11, Status.OPEN));
        assertEquals(initialSize + 1, opportunityRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadOpportunity_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = opportunityRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadOpportunity_findById_returnsObjectsWithId() {
        var o1 = new Opportunity(Product.HYBRID, 1, Status.OPEN);
        opportunityRepository.save(o1);
        var storedOpportunity = opportunityRepository.findById(3);
        if (storedOpportunity.isPresent()) {
            assertEquals(3, storedOpportunity.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateOpportunity_changeQuantity_newQuantityEqualsDefinedQuantity() {
        var o1 = new Opportunity(Product.BOX, 100, Status.OPEN);
        opportunityRepository.save(o1);
        var storedOpportunity = opportunityRepository.findById(3);
        if (storedOpportunity.isPresent()) {
            storedOpportunity.get().setQuantity(120);
            opportunityRepository.save(storedOpportunity.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredOpportunity = opportunityRepository.findById(3);
        if (updatedStoredOpportunity.isPresent()) {
            assertEquals(120, updatedStoredOpportunity.get().getQuantity());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteOpportunity_deleteOpportunity_deletedFromRepository() {
        var o1 = new Opportunity(Product.BOX, 2000, Status.CLOSED_LOST);
        opportunityRepository.save(o1);
        var initialSize = opportunityRepository.count();
        opportunityRepository.deleteById(3);
        assertEquals(initialSize - 1, opportunityRepository.count());
    }


    // ============================== Relations Testing ==============================
    // ==================== with Account ====================
    @Test
    @Order(7)
    void testCheckForAccount() {
        var a = new Account(Industry.PRODUCE, 40, "Rome", "Italy");
        accountRepository.save(a);
        var o1 = new Opportunity(Product.BOX, 100, Status.OPEN);
        o1.setAccountOpp(a);
        opportunityRepository.save(o1);

        var storedOpportunity = opportunityRepository.findByIdJoinedAccount(3);
        if (storedOpportunity.isPresent()) {
            assertEquals("Italy", storedOpportunity.get().getAccountOpp().getCountry());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with Contact ====================
    @Test
    @Order(7)
    void testCheckForOpportunity() {
        var c = new Contact("New Contact", "999999999", "newemail@mail.com", "CN");
        contactRepository.save(c);
        var o1 = new Opportunity(Product.HYBRID, 9, c, Status.OPEN);
        opportunityRepository.save(o1);

        var storedOpportunity = opportunityRepository.findByIdJoinedContact(3);
        if (storedOpportunity.isPresent()) {
            assertEquals("New Contact", storedOpportunity.get().getDecisionMaker().getName());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with SalesRep ====================
    @Test
    @Order(7)
    void testCheckForOpportunities() {
        var sr = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr);
        var o1 = new Opportunity(Product.HYBRID, 6, Status.OPEN);
        o1.setSalesRep(sr);
        opportunityRepository.save(o1);

        var storedOpportunity = opportunityRepository.findByIdJoinedSalesRep(3);
        if (storedOpportunity.isPresent()) {
            assertEquals("New Sales Girl", storedOpportunity.get().getSalesRep().getName());
        } else throw new TestInstantiationException("Id not found");
    }


    // ============================== Custom Queries Testing ==============================


}
