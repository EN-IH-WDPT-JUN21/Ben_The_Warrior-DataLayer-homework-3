package com.ironhack.homework3.dao.classes;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "leads")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Lead {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lead_id")
    private Integer id;

    @NotBlank
    @NotNull
    @Column(name = "name")
    private String name;

    @NotBlank
    @NotNull
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @NotNull
    @Column(name = "email")
    private String email;

    @NotBlank
    @NotNull
    @Column(name = "company_name")
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "sales_reps")
    private SalesRep salesRep;

    // ============================== CONSTRUCTOR ==============================
    public Lead(String name, String phoneNumber, String email, String companyName, SalesRep salesRep) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
        this.salesRep = salesRep;
    }

    public Lead(String name, String phoneNumber, String email, String companyName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
    }

    // TODO-JA Check if needed after removing tests from previous homework
    public Lead(Integer id, String name, String phoneNumber, String email, String companyName) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phoneNumber + ", Company: " +
                companyName + "Sales Representative: " + salesRep.getName();
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Lead lead = (Lead) o;
//        return Objects.equals(name, lead.name) && Objects.equals(phoneNumber, lead.phoneNumber) &&
//                Objects.equals(email, lead.email) && Objects.equals(companyName, lead.companyName);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(name, phoneNumber, email, companyName);
//    }

    public boolean hasNullValues() {
        return getName() == null || getPhoneNumber() == null || getEmail() == null || getCompanyName() == null;
    }
}
