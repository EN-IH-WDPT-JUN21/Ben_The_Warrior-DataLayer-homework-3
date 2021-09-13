package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Industry;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    private int employeeCount;

    private String city;

    private String country;

    @OneToMany(mappedBy = "account", cascade = {CascadeType.REMOVE})
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(mappedBy = "accountOpp", cascade = {CascadeType.REMOVE})
    private List<Opportunity> opportunityList = new ArrayList<>();


    // ============================== CONSTRUCTOR ==============================
    public Account(Industry industry, int employeeCount, String city, String country) {
        setId(id);
        setIndustry(industry);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
    }


    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Industry: " + industry + ", Number of Employees: " + employeeCount + ", City: " + city +
                ", Country: " + country + ", Number of Contacts: " + contactList.size() + ", Number of Opportunities: " + opportunityList.size();
    }

}
