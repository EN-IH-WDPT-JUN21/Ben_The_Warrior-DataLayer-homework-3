package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    ContactRepository contactRepository;

    Account a;

    @BeforeEach
    void setUp() {
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contact.setId(100);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID,30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();



    }
    /*sth not working....*/

    @Test
    void testToString() {
        Contact c = new Contact("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        c.setId(102);
        contactRepository.save(c);
        Opportunity o = new Opportunity(Product.HYBRID, 30000, c, Status.OPEN);
        opportunityRepository.save(o);
        a = new Account(Industry.ECOMMERCE, 100, "Madrid", "Spain", List.of(c), List.of(o));
        accountRepository.save(a);
        c.setAccount(a);
        o.setAccountOpp(a);
        contactRepository.save(c);
        opportunityRepository.save(o);
        assertEquals("Id: 1, Industry: ECOMMERCE, Number of Employees: 100, City: Madrid, Country: Spain, Number of Contacts: 1, Number of Opportunities: 1", a.toString());
    }

    @Test
    void saveANewAccount(){
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

    @Test
    void getCountByCountry(){
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countByCountry();
        assertEquals("UK", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_ClosedWonStatus(){
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contact.setId(101);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedWonByCountry();
        assertEquals("USA", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_ClosedLostStatus(){
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countClosedLostByCountry();
        assertEquals("Mongolia", countryCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCountry_With_OpenStatus(){
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> countryCounts = accountRepository.countOpenByCountry();
        assertEquals(2, countryCounts.get(0).getCountryOrCityCount());
    }

    @Test
    void getCountByCity(){
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countByCity();
        assertEquals("London", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_ClosedWonStatus(){
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contact.setId(101);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedWonByCity();
        assertEquals("New York", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_ClosedLostStatus(){
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countClosedLostByCity();
        assertEquals("Karakorum", cityCounts.get(0).getCountryOrCityComment());
    }

    @Test
    void getCountByCity_With_OpenStatus(){
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityCountryOrCityCount> cityCounts = accountRepository.countOpenByCity();
        assertEquals(2, cityCounts.get(0).getCountryOrCityCount());
    }

    @Test
    void getCountByIndustry(){
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countByIndustry();
        assertEquals(Industry.ECOMMERCE, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_ClosedWonStatus(){
        var contact = new Contact("Macho Man", "123643543", "Randy@savage.com", "WWF");
        contact.setId(101);
        contactRepository.save(contact);
        var account = new Account(Industry.OTHER, 200, "New York", "USA");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_WON, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedWonByIndustry();
        assertEquals(Industry.OTHER, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_ClosedLostStatus(){
        var contact = new Contact("Genghis Khan", "123643543", "Khan@steppe.com", "KhanEmpire");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.PRODUCE, 200, "Karakorum", "Mongolia");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.CLOSED_LOST, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countClosedLostByIndustry();
        assertEquals(Industry.PRODUCE, industryCounts.get(0).getIndustryComment());
    }

    @Test
    void getCountByIndustry_With_OpenStatus(){
        var contact = new Contact("Maurice Moss", "123643543", "Moss@thebasement.com", "Reynholm Industries");
        contact.setId(102);
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 30000, contact, Status.OPEN, account);
        opportunityRepository.save(opportunity);
        List<IOpportunityIndustryCount> industryCounts = accountRepository.countOpenByIndustry();
        assertEquals(2, industryCounts.get(0).getIndustryCount());
    }
}