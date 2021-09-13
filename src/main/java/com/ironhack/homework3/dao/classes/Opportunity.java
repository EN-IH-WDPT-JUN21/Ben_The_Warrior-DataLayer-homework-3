package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
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
public class Opportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunity_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Product product;

    private int quantity;

    @OneToOne
    @JoinColumn(name = "decision_maker_id")
    private Contact decisionMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "account_id", referencedColumnName = "account_id")
    private Account accountOpp;

    @ManyToOne
    @JoinColumn(name = "sales_rep_id")
    private SalesRep salesRep;


    // ============================== CONSTRUCTOR ==============================
    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status, Account account, SalesRep salesRep) {
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setStatus(status);
        setAccountOpp(account);
        setSalesRep(salesRep);
    }


    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Product: " + product + ", Quantity: " + quantity + ", Decision Maker: " +
                decisionMaker.getName() + ", Status: " + status + ", Sales Representative: " + salesRep.getName();
    }

}
