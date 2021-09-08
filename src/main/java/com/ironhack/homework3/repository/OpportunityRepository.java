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



    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countByCountry();

    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCountry();

    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCountry();

    @Query("SELECT a.country AS countryOrCityComment, COUNT(a.country) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.country ORDER BY COUNT(a.country) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCountry();

    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countByCity();

    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCity();

    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCity();

    @Query("SELECT a.city AS countryOrCityComment, COUNT(a.city) AS countryOrCityCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.city ORDER BY COUNT(a.city) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCity();

    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countByIndustry();

    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countClosedWonByIndustry();

    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countClosedLostByIndustry();

    @Query("SELECT a.industry AS industryComment, COUNT(a.industry) AS industryCount FROM Opportunity o, Account a " +
            "LEFT JOIN o.accountOpp a " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY a.industry ORDER BY COUNT(a.industry) DESC")
    List<IOpportunityIndustryCount> countOpenByIndustry();


}
