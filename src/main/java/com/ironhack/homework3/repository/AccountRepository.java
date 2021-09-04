package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

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

    // Complex median sql query source: https://sqlperformance.com/2012/08/t-sql-queries/median
    @Query(value = "SELECT AVG(1.0 * employee_count) " +
            "FROM (" +
                    "SELECT TOP( " +
                            "(((SELECT COUNT(account_id) FROM account) - 1) / 2) + (1 + (1 - (SELECT COUNT(account_id) FROM account) % 2)) ) employee_count, " +
                            "ROW_NUMBER() OVER (ORDER BY employee_count) AS r " +
                    "FROM account a " +
                    "ORDER BY employee_count" +
            ") p " +
            "WHERE r BETWEEN (((SELECT COUNT(account_id) FROM account a)- 1) / 2) + 1 " +
            "AND ((((SELECT COUNT(account_id) FROM account)- 1) / 2) + (1 + (1 - (SELECT COUNT(account_id) FROM account) % 2)))",
            nativeQuery = true)
    double medianEmployeeCount();

    @Query("SELECT MIN(a.employeeCount) FROM Account a")
    int minEmployeeCount();

    @Query("SELECT MAX(a.employeeCount) FROM Account a")
    int maxEmployeeCount();


}
