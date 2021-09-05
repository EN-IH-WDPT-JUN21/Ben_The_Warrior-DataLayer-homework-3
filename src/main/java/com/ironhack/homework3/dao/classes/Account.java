package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Industry;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "account")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "industry")
    private Industry industry;

    @NotNull
    @Column(name = "employee_count")
    private int employeeCount;

    @NotBlank
    @Column(name = "city")
    private String city;

    @NotBlank
    @Column(name = "country")
    private String country;

    @OneToMany(mappedBy = "account")
    private List<Contact> contactList;

    @OneToMany(mappedBy = "accountOpp")
    private List<Opportunity> opportunityList;

    // ============================== CONSTRUCTOR ==============================
    public Account(Industry industry, int employeeCount, String city, String country, List<Contact> contactList, List<Opportunity> opportunityList) {
        this.industry = industry;
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.contactList = contactList;
        this.opportunityList = opportunityList;
    }

    public Account(Industry industry, int employeeCount, String city, String country) {
        this.industry = industry;
        this.employeeCount = employeeCount;
        this.city = city;
        this.country = country;
        this.contactList = Collections.emptyList();
        this.opportunityList = Collections.emptyList();
    }

    //    public Account(Industry industry, int employeeCount, String city, String country) {
//        setId(id);
//        setIndustry(industry);
//        setEmployeeCount(employeeCount);
//        setCity(city);
//        setCountry(country);
//    }
//
//    public Account(Industry industry, int employeeCount, String city, String country, List<Contact> contactList, List<Opportunity> opportunityList) {
//        setId(id);
//        setIndustry(industry);
//        setEmployeeCount(employeeCount);
//        setCity(city);
//        setCountry(country);
//        this.contactList = contactList;
//        this.opportunityList = opportunityList;
//    }

/*    public Account(Industry industry, int employeeCount, String city, String country, Contact contact, Opportunity opportunity) {
        setId(id);
        setIndustry(industry);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        contactList = new ArrayList<>();
        contactList.add(contact);
        opportunityList = new ArrayList<>();
        opportunityList.add(opportunity);
    }*/

    // ============================== METHODS ==============================

    @Override
    public String toString() {
        return "Id: " + id + ", Industry: " + industry + ", Number of Employees: " + employeeCount + ", City: " + city +
                ", Country: " + country + ", Number of Contacts: " + contactList.size() + ", Number of Opportunities: " + opportunityList.size();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Account account = (Account) o;
//        return employeeCount == account.employeeCount && industry == account.industry &&
//                Objects.equals(city, account.city) && Objects.equals(country, account.country) &&
//                Objects.equals(contactList, account.contactList) && Objects.equals(opportunityList, account.opportunityList);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(industry, employeeCount, city, country, contactList, opportunityList);
//    }

    public boolean hasNullValues() {
        if (getIndustry() == null || getCity() == null || getCountry() == null || getContactList() == null || getOpportunityList() == null) {
            return true;
        }
        for (Opportunity opportunity : getOpportunityList()) {
            if (opportunity.hasNullValues()) {
                return true;
            }
        }
        for (Contact contact : getContactList()) {
            if (contact.hasNullValues()) {
                return true;
            }
        }
        return false;
    }
}
