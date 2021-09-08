package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Industry;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="account_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    private int employeeCount;
    private String city;
    private String country;

    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private List<Contact> contactList = new ArrayList<>();

    @OneToMany(mappedBy = "accountOpp")
    private List<Opportunity> opportunityList = new ArrayList<>();

//    private ArrayList<Contact> contactList;
//    private ArrayList<Opportunity> opportunityList;
    // ============================== CONSTRUCTOR ==============================
    public Account(Industry industry, int employeeCount, String city, String country) {
        setId(id);
        setIndustry(industry);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
    }

    public Account(Industry industry, int employeeCount, String city, String country, List<Contact> contactList, List<Opportunity> opportunityList) {
        setId(id);
        setIndustry(industry);
        setEmployeeCount(employeeCount);
        setCity(city);
        setCountry(country);
        this.contactList = contactList;
        this.opportunityList = opportunityList;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return employeeCount == account.employeeCount && industry == account.industry &&
                Objects.equals(city, account.city) && Objects.equals(country, account.country) &&
                Objects.equals(contactList, account.contactList) && Objects.equals(opportunityList, account.opportunityList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(industry, employeeCount, city, country, contactList, opportunityList);
    }

    public boolean hasNullValues(){
        if (getIndustry() == null || getCity() == null || getCountry() == null || getContactList() == null || getOpportunityList() == null){
            return true;
        }
        for (Opportunity opportunity : getOpportunityList()){
            if (opportunity.hasNullValues()){
                return true;
            }
        }
        for (Contact contact : getContactList()){
            if (contact.hasNullValues()){
                return true;
            }
        }
        return false;
    }
}
