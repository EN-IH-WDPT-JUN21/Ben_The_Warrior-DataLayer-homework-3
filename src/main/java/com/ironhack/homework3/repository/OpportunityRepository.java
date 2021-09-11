package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    // ============================== QUERIES 2 - Reporting Functionality By Product ==============================

    // 1. Report Opportunity by Product
    @Query("SELECT o.product AS productComment, COUNT(o.product) AS productCount FROM Opportunity o "+
            "GROUP BY o.product ORDER BY COUNT(o.product) DESC")
    List<IOpportunityProduct> countOpportunitiesByProduct();

    // 2. Report CLOSED_WON Opportunity by Product
    @Query("SELECT o.product AS productComment, COUNT(o.product) AS productCount FROM Opportunity o "+
            "WHERE o.status = 'CLOSED_WON'" +
            "GROUP BY o.product ORDER BY COUNT(o.product) DESC")
    List<IOpportunityProduct> countOpportunitiesClosedWonByProduct();

    // 3. Report CLOSED_LOST Opportunity by Product
    @Query("SELECT o.product AS productComment, COUNT(o.product) AS productCount FROM Opportunity o "+
            "WHERE o.status = 'CLOSED_LOST'" +
            "GROUP BY o.product ORDER BY COUNT(o.product) DESC")
    List<IOpportunityProduct> countOpportunitiesClosedLostByProduct();

    // 4. Report OPEN Opportunity by Product
    @Query("SELECT o.product AS productComment, COUNT(o.product) AS productCount FROM Opportunity o "+
            "WHERE o.status = 'OPEN'" +
            "GROUP BY o.product ORDER BY COUNT(o.product) DESC")
    List<IOpportunityProduct> countOpportunitiesOpenByProduct();


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
