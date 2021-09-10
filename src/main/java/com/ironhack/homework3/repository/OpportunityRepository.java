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


}
