package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //Resets DB and ids, but is slower
@TestPropertySource(properties = {      // For testing it uses a "datalayer_test" database
        "spring.datasource.url=jdbc:mysql://localhost:3306/datalayer_test",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.datasource.username=test",
        "spring.datasource.password=Test-123",
        "spring.jpa.hibernate.ddl-auto=create"
})
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    @BeforeEach
    void setUp() {
        var a1 = new Account(Industry.PRODUCE, 1000, "London", "UK");
        accountRepository.save(a1);
        var o = new Opportunity(Product.BOX, 200, Status.CLOSED_WON);
        o.setAccountOpp(a1);
        opportunityRepository.save(o);
        var c1 = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a1);
        contactRepository.save(c1);
        var a2 = new Account(Industry.PRODUCE, 100, "Madrid", "Spain");
        accountRepository.save(a2);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }


    // ============================== POJO Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var a = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a);
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 0, Number of Opportunities: 0", a.toString());
    }

    @Test
    @Order(1)
    void testToString() {
        var a1 = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a1);
        var c = new Contact("Contact Guy", "2460247246", "johnthewarrior@fighters.com", "The smiths", a1);
        contactRepository.save(c);
        a1.setContactList(List.of(c));
        var o = new Opportunity(Product.HYBRID, 30000, Status.OPEN);
        o.setDecisionMaker(c);
        o.setAccountOpp(a1);
        opportunityRepository.save(o);
        a1.setOpportunityList(List.of(o));
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 1, Number of Opportunities: 1", a1.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, accountRepository.count());
    }

    // ==================== Create ====================
//    @Test
//    void saveANewAccount() {
//        var AccountCountBeforeSave = accountRepository.count();
//
//        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        contact.setId(101);
//        contactRepository.save(contact);
//        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN);
//        opportunityRepository.save(opportunity);
//        var account = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
//        accountRepository.save(account);
//        var AccountCountAfterSave = accountRepository.count();
//        contact.setAccount(account);
//        contactRepository.save(contact);
//        opportunity.setAccountOpp(account);
//        opportunityRepository.save(opportunity);
//        assertEquals(1, AccountCountAfterSave - AccountCountBeforeSave);
//        assertEquals(account.getId(), contact.getAccount().getId());
//        assertEquals(account.getId(), opportunity.getAccountOpp().getId());
//    }

    @Test
    @Order(3)
    void testCreateAccount_addNewAccount_savedInRepository() {
        var initialSize = accountRepository.count();
        accountRepository.save(new Account(Industry.MEDICAL, 2000, "Coimbra", "Portugal"));
        assertEquals(initialSize + 1, accountRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadAccount_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = accountRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadAccount_findById_returnsObjectsWithId() {
        var a1 = new Account(Industry.ECOMMERCE, 10, "Berlin", "Germany");
        accountRepository.save(a1);
        var storedAccount = accountRepository.findById(3);
        if (storedAccount.isPresent()) {
            assertEquals(3, storedAccount.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateAccount_changeEmployeeCount_newEmployeeCountEqualsDefinedEmployeeCount() {
        var a1 = new Account(Industry.MANUFACTURING, 135, "New York", "USA");
        accountRepository.save(a1);
        var storedAccount = accountRepository.findById(3);
        if (storedAccount.isPresent()) {
            storedAccount.get().setEmployeeCount(136);
            accountRepository.save(storedAccount.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredAccount = accountRepository.findById(3);
        if (updatedStoredAccount.isPresent()) {
            assertEquals(136, updatedStoredAccount.get().getEmployeeCount());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteAccount_deleteAccount_deletedFromRepository() {
        var a1 = new Account(Industry.OTHER, 5, "Sydney", "Australia");
        accountRepository.save(a1);
        var initialSize = accountRepository.count();
        accountRepository.deleteById(3);
        assertEquals(initialSize - 1, accountRepository.count());
    }


    // ============================== Relations Testing ==============================
    // ==================== with Contacts ====================
    @Test
    @Order(7)
    void testCheckForContacts() {
        var a1 = new Account(Industry.MEDICAL, 350, "Rio de Janeiro", "Brazil");
        accountRepository.save(a1);
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN", a1);
        contactRepository.save(c1);
        var o1 = new Opportunity(Product.HYBRID, 6, c1, Status.OPEN);
        o1.setAccountOpp(a1);
        opportunityRepository.save(o1);

        var storedAccount = accountRepository.findByIdJoinedContact(3);
        if (storedAccount.isPresent()) {
            assertEquals("CN", storedAccount.get().getContactList().get(0).getCompanyName());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== with Opportunities ====================
    @Test
    @Order(7)
    void testCheckForOpportunities() {
        var a1 = new Account(Industry.MEDICAL, 350, "Rio de Janeiro", "Brazil");
        accountRepository.save(a1);
        var c1 = new Contact("New Contact", "999999999", "newemail@mail.com", "CN", a1);
        contactRepository.save(c1);
        var o1 = new Opportunity(Product.HYBRID, 6, c1, Status.OPEN);
        o1.setAccountOpp(a1);
        opportunityRepository.save(o1);

        var storedAccount = accountRepository.findByIdJoinedOpportunity(3);
        if (storedAccount.isPresent()) {
            assertEquals(Status.OPEN, storedAccount.get().getOpportunityList().get(0).getStatus());
        } else throw new TestInstantiationException("Id not found");
    }


    // ============================== Custom Queries Testing ==============================
    // ==================== EmployeeCount States ====================
    @Test
    @Order(8)
    void testMeanEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // mean is from the values of setup and this new account
        assertEquals(((double) Math.round(((6000 + 1000 + 100) / 3.0) * 10000d) / 10000d), accountRepository.meanEmployeeCount());
    }

//    @Test
//    @Order(8)
//    void testMedianEmployeeCount_oddNrOfValues() {
//        var a3 = new Account(Industry.MEDICAL, 537, "Rio de Janeiro", "Brazil");
//        accountRepository.save(a3);
//        // mean is from the values of setup and this new account
//        assertEquals(537 * 1.0, accountRepository.medianEmployeeCount());
//    }

//    @Test
//    @Order(8)
//    void testMedianEmployeeCount_evenNrOfValues() {
//        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
//        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
//        accountRepository.saveAll(List.of(a3, a4));
//        // mean is from the values of setup and this new account
//        assertEquals((1000 + 835) / 2.0, accountRepository.medianEmployeeCount());
//    }

    @Test
    @Order(8)
    void testMinEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // min is from the values of setup and this new account
        assertEquals(100, accountRepository.minEmployeeCount());
    }

    @Test
    @Order(8)
    void testMaxEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // max is from the values of setup and this new account
        assertEquals(6000, accountRepository.maxEmployeeCount());
    }
}