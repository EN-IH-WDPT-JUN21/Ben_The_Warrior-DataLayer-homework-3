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
public class Contact{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "contact_id")
    private Integer id;
    private String name;
    private String phoneNumber;
    private String email;
    private String companyName;

    @OneToOne(mappedBy = "decisionMaker")
    private Opportunity opportunity;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;

    // ============================== CONSTRUCTOR ==============================
    public Contact(String name, String phoneNumber, String email, String companyName) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setCompanyName(companyName);
    }

    public Contact(String name, String phoneNumber, String email, String companyName, Account account) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.companyName = companyName;
        this.account = account;
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
        Contact contact = (Contact) o;
        return Objects.equals(name, contact.name) && Objects.equals(phoneNumber, contact.phoneNumber) &&
                Objects.equals(email, contact.email) && Objects.equals(companyName, contact.companyName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, phoneNumber, email, companyName);
    }

    public boolean hasNullValues() {
        return getName() == null || getPhoneNumber() == null || getEmail() == null || getCompanyName() == null;
    }
}
