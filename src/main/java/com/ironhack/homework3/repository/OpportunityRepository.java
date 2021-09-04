package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.accountOpp a WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedAccount(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.decisionMaker c WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedContact(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.salesRep sr WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedSalesRep(int id);

}
