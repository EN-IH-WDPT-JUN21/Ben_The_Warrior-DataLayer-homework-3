package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.dao.main.MainMenuAutowired;
import com.ironhack.homework3.dao.queryInterfaces.ILeadsCountBySalesRep;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountBySalesRep;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.TestInstantiationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
class SalesRepRepositoryTest {

    @MockBean
    private MainMenuAutowired mainMenuAutowired;

    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    private Account a;
    private Contact c;
    private Opportunity o;

    @BeforeEach
    void setUp() {
        var sr1 = new SalesRep("Person 1");
        var sr2 = new SalesRep("Sam");
        salesRepRepository.saveAll(List.of(sr1, sr2));
        var l1 = new Lead(1, "Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", sr1);
        var l2 = new Lead(2, "John", "999999942", "John@gmail.com", "John Spa", sr2);
        var l3 = new Lead(3, "a", "999999974", "a@gmail.com", "a", sr2);
        leadRepository.saveAll(List.of(l1, l2, l3));
        a = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(a);
        c = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", a);
        contactRepository.save(c);
        o = new Opportunity(Product.HYBRID, 3000, c, Status.OPEN, a, sr1);
        opportunityRepository.save(o);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var sr3 = new SalesRep("New Sales Person");
        salesRepRepository.save(sr3);
        assertEquals("Id: 3, Name: New Sales Person", sr3.toString());
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
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
        var storedSr = salesRepRepository.findById(3);
        if (storedSr.isPresent()) {
            assertEquals(3, storedSr.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateSalesRep_changeName_newNameEqualsDefinedName() {
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
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
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
        var initialSize = salesRepRepository.count();
        salesRepRepository.deleteById(3);
        assertEquals(initialSize - 1, salesRepRepository.count());
    }


    // ============================== Custom Queries Testing ==============================
    // ==================== 1 - Reporting Leads By SalesRep ====================
    @Test
    @Order(7)
    void getLeadsCountBySalesRep() {
        List<ILeadsCountBySalesRep> leadsCounts = salesRepRepository.countLeadsBySalesRep();
        assertEquals(2, (leadsCounts.size()));
        assertEquals("Sam", leadsCounts.get(0).getSalesRepName());
        assertEquals(2, leadsCounts.get(0).getLeadsCount());
        assertEquals("Person 1", leadsCounts.get(1).getSalesRepName());
        assertEquals(1, leadsCounts.get(1).getLeadsCount());
    }

    // ==================== 2 - Reporting Opportunities By SalesRep ====================
    @Test
    @Order(7)
    void getOpportunitiesCountBySalesRep() {
        List<IOpportunityCountBySalesRep> opportunitiesCounts = salesRepRepository.countOpportunitiesBySalesRep();
        assertEquals(1, opportunitiesCounts.get(0).getOpportunitiesCount());
        assertEquals("Person 1", opportunitiesCounts.get(0).getSalesRepName());
    }

    // ==================== 3 - Reporting Opportunities By SalesRep With Status CLOSED_WON ====================
    @Test
    @Order(7)
    void getOpportunitiesCountBySalesRep_With_ClosedWonStatus() {
        var salesRep = new SalesRep("person");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("lala", "124578654", "a@gm.com", "lolo",a);
        contactRepository.save(contact1);
        var account1 = new Account(Industry.ECOMMERCE, 10, "Montevideo", "Uruguay");
        accountRepository.save(account1);
        var opportunity1 = new Opportunity(Product.HYBRID, 3000, contact1, Status.CLOSED_WON, account1, salesRep);
        opportunityRepository.save(opportunity1);

        List<IOpportunityCountBySalesRep> opportunitiesCounts = salesRepRepository.countOpportunitiesByClosedWonBySalesRep();
        assertEquals(1, opportunitiesCounts.get(0).getOpportunitiesCount());
        assertEquals("person", opportunitiesCounts.get(0).getSalesRepName());
    }

    // ==================== 4 - Reporting Opportunities By SalesRep With Status CLOSED_LOST ====================
    @Test
    @Order(7)
    void getOpportunitiesCountBySalesRep_With_ClosedLostStatus() {
        var salesRep = new SalesRep("Humano");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("alguien", "124579999", "al@gm.com", "alguna",a);
        contactRepository.save(contact1);
        var account1 = new Account(Industry.ECOMMERCE, 74, "Mendoza", "Argentina");
        accountRepository.save(account1);
        var opportunity1 = new Opportunity(Product.HYBRID, 3, contact1, Status.CLOSED_LOST, account1, salesRep);
        opportunityRepository.save(opportunity1);

        List<IOpportunityCountBySalesRep> opportunitiesCount = salesRepRepository.countOpportunitiesByClosedLostBySalesRep();
        assertEquals(1, opportunitiesCount.get(0).getOpportunitiesCount());
        assertEquals("Humano", opportunitiesCount.get(0).getSalesRepName());
    }

    // ==================== 5 - Reporting Opportunities By SalesRep With Status OPEN ====================
    @Test
    @Order(7)
    void getOpportunitiesCountBySalesRep_With_OpenStatus() {
        var salesRep = new SalesRep("Humano");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("alguien", "124579999", "al@gm.com", "alguna",a);
        contactRepository.save(contact1);
        var account1 = new Account(Industry.ECOMMERCE, 74, "Mendoza", "Argentina");
        accountRepository.save(account1);
        var opportunity1 = new Opportunity(Product.HYBRID, 3, contact1, Status.OPEN, account1, salesRep);
        opportunityRepository.save(opportunity1);
        List<IOpportunityCountBySalesRep> opportunitiesCount = salesRepRepository.countOpportunitiesByOpenBySalesRep();
        assertEquals(1, opportunitiesCount.get(0).getOpportunitiesCount());
        assertEquals("Person 1", opportunitiesCount.get(0).getSalesRepName());
        assertEquals(2, opportunitiesCount.size());
    }


}