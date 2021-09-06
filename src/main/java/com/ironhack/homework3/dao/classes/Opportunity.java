package com.ironhack.homework3.dao.classes;

import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
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
public class Opportunity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="opportunity_id")
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Product product;

    @Enumerated(EnumType.STRING)
    private Industry industry;

    private int quantity;

    @OneToOne
    @JoinColumn(name="decision_maker_id")
    private Contact decisionMaker;

    @Enumerated(EnumType.STRING)
    private Status status;

    private String country;
    private String city;

    @ManyToOne
    @JoinColumn(name="account_id", referencedColumnName = "account_id")
    private Account accountOpp;

    // ============================== CONSTRUCTOR ==============================

    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status) {
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setStatus(status);
    }

    public Opportunity(Product product, int quantity, Contact decisionMaker, Status status, String country, String city) {
        setProduct(product);
        setQuantity(quantity);
        setDecisionMaker(decisionMaker);
        setStatus(status);
        this.country = country;
        this.city = city;
    }

    public Opportunity(Integer id, Product product, int quantity, Contact decisionMaker, Status status, String country, String city) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
        this.decisionMaker = decisionMaker;
        this.status = status;
        this.country = country;
        this.city = city;
    }

    public Opportunity(Product product, Industry industry, int quantity, Contact decisionMaker, Status status, String country, String city) {
        this.product = product;
        this.industry = industry;
        this.quantity = quantity;
        this.decisionMaker = decisionMaker;
        this.status = status;
        this.country = country;
        this.city = city;
    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Product: " + product + ", Quantity: " + quantity + ", Decision Maker: " +
                decisionMaker.getName() + ", Status: " + status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Opportunity that = (Opportunity) o;
        return quantity == that.quantity && product == that.product &&
                Objects.equals(decisionMaker, that.decisionMaker) && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(product, quantity, decisionMaker, status);
    }

    public boolean hasNullValues(){
        return getProduct() == null || getStatus() == null || getDecisionMaker().hasNullValues();
    }
}
