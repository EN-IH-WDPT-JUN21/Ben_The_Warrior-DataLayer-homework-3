package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Opportunity;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {


    // ============================== QUERIES 3 - Reporting Functionality By Country ==============================
    // 1. Report Opportunity by Country
    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countByCountry();

    // 2. Report CLOSED-WON by Country
    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCountry();

    // 3. Report CLOSED-LOST by Country
    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCountry();

    // 4. Report OPEN by Country
    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCountry();


    // ============================== QUERIES 4 - Reporting Functionality By City ==============================
    // 1. Report Opportunity by City
    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countByCity();

    // 2. Report CLOSED-WON by City
    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCity();

    // 3. Report CLOSED-LOST by City
    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCity();

    // 4. Report OPEN by City
    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCity();


    // ============================== QUERIES 5 - Reporting Functionality By Industry ==============================
    // 1. Report Opportunity by Industry
    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countByIndustry();

    // 2. Report CLOSED-WON by Industry
    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countClosedWonByIndustry();

    // 3. Report CLOSED-LOST by Industry
    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countClosedLostByIndustry();

    // 4. Report OPEN by Industry
    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countOpenByIndustry();


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
