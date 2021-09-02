package com.ironhack.homework3.dao.main;

import com.ironhack.homework3.repository.AccountRepository;
import com.ironhack.homework3.repository.ContactRepository;
import com.ironhack.homework3.repository.LeadRepository;
import com.ironhack.homework3.repository.OpportunityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenuAutowired {
    final LeadRepository leadRepository;
    final ContactRepository contactRepository;
    final AccountRepository accountRepository;
    final OpportunityRepository opportunityRepository;


    @Autowired
    Menu newMenu;

    @Autowired
    public MainMenuAutowired(LeadRepository leadRepository, ContactRepository contactRepository, AccountRepository accountRepository, OpportunityRepository opportunityRepository) {
        this.leadRepository = leadRepository;
        this.contactRepository = contactRepository;
        this.accountRepository = accountRepository;
        this.opportunityRepository = opportunityRepository;
    }

    public void printMenu() {
        newMenu.mainMenu();
    }
}
