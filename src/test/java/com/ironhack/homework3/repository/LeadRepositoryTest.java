/*package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LeadRepositoryTest {

    @Autowired
    LeadRepository leadRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }
   @Test
    void testToString() {
        Lead c = new Lead("John Smith", "2460247246", "johnthewarrior@fighters.com", "The smiths");
        assertEquals("Id: null, Name: John Smith, Email: johnthewarrior@fighters.com, Phone: 2460247246, Company: The smiths", c.toString());
    }

    @Test
    void saveANewLead(){
        var LeadCountBeforeSave = leadRepository.count();
        var lead = new Lead(100, "Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries");
        leadRepository.save(lead);
        var LeadCountAfterSave = leadRepository.count();
        assertEquals(1, LeadCountAfterSave - LeadCountBeforeSave);
        leadRepository.delete(lead);
    }
}*/