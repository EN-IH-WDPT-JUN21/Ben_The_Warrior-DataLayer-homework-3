package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.SalesRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalesRepRepository extends JpaRepository<SalesRep, Integer> {

    @Query("SELECT sr FROM SalesRep sr LEFT JOIN FETCH sr.leadList l WHERE sr.id = :id")
    Optional<SalesRep> findByIdJoinedLeads(int id);

    @Query("SELECT sr FROM SalesRep sr LEFT JOIN FETCH sr.opportunityList o WHERE sr.id = :id")
    Optional<SalesRep> findByIdJoinedOpportunities(int id);

}
