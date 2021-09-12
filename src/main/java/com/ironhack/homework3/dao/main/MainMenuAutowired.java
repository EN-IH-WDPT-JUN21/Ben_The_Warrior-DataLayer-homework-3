package com.ironhack.homework3.dao.main;

import com.ironhack.homework3.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainMenuAutowired {
    @Autowired
    private Menu newMenu;

    public void printMenu() {
        newMenu.mainMenu();
    }
}
