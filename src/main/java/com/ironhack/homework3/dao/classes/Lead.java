package com.ironhack.homework3.dao.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table(name = "leads")
public class Lead {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id")
    private Integer id;
    private String name;
    private String phoneNumber;
    private String email;
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    SalesRep salesRep;

    // ============================== CONSTRUCTOR ==============================
    public Lead(String name, String phoneNumber, String email, String companyName) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setCompanyName(companyName);
    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phoneNumber + ", Company: " + companyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(name, lead.name) && Objects.equals(phoneNumber, lead.phoneNumber) &&
                Objects.equals(email, lead.email) && Objects.equals(companyName, lead.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, email, companyName);
    }

    public boolean hasNullValues() {
        return getName() == null || getPhoneNumber() == null || getEmail() == null || getCompanyName() == null;
    }
}
