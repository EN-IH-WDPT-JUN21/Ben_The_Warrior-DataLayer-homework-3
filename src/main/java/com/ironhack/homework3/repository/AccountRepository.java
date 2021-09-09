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
            "HAVING account_id IS NOT NULL" +
            "ORDER BY nrOpp", nativeQuery = true)
    List<Integer> orderListOfOpportunities();

    // 3. Max Opps per Account
    @Query(value = "SELECT MIN(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int minOpportunities();

    // 4. Min Opps per Account
    @Query(value = "SELECT MAX(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int maxOpportunities();



    @Query("SELECT a.country AS countryOrCityComment, COUNT(o) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY COUNT(o) DESC")
    List<IOpportunityCountryOrCityCount> countByCountry();

    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCountry();

    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCountry();

    @Query("SELECT a.country AS countryOrCityComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.country ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCountry();

    @Query("SELECT a.city AS countryOrCityComment, COUNT(o) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY COUNT(o) DESC")
    List<IOpportunityCountryOrCityCount> countByCity();

    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCity();

    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCity();

    @Query("SELECT a.city AS countryOrCityComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS countryOrCityCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.city ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCity();

    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countByIndustry();

    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countClosedWonByIndustry();

    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countClosedLostByIndustry();

    @Query("SELECT a.industry AS industryComment, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS industryCount FROM Account a " +
            "LEFT JOIN a.opportunityList o " +
            "GROUP BY a.industry ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityIndustryCount> countOpenByIndustry();
}
