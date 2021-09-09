package com.ironhack.homework3.dao.classes;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalesRep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

    @OneToMany(mappedBy = "salesRep")
    List<Lead> leadList;

    @OneToMany(mappedBy = "salesRep")
    List<Opportunity> opportunityList;

    public SalesRep(String name){
        setName(name);
    }
}
