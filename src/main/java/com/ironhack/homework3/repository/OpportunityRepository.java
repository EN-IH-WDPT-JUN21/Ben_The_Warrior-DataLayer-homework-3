package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    // ============================== QUERIES 7 - Reporting Functionality Quantity States ==============================
    // 1. Mean Quantity
    @Query("SELECT AVG(o.quantity) FROM Opportunity o")
    double meanQuantity();

    // 2. Median Quantity - returns all the quantities ordered and uses an util method to calculate Median
    @Query("SELECT o.quantity AS q FROM Opportunity o " +
            "ORDER by q")
    List<Integer> orderedListOfQuantities();

    // 3. Max Quantity
    @Query("SELECT MIN(o.quantity) FROM Opportunity o")
    int minQuantity();

    // 4. Min Quantity
    @Query("SELECT MAX(o.quantity) FROM Opportunity o")
    int maxQuantity();


}
