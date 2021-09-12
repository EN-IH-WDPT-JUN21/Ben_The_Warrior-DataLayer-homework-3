package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.classes.SalesRep;
import com.ironhack.homework3.dao.main.MainMenuAutowired;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
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
class AccountRepositoryTest {

    @MockBean
    private MainMenuAutowired mainMenuAutowired;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

    @Autowired
    private OpportunityRepository opportunityRepository;

    private Contact c;

    private SalesRep sr;

    private Opportunity o;

    @BeforeEach
    void setUp() { // a1 contains c and o, a2 is empty account
        var a1 = new Account(Industry.ECOMMERCE, 1000, "London", "UK");
        var a2 = new Account(Industry.MANUFACTURING, 100, "Madrid", "Spain");
        accountRepository.saveAll(List.of(a1, a2));
        c = new Contact("Joe", "999999999", "joe@mail.com", "New Company", a1);
        contactRepository.save(c);
        sr = new SalesRep("Sales Person");
        salesRepRepository.save(sr);
        o = new Opportunity(Product.BOX, 100, c, Status.OPEN, a1, sr);
        opportunityRepository.save(o);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        contactRepository.deleteAll();
        opportunityRepository.deleteAll();
        salesRepRepository.deleteAll();
    }


    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var a3 = new Account(Industry.ECOMMERCE, 17, "Lisbon", "Portugal");
        accountRepository.save(a3);
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 17, City: Lisbon, Country: Portugal, Number of Contacts: 0, Number of Opportunities: 0", a3.toString());
    }

    @Test
    @Order(1)
    void testToString_oneOpportunityOneContact() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain");
        accountRepository.save(a3);
        var c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths", a3);
        contactRepository.save(c);
        var sr = new SalesRep("Sales Person");
        salesRepRepository.save(sr);
        a3.setContactList(List.of(c));
        var o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN, a3, sr);
        opportunityRepository.save(o);
        a3.setOpportunityList(List.of(o));
        assertEquals("Id: 3, Industry: ECOMMERCE, Number of Employees: 100, City: Madrid, Country: Spain, Number of Contacts: 1, Number of Opportunities: 1", a3.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, accountRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateAccount_addNewAccount_savedInRepository() {
        var initialSize = accountRepository.count();
        accountRepository.save(new Account(Industry.MEDICAL, 2000, "Porto", "Portugal"));
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
        var storedAccount = accountRepository.findById(1);
        if (storedAccount.isPresent()) {
            assertEquals(1, storedAccount.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateAccount_changeEmployeeCount_newEmployeeCountEqualsDefinedEmployeeCount() {
        var storedAccount = accountRepository.findById(1);
        if (storedAccount.isPresent()) {
            storedAccount.get().setEmployeeCount(136);
            accountRepository.save(storedAccount.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredAccount = accountRepository.findById(1);
        if (updatedStoredAccount.isPresent()) {
            assertEquals(136, updatedStoredAccount.get().getEmployeeCount());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteAccount_deleteAccount_deletedFromRepository() {
        var initialSize = accountRepository.count();
        accountRepository.deleteById(1);
        assertEquals(initialSize - 1, accountRepository.count());
    }


    // ============================== Custom Queries Testing ==============================
    // ==================== 3 - Reporting Functionality By Country ====================
    @Test
    @Order(7)
    void getCountByCountry() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 100, "Toledo", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countByCountry();
        assertEquals(2, countryCounts.size());
        assertEquals("Spain", countryCounts.get(0).getCountryOrCityComment());
        assertEquals(2, countryCounts.get(0).getCountryOrCityCount());
        assertEquals("UK", countryCounts.get(1).getCountryOrCityComment());
        assertEquals(1, countryCounts.get(1).getCountryOrCityCount());
    }

    @Test
    @Order(7)
    void getCountByCountry_With_ClosedWonStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 100, "Toledo", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedWonByCountry();
        assertEquals(2, countryCounts.size());
        assertEquals("Spain", countryCounts.get(0).getCountryOrCityComment());
        assertEquals(2, countryCounts.get(0).getCountryOrCityCount());
        assertEquals("UK", countryCounts.get(1).getCountryOrCityComment());
        assertEquals(0, countryCounts.get(1).getCountryOrCityCount());
    }

    @Test
    @Order(7)
    void getCountByCountry_With_ClosedLostStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 100, "Toledo", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedLostByCountry();
        assertEquals(2, countryCounts.size());
        assertEquals("Spain", countryCounts.get(0).getCountryOrCityComment());
        assertEquals(1, countryCounts.get(0).getCountryOrCityCount());
        assertEquals("UK", countryCounts.get(1).getCountryOrCityComment());
        assertEquals(0, countryCounts.get(1).getCountryOrCityCount());
    }

    @Test
    @Order(7)
    void getCountByCountry_With_OpenStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 100, "Toledo", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.OPEN, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countOpenByCountry();
        assertEquals(2, countryCounts.size());
        assertEquals("UK", countryCounts.get(0).getCountryOrCityComment());
        assertEquals(1, countryCounts.get(0).getCountryOrCityCount());
        assertEquals("Spain", countryCounts.get(1).getCountryOrCityComment());
        assertEquals(1, countryCounts.get(1).getCountryOrCityCount());
    }


    // ==================== 4 - Reporting Functionality By City ====================
    @Test
    @Order(8)
    void getCountByCity() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countByCity();
        assertEquals(3, cityCounts.size());
        assertEquals("Barcelona", cityCounts.get(0).getCountryOrCityComment());
        assertEquals(2, cityCounts.get(0).getCountryOrCityCount());
        assertEquals("London", cityCounts.get(1).getCountryOrCityComment());
        assertEquals(1, cityCounts.get(1).getCountryOrCityCount());
        assertEquals("Madrid", cityCounts.get(2).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(2).getCountryOrCityCount());
    }

    @Test
    @Order(8)
    void getCountByCity_With_ClosedWonStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedWonByCity();
        assertEquals(3, cityCounts.size());
        assertEquals("Barcelona", cityCounts.get(0).getCountryOrCityComment());
        assertEquals(2, cityCounts.get(0).getCountryOrCityCount());
        assertEquals("London", cityCounts.get(1).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(1).getCountryOrCityCount());
        assertEquals("Madrid", cityCounts.get(2).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(2).getCountryOrCityCount());
    }

    @Test
    @Order(8)
    void getCountByCity_With_ClosedLostStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedLostByCity();
        assertEquals(3, cityCounts.size());
        assertEquals("Barcelona", cityCounts.get(0).getCountryOrCityComment());
        assertEquals(1, cityCounts.get(0).getCountryOrCityCount());
        assertEquals("London", cityCounts.get(1).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(1).getCountryOrCityCount());
        assertEquals("Madrid", cityCounts.get(2).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(2).getCountryOrCityCount());
    }

    @Test
    @Order(8)
    void getCountByCity_With_OpenStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.ECOMMERCE, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.OPEN, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countOpenByCity();
        assertEquals(3, cityCounts.size());
        assertEquals("London", cityCounts.get(0).getCountryOrCityComment());
        assertEquals(1, cityCounts.get(0).getCountryOrCityCount());
        assertEquals("Barcelona", cityCounts.get(1).getCountryOrCityComment());
        assertEquals(1, cityCounts.get(1).getCountryOrCityCount());
        assertEquals("Madrid", cityCounts.get(2).getCountryOrCityComment());
        assertEquals(0, cityCounts.get(2).getCountryOrCityCount());
    }

    // ==================== 5 - Reporting Functionality By Industry ====================
    @Test
    @Order(9)
    void getCountByIndustry() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.MEDICAL, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.OPEN, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityIndustryCount> industryCounts = accountRepository.countByIndustry();
        assertEquals(3, industryCounts.size());
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
        assertEquals(2, industryCounts.get(0).getIndustryCount());
        assertEquals(Industry.MEDICAL, industryCounts.get(1).getIndustryComment());
        assertEquals(1, industryCounts.get(1).getIndustryCount());
        assertEquals(Industry.MANUFACTURING, industryCounts.get(2).getIndustryComment());
        assertEquals(0, industryCounts.get(2).getIndustryCount());
    }

    @Test
    @Order(9)
    void getCountByIndustry_With_ClosedWonStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.MEDICAL, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedWonByIndustry();
        assertEquals(3, industryCounts.size());
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
        assertEquals(1, industryCounts.get(0).getIndustryCount());
        assertEquals(Industry.MEDICAL, industryCounts.get(1).getIndustryComment());
        assertEquals(1, industryCounts.get(1).getIndustryCount());
        assertEquals(Industry.MANUFACTURING, industryCounts.get(2).getIndustryComment());
        assertEquals(0, industryCounts.get(2).getIndustryCount());
    }

    @Test
    @Order(9)
    void getCountByIndustry_With_ClosedLostStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.MEDICAL, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_LOST, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedLostByIndustry();
        assertEquals(3, industryCounts.size());
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
        assertEquals(1, industryCounts.get(0).getIndustryCount());
        assertEquals(Industry.MANUFACTURING, industryCounts.get(1).getIndustryComment());
        assertEquals(0, industryCounts.get(1).getIndustryCount());
        assertEquals(Industry.MEDICAL, industryCounts.get(2).getIndustryComment());
        assertEquals(0, industryCounts.get(2).getIndustryCount());
    }

    @Test
    @Order(9)
    void getCountByIndustry_With_OpenStatus() {
        var a3 = new Account(Industry.ECOMMERCE, 100, "Barcelona", "Spain");
        var a4 = new Account(Industry.MEDICAL, 200, "Barcelona", "Spain");
        accountRepository.saveAll(List.of(a3, a4));
        var o1 = new Opportunity(Product.HYBRID, 100, c, Status.OPEN, a3, sr);
        var o2 = new Opportunity(Product.HYBRID, 100, c, Status.CLOSED_WON, a4, sr);
        opportunityRepository.saveAll(List.of(o1, o2));

        List<IOpportunityIndustryCount> industryCounts = accountRepository.countOpenByIndustry();
        assertEquals(3, industryCounts.size());
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
        assertEquals(2, industryCounts.get(0).getIndustryCount());
        assertEquals(Industry.MANUFACTURING, industryCounts.get(1).getIndustryComment());
        assertEquals(0, industryCounts.get(1).getIndustryCount());
        assertEquals(Industry.MEDICAL, industryCounts.get(2).getIndustryComment());
        assertEquals(0, industryCounts.get(2).getIndustryCount());
    }


    // ==================== 6 - Reporting Functionality EmployeeCount States ====================
    @Test
    @Order(10)
    void testMeanEmployeeCount() {
        // a1 = 1000
        // a2 = 100
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // mean is from the values of setup and this new account
        assertEquals(((double) Math.round(((6000 + 1000 + 100) / 3.0) * 10000d) / 10000d), accountRepository.meanEmployeeCount());
    }

    @Test
    @Order(10)
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
    @Order(10)
    void testMinEmployeeCount() {
        // a1 = 1000
        // a2 = 100
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // min is from the values of setup and this new account
        assertEquals(100, accountRepository.minEmployeeCount());
    }

    @Test
    @Order(10)
    void testMaxEmployeeCount() {
        // a1 = 1000
        // a2 = 100
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        accountRepository.save(a3);
        // max is from the values of setup and this new account
        assertEquals(6000, accountRepository.maxEmployeeCount());
    }


    // ==================== 8 - Reporting Functionality Opportunity States ====================
    @Test
    @Order(11)
    void testMeanOpportunities() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        var a5 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a6 = new Account(Industry.MEDICAL, 273, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4, a5, a6));
        for (int i = 0; i < 3; i++) { //3
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a3, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a4, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 9; i++) { //9
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a5, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 16; i++) { //16
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a6, sr);
            opportunityRepository.save(o);
        }
        // mean is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals((1 + 3 + 6 + 9 + 16) / 5.0, accountRepository.meanOpportunities());
    }

    @Test
    @Order(11)
    void testOrderedListOfOpportunities() {
        // a1 = 1
        // a2 = 0 doesn't count
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        var a5 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a6 = new Account(Industry.MEDICAL, 273, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4, a5, a6));
        for (int i = 0; i < 3; i++) { //3
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a3, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a4, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 9; i++) { //9
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a5, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 16; i++) { //16
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a6, sr);
            opportunityRepository.save(o);
        }
        accountRepository.saveAll(List.of(a3, a4, a5, a6));
        assertEquals(List.of(1, 3, 6, 9, 16), accountRepository.orderListOfOpportunities());
    }

    @Test
    @Order(11)
    void testMinOpportunities() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4));
        for (int i = 0; i < 3; i++) { //3
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a3, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a4, sr);
            opportunityRepository.save(o);
        }
        // min is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals(1 /*from the setup*/, accountRepository.minOpportunities());
    }

    @Test
    @Order(11)
    void testMaxOpportunities() {
        var a3 = new Account(Industry.MEDICAL, 6000, "Rio de Janeiro", "Brazil");
        var a4 = new Account(Industry.MEDICAL, 835, "Rio de Janeiro", "Brazil");
        accountRepository.saveAll(List.of(a3, a4));
        for (int i = 0; i < 6; i++) { //6
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a3, sr);
            opportunityRepository.save(o);
        }
        for (int i = 0; i < 9; i++) { //9
            o = new Opportunity(Product.HYBRID, 1, c, Status.OPEN, a4, sr);
            opportunityRepository.save(o);
        }
        // max is from the values of setup and these new opportunity (only for opportunity with accounts)
        assertEquals(9, accountRepository.maxOpportunities());
    }

}