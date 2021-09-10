package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.SalesRep;
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
class SalesRepRepositoryTest {

    @Autowired
    private SalesRepRepository salesRepRepository;

    @BeforeEach
    void setUp() {
        var sr1 = new SalesRep("Sales Guy");
        var sr2 = new SalesRep("Sales Girl");
        salesRepRepository.saveAll(List.of(sr1, sr2));
    }

    @AfterEach
    void tearDown() {
        salesRepRepository.deleteAll();
    }

    // ============================== JAVA Object Testing ==============================
    @Test
    @Order(1)
    void testToString_noOpportunitiesAndNoContacts() {
        var sr3 = new SalesRep("New Sales Person");
        salesRepRepository.save(sr3);
        assertEquals("Id: 3, Name: New Sales Person", sr3.toString());
    }


    // ============================== CRUD Testing ==============================
    @Test
    @Order(2)
    void count() {
        assertEquals(2, salesRepRepository.count());
    }

    // ==================== Create ====================
    @Test
    @Order(3)
    void testCreateSalesRep_addNewSalesRep_savedInRepository() {
        var initialSize = salesRepRepository.count();
        salesRepRepository.save(new SalesRep("New Sales Girl"));
        assertEquals(initialSize + 1, salesRepRepository.count());
    }

    // ==================== Read ====================
    @Test
    @Order(4)
    void testReadSalesRep_findAll_returnsListOfObjectsNotEmpty() {
        var allElements = salesRepRepository.findAll();
        assertFalse(allElements.isEmpty());
    }

    @Test
    @Order(4)
    void testReadSalesRep_findById_returnsObjectsWithId() {
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
        var storedSr = salesRepRepository.findById(3);
        if (storedSr.isPresent()) {
            assertEquals(3, storedSr.get().getId());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Update ====================
    @Test
    @Order(5)
    void testUpdateSalesRep_changeName_newNameEqualsDefinedName() {
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
        var storedSr = salesRepRepository.findById(3);
        if (storedSr.isPresent()) {
            storedSr.get().setName("Old Sales Girl");
            salesRepRepository.save(storedSr.get());
        } else throw new TestInstantiationException("Id not found");
        var updatedStoredSr = salesRepRepository.findById(3);
        if (updatedStoredSr.isPresent()) {
            assertEquals("Old Sales Girl", updatedStoredSr.get().getName());
        } else throw new TestInstantiationException("Id not found");
    }

    // ==================== Delete ====================
    @Test
    @Order(6)
    void testDeleteSalesRep_deleteSalesRep_deletedFromRepository() {
        var sr3 = new SalesRep("New Sales Girl");
        salesRepRepository.save(sr3);
        var initialSize = salesRepRepository.count();
        salesRepRepository.deleteById(3);
        assertEquals(initialSize - 1, salesRepRepository.count());
    }


}