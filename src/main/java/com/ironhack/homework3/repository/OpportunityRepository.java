package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Opportunity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OpportunityRepository extends JpaRepository<Opportunity, Integer> {

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.accountOpp a WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedAccount(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.decisionMaker c WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedContact(int id);

    // Use this if Contact's fetch = FetchType.LAZY else you can use the predefined findById(int id)
    @Query("SELECT o FROM Opportunity o LEFT JOIN FETCH o.salesRep sr WHERE o.id = :id")
    Optional<Opportunity> findByIdJoinedSalesRep(int id);


    // ==================== QUERIES by EmployeeCount States ====================
    @Query("SELECT AVG(o.quantity) FROM Opportunity o")
    double meanQuantity();

//    // Complex median sql query source: https://sqlperformance.com/2012/08/t-sql-queries/median
//    @Query(value = "SELECT AVG(1.0 * quantity) " +
//            "FROM (" +
//                    "SELECT TOP( " +
//                            "(((SELECT COUNT(opportunity_id) FROM opportunity) - 1) / 2) + (1 + (1 - (SELECT COUNT(opportunity_id) FROM opportunity) % 2)) ) quantity, " +
//                            "ROW_NUMBER() OVER (ORDER BY quantity) AS r " +
//                    "FROM opportunity o " +
//                    "ORDER BY quantity" +
//            ") p " +
//            "WHERE r BETWEEN (((SELECT COUNT(opportunity_id) FROM opportunity a)- 1) / 2) + 1 " +
//            "AND ((((SELECT COUNT(opportunity_id) FROM opportunity)- 1) / 2) + (1 + (1 - (SELECT COUNT(opportunity_id) FROM opportunity) % 2)))",
//            nativeQuery = true)
////    @Query(value = "    SELECT ((" +
////            "SELECT MAX(quantity) FROM (SELECT TOP 50 PERCENT quantity FROM opportunity ORDER BY quantity, opportunity_id) AS t) + " +
////            "SELECT MIN(quantity) FROM (SELECT TOP 50 PERCENT quantity FROM opportunity ORDER BY quantity DESC, opportunity_id DESC) AS b)) " +
////            "/ 2.0;",
////            nativeQuery = true)
//    double medianQuantity();

    @Query("SELECT MIN(o.quantity) FROM Opportunity o")
    int minQuantity();

    @Query("SELECT MAX(o.quantity) FROM Opportunity o")
    int maxQuantity();


    // ==================== QUERIES by (Account) Opportunities States ====================
    @Query(value = "SELECT AVG(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    double meanOpportunities();

//    //    // Complex median sql query source: https://sqlperformance.com/2012/08/t-sql-queries/median
//    @Query(value = "SELECT AVG(1.0 * quantity) " +
//            "FROM (" +
//            "SELECT TOP( " +
//            "(((SELECT COUNT(opportunity_id) FROM opportunity) - 1) / 2) + (1 + (1 - (SELECT COUNT(opportunity_id) FROM opportunity) % 2)) ) quantity, " +
//            "ROW_NUMBER() OVER (ORDER BY quantity) AS r " +
//            "FROM opportunity o " +
//            "ORDER BY quantity" +
//            ") p " +
//            "WHERE r BETWEEN (((SELECT COUNT(opportunity_id) FROM opportunity a)- 1) / 2) + 1 " +
//            "AND ((((SELECT COUNT(opportunity_id) FROM opportunity)- 1) / 2) + (1 + (1 - (SELECT COUNT(opportunity_id) FROM opportunity) % 2)))",
//            nativeQuery = true)
//    double medianOpportunities();

    @Query(value = "SELECT MIN(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int minOpportunities();

    @Query(value = "SELECT MAX(a.count) FROM (SELECT count(*) AS count FROM opportunity GROUP BY account_id HAVING account_id IS NOT NULL) a", nativeQuery = true)
    int maxOpportunities();

}
