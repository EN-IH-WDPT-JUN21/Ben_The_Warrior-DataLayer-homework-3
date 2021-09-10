package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

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
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    @Autowired
    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() {

        var a1 = new Account(Industry.ECOMMERCE, 1000, "London", "UK");
        accountRepository.save(a1);
        var o = new Opportunity(Product.BOX, 200, Status.OPEN);
        o.setAccountOpp(a1);
        opportunityRepository.save(o);
        var c1 = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a1);
        contactRepository.save(c1);
        var a2 = new Account(Industry.PRODUCE, 100, "Madrid", "Spain");
        accountRepository.save(a2);

/*        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(100);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID,30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);*/

    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }
    /*sth not working....*/

    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var a = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a);
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 0, Number of Opportunities: 0", a.toString());
    }

    @Test
    @Order(1)
    void testToString_oneOpportunityOneContact() {
        var a = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
        accountRepository.save(a);
        var c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths", a);
        contactRepository.save(c);
        a.setContactList(List.of(c));
        var o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, a);
        opportunityRepository.save(o);
        a.setOpportunityList(List.of(o));
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 100, City: Madrid, Country: Spain, Number of Contacts: 1, Number of Opportunities: 1", a.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, accountRepository.count());
    }

    // ==================== Create ====================
//    @Test
//    @Order(3)
//    void saveANewAccount() {
//        var AccountCountBeforeSave = accountRepository.count();
//        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        contact.setId(101);
//        contactRepository.save(contact);
//        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, "UK", "London");
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

//    void saveANewAccount() {
//        var AccountCountBeforeSave = accountRepository.count();
//        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
//        contactRepository.save(contact);
//        var account = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
//        accountRepository.save(account);
//        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
//        opportunityRepository.save(opportunity);
//        var AccountCountAfterSave = accountRepository.count();
//        contact.setAccount(account);
//        contactRepository.save(contact);
//        opportunity.setAccountOpp(account);
//        opportunityRepository.save(opportunity);
//        assertEquals(1, AccountCountAfterSave - AccountCountBeforeSave);
//        assertEquals(account.getId(), contact.getAccount().getId());
//        assertEquals(account.getId(), opportunity.getAccountOpp().getId());
//    }


    // ============================== Custom Queries Testing ==============================
    // ==================== 3 - Reporting Functionality By Country ====================
    @Test
    void getCountByCountry() {
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countByCountry();
        assertEquals("UK", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_ClosedWonStatus() {
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedWonByCountry();
        assertEquals("USA", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_ClosedLostStatus() {
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedLostByCountry();
        assertEquals("Mongolia", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_OpenStatus() {
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countOpenByCountry();
        assertEquals(2, countryCounts.get(0).getCountryOrCityCount());
    }


    // ==================== 4 - Reporting Functionality By City ====================
    @Test
    void getCountByCity() {
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countByCity();
        assertEquals("London", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_ClosedWonStatus() {
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedWonByCity();
        assertEquals("New York", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_ClosedLostStatus() {
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedLostByCity();
        assertEquals("Karakorum", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_OpenStatus() {
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countOpenByCity();
        assertEquals(2, cityCounts.get(0).getCountryOrCityCount());
    }

    // ==================== 5 - Reporting Functionality By Industry ====================
    @Test
    void getCountByIndustry() {
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countByIndustry();
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_ClosedWonStatus() {
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contactRepository.save(contact);
        var account = new Account(Industry.OTHER, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedWonByIndustry();
        assertEquals(Industry.OTHER, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_ClosedLostStatus() {
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contactRepository.save(contact);
        var account = new Account(Industry.PRODUCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedLostByIndustry();
        assertEquals(Industry.PRODUCE, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_OpenStatus() {
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countOpenByIndustry();
        assertEquals(2, industryCounts.get(0).getIndustryCount());
    }


    // ==================== 6 - Reporting Functionality EmployeeCount States ====================
    @Test
    void testMeanEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // mean is from the values of setup and this new account
        assertEquals(((double) Math.round(((6000 + 1000 + 100) / 3.0) * 10000d) / 10000d), accountRepository.meanEmployeeCount());
    }

    @Test
    void testOrderedListOfEmployeeCount() {
        // a1 = 1000
        // a2 = 100
        var a3 = new Account(Industry.MEDICAL, 3, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 123, "Rio de Janeiro", "Brazil");
        var a5 = new Account(Industry.MEDICAL, 546, "Rio de Janeiro", "Brazil");
        var a6 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a7 = new Account(Industry.MEDICAL, 2486, "Rio de Janeiro", "Brazil");
        var a8 = new Account(Industry.MEDICAL, 68404, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4, a5, a6, a7, a8));
        assertEquals(List.of(3, 100, 123, 546, 1000, 2486, 6000, 68404), accountRepository.orderedListOfEmployeeCount());
    }

    @Test
    void testMinEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // min is from the values of setup and this new account
        assertEquals(100, accountRepository.minEmployeeCount());
    }

    @Test
    void testMaxEmployeeCount() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // max is from the values of setup and this new account
        assertEquals(6000, accountRepository.maxEmployeeCount());
    }


    // ==================== 8 - Reporting Functionality Opportunity States ====================
    @Test
    void testMeanOpportunities() {
        var o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        var a5 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a6 = new Account(Industry.MEDICAL, 273, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4, a5, a6));
        for (int i = 0; i < 3; i++) { //3
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a3);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a4);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 9; i++) { //9
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a5);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 16; i++) { //16
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a6);
            opportunityRepository.save(o);
        }
        // mean is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals((1 + 3 + 6 + 9 + 16) / 5.0, accountRepository.meanOpportunities());
    }

    @Test
    void testMinOpportunities() {
        var o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4));
        for (int i = 0; i < 3; i++) { //3
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a3);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a4);
            opportunityRepository.save(o);
        }
        // min is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals(1 /*from the setup*/, accountRepository.minOpportunities());
    }

    @Test
    void testMaxOpportunities() {
        var o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4));
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a3);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 9; i++) { //9
            o = new Opportunity(Product.HYBRID, 1, Status.OPEN);
            o.setAccountOpp(a4);
            opportunityRepository.save(o);
        }
        // max is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals(9, accountRepository.maxOpportunities());
    }

}