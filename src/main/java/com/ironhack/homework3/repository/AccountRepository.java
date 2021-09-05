package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.persistence.NamedQueries;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.contactList c WHERE a.id = :id")
    Optional<Account> findByIdJoinedContact(int id);

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.opportunityList o WHERE a.id = :id")
    Optional<Account> findByIdJoinedOpportunity(int id);


    // ==================== QUERIES by EmployeeCount States ====================
    @Query("SELECT AVG(a.employeeCount) FROM Account a")
    double meanEmployeeCount();

//    @Query(value = "SET @rowindex = -1; " +
//            "SELECT AVG(c.count) as Median " +
//            "FROM(" +
//                    "SELECT @rowindex = @rowindex + 1 AS rowindex, " +
//                            "account.employee_count AS count " +
//                    "FROM account " +
//                    "ORDER BY account.employee_count) AS c " +
//            "WHERE c.rowindex IN (FLOOR(@rowindex / 2), CEIL(@rowindex / 2));",
//            nativeQuery = true)
//    @NamedQueries({})
//    double medianEmployeeCount();

    @Query("SELECT MIN(a.employeeCount) FROM Account a")
    int minEmployeeCount();

    @Query("SELECT MAX(a.employeeCount) FROM Account a")
    int maxEmployeeCount();


}
