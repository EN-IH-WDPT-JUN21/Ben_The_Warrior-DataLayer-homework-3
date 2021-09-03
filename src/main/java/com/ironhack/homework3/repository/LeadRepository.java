package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {

    // Use this if Lead's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT l FROM Lead l LEFT JOIN FETCH l.salesRep sr WHERE l.id = :id")
    Optional<Lead> findByIdJoinedSalesRep(int id);


}
