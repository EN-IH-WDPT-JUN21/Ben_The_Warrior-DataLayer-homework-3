package com.ironhack.homework3.dao.classes;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "sales_reps")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class SalesRep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sales_reps_id")
    private Integer id;

    @NotBlank
    @NotNull
    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "salesRep")
    private List<Lead> leadList;

    @OneToMany(mappedBy = "salesRep")
    private List<Opportunity> opportunityList;

    // ============================== CONSTRUCTOR ==============================
    public SalesRep(String name, List<Lead> leadList, List<Opportunity> opportunityList) {
        this.name = name;
        this.leadList = leadList;
        this.opportunityList = opportunityList;
    }

    public SalesRep(String name) {
        this.name = name;
    }

    // ============================== METHODS ==============================
    @Override
    public String toString() {
        return "Id: " + id + ", Name: " + name;
    }
}
