package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Contact;
import com.ironhack.homework3.dao.classes.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Integer> {

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.account a WHERE c.id = :id")
    Optional<Contact> findByIdJoinedAccount(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.opportunity o WHERE c.id = :id")
    Optional<Contact> findByIdJoinedOpportunity(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    // (TODO JA - Might not be needed)
    @Query("SELECT c FROM Contact c LEFT JOIN FETCH c.opportunity o LEFT JOIN FETCH o.salesRep a WHERE c.id = :id")
    Optional<Contact> findByIdJoinedOpportunitySalesRep(int id);


}
