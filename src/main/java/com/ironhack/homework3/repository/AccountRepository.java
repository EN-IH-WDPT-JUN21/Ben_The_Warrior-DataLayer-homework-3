package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.classes.SalesRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.contactList c WHERE a.id = :id")
    Optional<Account> findByIdJoinedContact(int id);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.opportunityList o WHERE a.id = :id")
    Optional<Account> findByIdJoinedOpportunity(int id);

}
