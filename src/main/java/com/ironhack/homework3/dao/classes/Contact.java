package com.ironhack.homework3.dao.classes;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "contact")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Contact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer id;

    @NotBlank
    @Column(name = "name")
    private String name;

    @NotBlank
    @Column(name = "phone_number")
    private String phoneNumber;

    @NotBlank
    @Column(name = "email")
    private String email;

    @NotBlank
    @Column(name = "company_name")
    private String companyName;

    @OneToOne(mappedBy = "decisionMaker",fetch = FetchType.LAZY)
    @JoinColumn(name = "opportunity_id")
    private Opportunity opportunity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    // ============================== CONSTRUCTOR ==============================


    public Contact(String name, String phoneNumber, String email, String companyName, /*Opportunity opportunity,*/ Account account) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
//        this.opportunity = opportunity;
        this.account = account;
    }

    public Contact(String name, String phoneNumber, String email, String companyName) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
    }
//    public Contact(String name, String phoneNumber, String email, String companyName) {
//        setName(name);
//        setPhoneNumber(phoneNumber);
//        setEmail(email);
//        setCompanyName(companyName);
//    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phoneNumber + ", Company: " + companyName;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Contact contact = (Contact) o;
//        return Objects.equals(name, contact.name) && Objects.equals(phoneNumber, contact.phoneNumber) &&
//                Objects.equals(email, contact.email) && Objects.equals(companyName, contact.companyName);
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
