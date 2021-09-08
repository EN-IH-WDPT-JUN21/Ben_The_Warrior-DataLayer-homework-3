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



    @Query("SELECT o.country AS countryOrCityComment, COUNT(o.country) AS countryOrCityCount FROM Opportunity o " +
            "GROUP BY o.country ORDER BY COUNT(o.country) DESC")
    List<IOpportunityCountryOrCityCount> countByCountry();

    @Query("SELECT o.country AS countryOrCityComment, COUNT(o.country) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY o.country ORDER BY COUNT(o.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCountry();

    @Query("SELECT o.country AS countryOrCityComment, COUNT(o.country) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY o.country ORDER BY COUNT(o.country) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCountry();

    @Query("SELECT o.country AS countryOrCityComment, COUNT(o.country) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY o.country ORDER BY COUNT(o.country) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCountry();

    @Query("SELECT o.city AS countryOrCityComment, COUNT(o.city) AS countryOrCityCount FROM Opportunity o " +
            "GROUP BY o.city ORDER BY COUNT(o.city) DESC")
    List<IOpportunityCountryOrCityCount> countByCity();

    @Query("SELECT o.city AS countryOrCityComment, COUNT(o.city) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY o.city ORDER BY COUNT(o.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedWonByCity();

    @Query("SELECT o.city AS countryOrCityComment, COUNT(o.city) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY o.city ORDER BY COUNT(o.city) DESC")
    List<IOpportunityCountryOrCityCount> countClosedLostByCity();

    @Query("SELECT o.city AS countryOrCityComment, COUNT(o.city) AS countryOrCityCount FROM Opportunity o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY o.city ORDER BY COUNT(o.city) DESC")
    List<IOpportunityCountryOrCityCount> countOpenByCity();

    @Query("SELECT o.industry AS industryComment, COUNT(o.industry) AS industryCount FROM Opportunity o " +
            "GROUP BY o.industry ORDER BY COUNT(o.industry) DESC")
    List<IOpportunityIndustryCount> countByIndustry();

    @Query("SELECT o.industry AS industryComment, COUNT(o.industry) AS industryCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_WON'" +
            "GROUP BY o.industry ORDER BY COUNT(o.industry) DESC")
    List<IOpportunityIndustryCount> countClosedWonByIndustry();

    @Query("SELECT o.industry AS industryComment, COUNT(o.industry) AS industryCount FROM Opportunity o " +
            "WHERE o.status ='CLOSED_LOST'" +
            "GROUP BY o.industry ORDER BY COUNT(o.industry) DESC")
    List<IOpportunityIndustryCount> countClosedLostByIndustry();

    @Query("SELECT o.industry AS industryComment, COUNT(o.industry) AS industryCount FROM Opportunity o " +
            "WHERE o.status ='OPEN'" +
            "GROUP BY o.industry ORDER BY COUNT(o.industry) DESC")
    List<IOpportunityIndustryCount> countOpenByIndustry();


}
