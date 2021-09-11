package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.classes.SalesRep;
import com.ironhack.homework3.dao.main.MainMenuAutowired;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityProduct;
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
class OpportunityRepositoryTest {

    @MockBean
    private MainMenuAutowired mainMenuAutowired;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

    private Contact c;

    private Account a;

    private SalesRep sr;


    @BeforeEach
    void setUp() {

        a = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(a);
        c = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a);
        contactRepository.save(c);
        var c1 = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", a);
        contactRepository.save(c1);
        var c2 = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a);
        contactRepository.save(c2);
        sr = new SalesRep("Sales Guy");
        salesRepRepository.save(sr);
        var o1 = new Opportunity(Product.HYBRID, 3000, c1, Status.OPEN, a, sr);
        var o2 = new Opportunity(Product.BOX, 200, c2, Status.CLOSED_WON, a, sr);
        opportunityRepository.saveAll(List.of(o1, o2));
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        salesRepRepository.deleteAll();
        contactRepository.deleteAll();
        opportunityRepository.deleteAll();
    }


    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString() {
        Opportunity o3 = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, a, sr);
        opportunityRepository.save(o3);
        assertEquals("Id: 3, Product: HYBRID, Quantity: 30000, Decision Maker: Joe, Status: OPEN", o3.toString());

//         var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
//         accountRepository.save(account);
//         Contact c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
//         contactRepository.save(c);
//         Opportunity o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, account);
//         opportunityRepository.save(o);
//         assertEquals("Id: 2, Product: HYBRID, Quantity: 30000, Decision Maker: John Smith, Status: OPEN", o.toString());

    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, opportunityRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateOpportunity_addNewOpportunity_savedInRepository() {
        var c1 = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths", a);
        contactRepository.save(c1);
        var initialSize = opportunityRepository.count();
        opportunityRepository.save(new Opportunity(Product.FLATBED, 11, c, Status.OPEN, a, sr));
        assertEquals(initialSize + 1, opportunityRepository.count());

//         var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
//         accountRepository.save(account);
//         var OpportunityCountBeforeSave = opportunityRepository.count();
//         var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
//         contactRepository.save(contact);
//         var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
//         opportunityRepository.save(opportunity);
//         var OpportunityCountAfterSave = opportunityRepository.count();
//         assertEquals(1, OpportunityCountAfterSave - OpportunityCountBeforeSave);

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
        var o3 = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a, sr);
        opportunityRepository.save(o3);
        var storedOpportunity = opportunityRepository.findById(3);
        if (storedOpportunity.isPresent()) {
            assertEquals(3, storedOpportunity.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateOpportunity_changeQuantity_newQuantityEqualsDefinedQuantity() {
        var o3 = new Opportunity(Product.BOX, 100, Status.OPEN);
        opportunityRepository.save(o3);
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
        var o3 = new Opportunity(Product.BOX, 2000, Status.CLOSED_LOST);
        opportunityRepository.save(o3);
        var initialSize = opportunityRepository.count();
        opportunityRepository.deleteById(3);
        assertEquals(initialSize - 1, opportunityRepository.count());
    }

    // ============================== Custom Queries Testing ==============================

    // ==================== 2 - Reporting Functionality By Product ====================

    // ====================  Report Opportunity by the product ====================
    @Test
    void getCountByProduct(){
        List<IOpportunityProduct> productsCounts = opportunityRepository.countOpportunitiesByProduct();
        assertEquals(Product.HYBRID, productsCounts.get(0).getProductComment());
        assertEquals(1, productsCounts.get(0).getProductCount());
    }

    // ====================  Report Opportunity by the product with CLOSED_WON status ====================
    @Test
    void getCountByProduct_With_ClosedWonStatus(){
        var contact = new Contact("Mapi", "123643543", "mapi@gm.com", "GM");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 20, "Santiago", "Chile");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.BOX, 300, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityProduct> productsCounts = opportunityRepository.countOpportunitiesClosedWonByProduct();
        assertEquals(1, productsCounts.get(0).getProductCount());
    }

    // ====================  Report Opportunity by the product with CLOSED_LOST status ====================
    @Test
    void getCountByProduct_With_ClosedLostStatus(){
        var contact = new Contact("Mapi", "123643543", "mapi@gm.com", "GM");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 20, "Santiago", "Chile");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.BOX, 300, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityProduct> productsCounts = opportunityRepository.countOpportunitiesClosedLostByProduct();
        assertEquals(Product.BOX, productsCounts.get(0).getProductComment());
    }

    // ====================  Report Opportunity by the product with OPEN status ====================
    @Test
    void getCountByProduct_With_OpenStatus(){
        var contact = new Contact("Mapi", "123643543", "mapi@gm.com", "GM");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 20, "Santiago", "Chile");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 300, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityProduct> productsCounts = opportunityRepository.countOpportunitiesOpenByProduct();
        assertEquals(2, productsCounts.get(0).getProductCount());

    }

    // ==================== 7 - Reporting Functionality Quantity States ====================
    @Test
    void testMeanQuantity() {
        // o1 = 3000
        // o2 = 200
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        assertEquals(((double) Math.round(((3000 +200+ 73 + 386 + 3468) / 5.0) * 10000d) / 10000d), opportunityRepository.meanQuantity());
    }

    @Test
    void testOrderedListOfQuantities() {
        // o1 = 3000
        // o2 = 200
        var o2 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        assertEquals(List.of(73, 200, 386, 3000, 3468), opportunityRepository.orderedListOfQuantities());
    }

    @Test
    void testMinQuantity() {
        // o1 = 3000
        // o2 = 200
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        assertEquals(73, opportunityRepository.minQuantity());
    }

    @Test
    void testMaxQuantity() {
        // o1 = 3000
        // o2 = 200
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        opportunityRepository.save(o3);
        assertEquals(3468, opportunityRepository.maxQuantity());
    }

}

