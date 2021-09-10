package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import com.ironhack.homework3.dao.classes.SalesRep;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

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
class LeadRepositoryTest {

    @Autowired
    private LeadRepository leadRepository;

    @Autowired
    private SalesRepRepository salesRepRepository;

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
    void saveANewLead() {
        var leadCountBeforeSave = leadRepository.count();
        var salesrep = new SalesRep("Sales person");
        salesRepRepository.save(salesrep);
        var lead = new Lead(100, "Ben", "123643543", "Ben@BenIndustries.com", "Ben Industries", salesrep);
        leadRepository.save(lead);
        var leadCountAfterSave = leadRepository.count();
        assertEquals(1, leadCountAfterSave - leadCountBeforeSave);
        leadRepository.delete(lead);
    }
}