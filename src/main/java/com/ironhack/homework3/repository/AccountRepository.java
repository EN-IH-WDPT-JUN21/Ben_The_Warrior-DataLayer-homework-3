package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountryOrCityCount;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityIndustryCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    // ============================== QUERIES 3 - Reporting Functionality By Country ==============================
    // 1. Report Opportunity by Country
    @Query("SELECT a.country AS countryOrCityComment, COUNT(o) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY COUNT(o) DESC")
    List<IOpportunityCountryOrCityCount> countByCountry();

    // 2. Report CLOSED-WON by Country
    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCountry();

    // 3. Report CLOSED-LOST by Country
    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCountry();

    // 4. Report OPEN by Country
    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCountry();


    // ============================== QUERIES 4 - Reporting Functionality By City ==============================
    // 1. Report Opportunity by City
    @Query("SELECT a.city AS countryOrCityComment, COUNT(o) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY COUNT(o) DESC")
    List<IOpportunityCountryOrCityCount> countByCity();

    // 2. Report CLOSED-WON by City
    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCity();

    // 3. Report CLOSED-LOST by City
    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCity();

    // 4. Report OPEN by City
    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCity();


    // ============================== QUERIES 5 - Reporting Functionality By Industry ==============================
    // 1. Report Opportunity by Industry
    @Query("SELECT a.industry AS industryComment, COUNT(o) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY COUNT(o) DESC")
    List<IOpportunityIndustryCount> countByIndustry();

    // 2. Report CLOSED-WON by Industry
    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countClosedWonByIndustry();

    // 3. Report CLOSED-LOST by Industry
    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countClosedLostByIndustry();

    // 4. Report OPEN by Industry
    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countOpenByIndustry();


    // ============================== QUERIES 6 - Reporting Functionality EmployeeCount States ==============================
    // 1. Mean EmployeeCount
    @Query("SELECT AVG(a.employeeCount) FROM Account a")
    double meanEmployeeCount();

    // 2. Median EmployeeCount - returns all the employeeCounts ordered and uses an util method to calculate Median
    @Query("SELECT a.employeeCount AS ec FROM Account a " +
            "ORDER BY ec")
    List<Integer> orderedListOfEmployeeCount();

    // 3. Max EmployeeCount
    @Query("SELECT MIN(a.employeeCount) FROM Account a")
    int minEmployeeCount();

    // 4. Min EmployeeCount
    @Query("SELECT MAX(a.employeeCount) FROM Account a")
    int maxEmployeeCount();

    // ============================== QUERIES 8 - Reporting Functionality Opportunity States ==============================
    // 1. Mean Opps per Account
    @Query(value = "SELECT AVG(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    double meanOpportunities();

    // 2. Median Opps per Account - returns all the opportunitiesCount ordered and uses an util method to calculate Median
    @Query(value = "SELECT count(*) AS nrOpp FROM opportunity " +
            "GROUP BY account_id " +
            "ORDER BY nrOpp", nativeQuery = true)
    List<Integer> orderListOfOpportunities();

    // 3. Max Opps per Account
    @Query(value = "SELECT MIN(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int minOpportunities();

    // 4. Min Opps per Account
    @Query(value = "SELECT MAX(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int maxOpportunities();


}
