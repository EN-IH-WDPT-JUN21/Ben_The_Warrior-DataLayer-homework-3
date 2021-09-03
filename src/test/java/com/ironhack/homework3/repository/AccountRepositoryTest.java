package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.classes.SalesRep;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.*;
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
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    SalesRepRepository salesRepRepository;

    @BeforeEach
    void setUp() {
        var a = new Account(Industry.PRODUCE, 99, "Madrid", "Spain");
        accountRepository.save(a);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
    }

    // ============================== POJO Testing ==============================
    @Test
    void testToString_noOpportunitiesAndNoContacts() {
        var a = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a);
        assertEquals("Id: 2, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 0, Number of Opportunities: 0", a.toString());
    }

    @Test
    void testToString() {
        var a = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a);

        var sr = new SalesRep("Sales Guy");
        salesRepRepository.save(sr);

        var c = new Contact("Contact Guy", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        c.setAccount(a);
        contactRepository.save(c);

        var o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, sr);
        o.setDecisionMaker(c);
        o.setAccountOpp(a);
        o.setSalesRep(sr);
        opportunityRepository.save(o);

        accountRepository.save(a);

        assertEquals("Id: 2, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 1, Number of Opportunities: 1", a.toString());
    }

    // ============================== CRUD Testing ==============================

    @Test
    void saveANewAccount() {
        var AccountCountBeforeSave = accountRepository.count();

        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(101);
        contactRepository.save(contact);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN);
        opportunityRepository.save(opportunity);
        var account = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
        accountRepository.save(account);
        var AccountCountAfterSave = accountRepository.count();
        contact.setAccount(account);
        contactRepository.save(contact);
        opportunity.setAccountOpp(account);
        opportunityRepository.save(opportunity);
        assertEquals(1, AccountCountAfterSave - AccountCountBeforeSave);
        assertEquals(account.getId(), contact.getAccount().getId());
        assertEquals(account.getId(), opportunity.getAccountOpp().getId());
    }
}