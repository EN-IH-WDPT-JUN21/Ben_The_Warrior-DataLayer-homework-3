package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityProduct;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

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
class OpportunityRepositoryTest {

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AccountRepository accountRepository;

    @BeforeEach
    void setUp() {
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 3000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
    }


    // ============================== JAVA Object Testing ==============================
    @Test
    void testToString() {
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        Contact c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        contactRepository.save(c);
        Opportunity o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, account);
        opportunityRepository.save(o);
        assertEquals("Id: 2, Product: HYBRID, Quantity: 30000, Decision Maker: John Smith, Status: OPEN", o.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    void saveANewContact() {
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var OpportunityCountBeforeSave = opportunityRepository.count();
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contactRepository.save(contact);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        var OpportunityCountAfterSave = opportunityRepository.count();
        assertEquals(1, OpportunityCountAfterSave - OpportunityCountBeforeSave);
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
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
//         mean is from the values of setup (3000) and this new account
        assertEquals(((double) Math.round(((3000 + 73 + 386 + 3468) / 4.0) * 10000d) / 10000d), opportunityRepository.meanQuantity());
    }

    @Test
    void testOrderedListOfQuantities() {
        var o2 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        // mean is from the values of setup (3000) and this new account
        assertEquals(List.of(73, 386, 3000, 3468), opportunityRepository.orderedListOfQuantities());
    }

    @Test
    void testMinQuantity() {
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        // min is from the values of setup (3000) and this new account
        assertEquals(73, opportunityRepository.minQuantity());
    }

    @Test
    void testMaxQuantity() {
        var o2 = new Opportunity(Product.HYBRID, 73, Status.OPEN);
        var o3 = new Opportunity(Product.HYBRID, 386, Status.OPEN);
        var o4 = new Opportunity(Product.HYBRID, 3468, Status.OPEN);
        opportunityRepository.saveAll(List.of(o2, o3, o4));
        opportunityRepository.save(o3);
        // max is from the values of setup (3000) and this new account
        assertEquals(3468, opportunityRepository.maxQuantity());
    }


    // ====================  ====================

}

