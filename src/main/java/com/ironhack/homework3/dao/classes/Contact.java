package com.ironhack.homework3.dao.classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Contact {

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
    public Contact(String name, String phoneNumber, String email, String companyName, Account account) {
        setName(name);
        setPhoneNumber(phoneNumber);
        setEmail(email);
        setCompanyName(companyName);
        setAccount(account);
    }


    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name + ", Email: " + email + ", Phone: " + phoneNumber + ", Company: " + companyName;
    }

}
