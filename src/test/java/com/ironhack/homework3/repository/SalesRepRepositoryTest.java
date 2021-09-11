package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.dao.queryInterfaces.ILeadsCountBySalesRep;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountBySalesRep;
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
class SalesRepRepositoryTest {

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

    @BeforeEach
    void setUp(){

        var salesRep = new SalesRep("person 1");
        salesRepRepository.save(salesRep);
        var salesRep2 = new SalesRep("Sam");
        salesRepRepository.save(salesRep2);
        var lead = new Lead(1, "Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", salesRep);
        leadRepository.save(lead);
        var lead2 = new Lead(2, "John", "999999942", "John@gmail.com", "John Spa", salesRep2);
        leadRepository.save(lead2);
        var lead3 = new Lead(3, "a", "999999974", "a@gmail.com", "a", salesRep2);
        leadRepository.save(lead3);
        var contact = new Contact("Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        contactRepository.save(contact);
        var account = new Account(Industry.ECOMMERCE, 200, "London", "UK");
        accountRepository.save(account);
        var opportunity = new Opportunity(Product.HYBRID, 3000, contact, Status.OPEN, account, salesRep);
        opportunityRepository.save(opportunity);
    }

    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
        contactRepository.deleteAll();
        accountRepository.deleteAll();
    }

    // ============================== Custom Queries Testing ==============================
    // ==================== 1 - Reporting Leads By SalesRep ====================

    @Test
    void getLeadsCountBySalesRep() {
        List<ILeadsCountBySalesRep> leadsCounts = salesRepRepository.countLeadsBySalesRep();
        assertEquals(2, ((List<?>) leadsCounts).size());
        assertEquals("Sam", leadsCounts.get(0).getSalesRepName());
        assertEquals(2, leadsCounts.get(0).getLeadsCount());
        assertEquals("person 1", leadsCounts.get(1).getSalesRepName());
        assertEquals(1, leadsCounts.get(1).getLeadsCount());
    }

    // ==================== 2 - Reporting Opportunities By SalesRep ====================
    @Test
    void getOpportunitiesCountBySalesRep(){
        List<IOpportunityCountBySalesRep> opportunitiesCounts = salesRepRepository.countOpportunitiesBySalesRep();
        assertEquals(1, opportunitiesCounts.get(0).getOpportunitiesCount());
        assertEquals("person 1", opportunitiesCounts.get(0).getSalesRepName());
    }

    // ==================== 3 - Reporting Opportunities By SalesRep With Status CLOSED_WON ====================
    @Test
    void getOpportunitiesCountBySalesRep_With_ClosedWonStatus(){
        var salesRep = new SalesRep("person");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("lala", "124578654", "a@gm.com", "lolo");
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
    void getOpportunitiesCountBySalesRep_With_ClosedLostStatus(){
        var salesRep = new SalesRep("Humano");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("alguien", "124579999", "al@gm.com", "alguna");
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
    void getOpportunitiesCountBySalesRep_With_OpenStatus(){
        var salesRep = new SalesRep("Humano");
        salesRepRepository.save(salesRep);
        var contact1 = new Contact("alguien", "124579999", "al@gm.com", "alguna");
        contactRepository.save(contact1);
        var account1 = new Account(Industry.ECOMMERCE, 74, "Mendoza", "Argentina");
        accountRepository.save(account1);
        var opportunity1 = new Opportunity(Product.HYBRID, 3, contact1, Status.OPEN, account1, salesRep);
        opportunityRepository.save(opportunity1);
        List<IOpportunityCountBySalesRep> opportunitiesCount = salesRepRepository.countOpportunitiesByOpenBySalesRep();
        assertEquals(1, opportunitiesCount.get(0).getOpportunitiesCount());
        assertEquals("person 1", opportunitiesCount.get(0).getSalesRepName());
        assertEquals(2,opportunitiesCount.size());
    }






}