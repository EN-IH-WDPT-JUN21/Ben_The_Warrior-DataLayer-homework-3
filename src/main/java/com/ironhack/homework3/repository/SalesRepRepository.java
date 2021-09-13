package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.SalesRep;
import com.ironhack.homework3.dao.queryInterfaces.ILeadsCountBySalesRep;
import com.ironhack.homework3.dao.queryInterfaces.IOpportunityCountBySalesRep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesRepRepository extends JpaRepository<SalesRep, Integer> {

    // ============================== QUERIES 1 - Reporting Functionality By SalesRep ==============================
    // 1. Report Leads by SalesRep
    @Query("SELECT s.name AS salesRepName, SUM(CASE WHEN l.id != null THEN 1 ELSE 0  END) AS leadsCount FROM Lead l "+
            "LEFT JOIN l.salesRep s "+
            "GROUP BY s " +
            "ORDER BY SUM(CASE WHEN l.id != null THEN 1 ELSE 0  END) DESC")
    List<ILeadsCountBySalesRep> countLeadsBySalesRep();

    //     2. Report Opportunities by SalesRep
    @Query("SELECT s.name AS salesRepName, COUNT(o) AS opportunitiesCount FROM Opportunity o "+
            "LEFT JOIN o.salesRep s "+
            "GROUP BY s " +
            "ORDER BY opportunitiesCount DESC")
    List<IOpportunityCountBySalesRep> countOpportunitiesBySalesRep();

    //    3. Report Opportunities that are CLOSED_WON status by SalesRep
    @Query("SELECT s.name AS salesRepName, SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) AS opportunitiesCount FROM Opportunity o "+
            "LEFT JOIN o.salesRep s "+
            "GROUP BY s " +
            "ORDER BY SUM(CASE WHEN o.status = 'CLOSED_WON' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountBySalesRep> countOpportunitiesByClosedWonBySalesRep();

    //    4. Report Opportunities that are CLOSED_LOST status by SalesRep
    @Query("SELECT s.name AS salesRepName, SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) AS opportunitiesCount FROM Opportunity o "+
            "LEFT JOIN o.salesRep s "+
            "GROUP BY s " +
            "ORDER BY SUM(CASE WHEN o.status = 'CLOSED_LOST' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountBySalesRep> countOpportunitiesByClosedLostBySalesRep();

    //    5. Report Opportunities that are OPEN status by SalesRep
    @Query("SELECT s.name AS salesRepName, SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) AS opportunitiesCount FROM Opportunity o "+
            "LEFT JOIN o.salesRep s "+
            "GROUP BY s " +
            "ORDER BY SUM(CASE WHEN o.status = 'OPEN' THEN 1 ELSE 0  END) DESC")
    List<IOpportunityCountBySalesRep> countOpportunitiesByOpenBySalesRep();

}
