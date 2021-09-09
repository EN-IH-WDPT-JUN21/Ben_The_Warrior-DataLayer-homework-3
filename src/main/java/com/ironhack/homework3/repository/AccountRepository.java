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
