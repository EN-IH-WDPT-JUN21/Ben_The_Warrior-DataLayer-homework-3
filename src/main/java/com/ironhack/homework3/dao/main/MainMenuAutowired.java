package com.ironhack.homework3.dao.main;

import com.ironhack.homework3.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.stereotype.Component;

@Component
public class MainMenuAutowired {
    final LeadRepository leadRepository;
    final ContactRepository contactRepository;
    final AccountRepository accountRepository;
    final OpportunityRepository opportunityRepository;


    @Autowired
    private Menu newMenu;

    @Autowired
    public MainMenuAutowired(LeadRepository leadRepository, ContactRepository contactRepository,
                             AccountRepository accountRepository, OpportunityRepository opportunityRepository,
                             SalesRepRepository salesRepRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.accountRepository = accountRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public void printMenu() {
        newMenu.mainMenu();
    }
}
