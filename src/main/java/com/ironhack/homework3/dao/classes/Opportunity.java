package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Table(name = "opportunity")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Opportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "opportunity_id")
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "product")
    private Product product;

    @NotNull
    @Column(name = "quantity")
    private int quantity;

    @OneToOne
    @JoinColumn(name = "decision_maker_id")
    private Contact decisionMaker;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account accountOpp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_reps")
    private SalesRep salesRep;

    // ============================== CONSTRUCTOR ==============================

    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status, Account accountOpp, SalesRep salesRep) {
        this.product = product;
        this.quantity = quantity;
        this.decisionMaker = decisionMaker;
        this.status = status;
        this.accountOpp = accountOpp;
        this.salesRep = salesRep;
    }

    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status, SalesRep salesRep) {
        this.product = product;
        this.quantity = quantity;
        this.decisionMaker = decisionMaker;
        this.status = status;
        this.salesRep = salesRep;
    }

    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status) {
        this.product = product;
        this.quantity = quantity;
        this.decisionMaker = decisionMaker;
        this.status = status;
    }

    //    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status) {
//        setProduct(product);
//        setQuantity(quantity);
//        setDecisionMaker(decisionMaker);
//        setStatus(status);
//    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Product: " + product + ", Quantity: " + quantity + ", Decision Maker: " +
                decisionMaker.getName() + "Sales Representative: " + salesRep.getName() + ", Status: " + status;
    }

//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Opportunity that = (Opportunity) o;
//        return quantity == that.quantity && product == that.product &&
//                Objects.equals(decisionMaker, that.decisionMaker) && status == that.status;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(product, quantity, decisionMaker, status);
//    }

    public boolean hasNullValues() {
        return getProduct() == null || getStatus() == null || getDecisionMaker().hasNullValues();
    }
}
