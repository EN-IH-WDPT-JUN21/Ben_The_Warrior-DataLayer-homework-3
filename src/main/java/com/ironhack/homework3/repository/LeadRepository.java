package com.ironhack.homework3.repository;

import com.ironhack.homework3.dao.classes.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeadRepository extends JpaRepository<Lead, Integer> {

}
