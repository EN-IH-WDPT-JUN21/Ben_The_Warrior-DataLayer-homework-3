package com.ironhack.homework3.dao.main;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.dao.queryInterfaces.*;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import com.ironhack.homework3.utils.DatabaseUtility;
import com.ironhack.homework3.utils.Printer;
import com.ironhack.homework3.utils.PrinterMenu;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.*;

import static com.ironhack.homework3.utils.Utils.*;
import static com.ironhack.homework3.utils.Utils.validLocation;

@Component
@Getter
@Setter
public class Menu {

    private final Scanner scanner;

    @Autowired
    private DatabaseUtility db;
    //private final JsonDatabaseUtility db; TODO remove if not necessary

    // Variable to check if the user asked for the available commands
    private boolean showHelp;
    private boolean showAllHelp;

    public Menu() {
        scanner = new Scanner(System.in);
        setShowHelp(false);
        setShowAllHelp(false);
    }

/*    private static final Menu menu = new Menu();

    public void printMenu() {
        menu.mainMenu();
    }*/
/*
    public void testLead() {
        var lead = new Lead(1, "Ula", "4346745", "sddf@dfgf.com", "San");
        leadRepository.save(lead);
        var allLeads = leadRepository.findAll();
        for (var c : allLeads) System.out.println("Our menu includes: " + c.getName());
    }*/

    public Menu(InputStream inputStream) {
        scanner = new Scanner(inputStream);
        db = new DatabaseUtility();
        setShowHelp(false);
        setShowAllHelp(false);
    }


    // Core method of the application. This method is running while the app is running and only returns when closing the app
    public void mainMenu() {
        String input;
        boolean running = true;
        showHelp = false;
        while (running) {
            // if the user asked for available commands print help menu, otherwise print main menu
            if (isShowHelp()) {
                PrinterMenu.printMenu("help");
                setShowHelp(false);
            } else if (isShowAllHelp()) {
                PrinterMenu.printMenu("help -a");
                setShowAllHelp(false);
            } else {
                PrinterMenu.printMenu("main");
            }
            // get a user input, if it is valid compute the command otherwise print a warning message
            input = scanner.nextLine();
            if (validCommand(input)) {
                PrinterMenu.clearWarning();
                running = computeCommand(input);
            } else {
                PrinterMenu.setWarning("There is no such command as \"" + input + "\"! To see the list of available commands type help!");
            }
        }
    }

    // Method to compute commands after being validated
    public boolean computeCommand(String input) {
        String cleanInput = input.trim().toLowerCase();
        String[] inputArray = cleanInput.split(" ");
        // commands are computed word by word and the appropriate method is called
        switch (inputArray[0]) {
            case "new":
                if (inputArray[1].equals("lead")) {
                    promptLead();
                } else if (inputArray[1].equals("salesrep")) {
                    promptSalesRep();
                }
                break;
            case "show":
                switch (inputArray[1]) {
                    case "leads":
                    case "opportunities":
                    case "contacts":
                    case "accounts":
                    case "salesreps":
                        showMenu(inputArray[1]);
                        break;
                }
                break;
            case "lookup":
                switch (inputArray[1]) {
                    case "lead":
                        try {
                            // If there is no lead an error is thrown
                            // Otherwise the command can be correctly computed and the warning messages can be cleared
                            Lead lead = db.lookupLeadId(Integer.parseInt(inputArray[2]));
                            PrinterMenu.clearWarning();
                            PrinterMenu.lookupObject(lead);
                            promptDecision("enter");
                        } catch (IllegalArgumentException e) {
                            PrinterMenu.setWarning(e.getMessage());
                        }
                        break;
                    case "opportunity":
                        try {
                            // If there is no opportunity an error is thrown
                            // Otherwise the command can be correctly computed and the warning messages can be cleared
                            Opportunity opportunity = db.lookupOpportunityId(Integer.parseInt(inputArray[2]));
                            PrinterMenu.clearWarning();
                            PrinterMenu.lookupObject(opportunity);
                            boolean decision = promptDecision("enter back");
                            if (decision) {
                                PrinterMenu.lookupObject(opportunity, "contact");
                                promptDecision("enter");
                            }
                        } catch (IllegalArgumentException e) {
                            PrinterMenu.setWarning(e.getMessage());
                        }

                        break;
                    case "contact":
                        try {
                            // If there is no contact an error is thrown
                            // Otherwise the command can be correctly computed and the warning messages can be cleared
                            Contact contact = db.lookupContactId(Integer.parseInt(inputArray[2]));
                            PrinterMenu.clearWarning();
                            PrinterMenu.lookupObject(contact);
                            promptDecision("enter");
                        } catch (IllegalArgumentException e) {
                            PrinterMenu.setWarning(e.getMessage());
                        }
                        break;
                    case "account":
                        try {
                            // If there is no account an error is thrown
                            // Otherwise the command can be correctly computed and the warning messages can be cleared
                            Account account = db.lookupAccountId(Integer.parseInt(inputArray[2]));
                            PrinterMenu.clearWarning();
                            lookupAccountMenu(account);
                        } catch (IllegalArgumentException e) {
                            PrinterMenu.setWarning(e.getMessage());
                        }
                        break;
                    case "salesrep":
                        try {
                            // If there is no SalesRep an error is thrown
                            // Otherwise the command can be correctly computed and the warning messages can be cleared
                            SalesRep salesRep = db.lookupSalesRepId(Integer.parseInt(inputArray[2]));
                            PrinterMenu.clearWarning();
                            PrinterMenu.lookupObject(salesRep);
                            promptDecision("enter");
                        } catch (IllegalArgumentException e) {
                            PrinterMenu.setWarning(e.getMessage());
                        }
                        break;
                    default:
                        break;
                }
                break;
            case "convert":
                promptConvert(Integer.parseInt(inputArray[1]));
                break;
            case "close-won":
                try {
                    // If there is no opportunity an error is thrown
                    // Otherwise the command can be correctly computed and the warning messages can be cleared
                    Opportunity opportunity = db.lookupOpportunityId(Integer.parseInt(inputArray[1]));
                    opportunity.setStatus(Status.CLOSED_WON);
                } catch (IllegalArgumentException e) {
                    PrinterMenu.setWarning(e.getMessage());
                }
                break;
            case "close-lost":
                try {
                    // If there is no opportunity an error is thrown
                    // Otherwise the command can be correctly computed and the warning messages can be cleared
                    Opportunity opportunity = db.lookupOpportunityId(Integer.parseInt(inputArray[1]));
                    opportunity.setStatus(Status.CLOSED_LOST);
                } catch (IllegalArgumentException e) {
                    PrinterMenu.setWarning(e.getMessage());
                }
                break;

            case "report":
            case "mean":
            case "median":
            case "max":
            case "min":
                switch (cleanInput) {
                    // BY SALESREP
                    case "report lead by salesrep":
                        showFromInterface("Count of Leads by SalesRep", db.getLeadsCountBySalesRep());
                        promptDecision("enter");
                        break;
                    case "report opportunity by salesrep":
                        showFromInterface("Count of Opportunities by SalesRep", db.getOpportunitiesCountBySalesRep());
                        promptDecision("enter");
                        break;
                    case "report closed-won by salesrep":
                        showFromInterface("Count of CLOSED-WON Opportunities by SalesRep", db.getOpportunitiesCountBySalesRep_With_ClosedWonStatus());
                        promptDecision("enter");
                        break;
                    case "report closed-lost by salesrep":
                        showFromInterface("Count of CLOSED-LOST Opportunities by SalesRep", db.getOpportunitiesCountBySalesRep_With_ClosedLostStatus());
                        promptDecision("enter");
                        break;
                    case "report open by salesrep":
                        showFromInterface("Count of OPEN Opportunities by SalesRep", db.getOpportunitiesCountBySalesRep_With_OpenStatus());
                        promptDecision("enter");
                        break;

                    // BY PRODUCT
                    case "report opportunity by product":
                        showFromInterface("Count of Opportunities by Product", db.getCountByProduct());
                        promptDecision("enter");
                        break;
                    case "report closed-won by product":
                        showFromInterface("Count of CLOSED-WON Opportunities by Product", db.getCountByProduct_With_ClosedWonStatus());
                        promptDecision("enter");
                        break;
                    case "report closed-lost by product":
                        showFromInterface("Count of CLOSED-LOST Opportunities by Product", db.getCountByProduct_With_ClosedLostStatus());
                        promptDecision("enter");
                        break;
                    case "report open by product":
                        showFromInterface("Count of OPEN Opportunities by Product", db.getCountByProduct_With_OpenStatus());
                        promptDecision("enter");
                        break;

                    // BY COUNTRY
                    case "report opportunity by country":
                        showFromInterface("Count of Opportunities by Country", db.getCountByCountry());
                        promptDecision("enter");
                        break;
                    case "report closed-won by country":
                        showFromInterface("Count of CLOSED_WON Opportunities by Country", db.getCountClosedWonByCountry());
                        promptDecision("enter");
                        break;
                    case "report closed-lost by country":
                        showFromInterface("Count of CLOSED_LOST Opportunities by Country", db.getCountClosedLostByCountry());
                        promptDecision("enter");
                        break;
                    case "report open by country":
                        showFromInterface("Count of OPEN Opportunities by Country", db.getCountOpenByCountry());
                        promptDecision("enter");
                        break;

                    // BY CITY
                    case "report opportunity by city":
                        showFromInterface("Count of Opportunities by City", db.getCountByCity());
                        promptDecision("enter");
                        break;
                    case "report closed-won by city":
                        showFromInterface("Count of CLOSED_WON Opportunities by City", db.getCountClosedWonByCity());
                        promptDecision("enter");
                        break;
                    case "report closed-lost by city":
                        showFromInterface("Count of CLOSED_LOST Opportunities by City", db.getCountClosedLostByCity());
                        promptDecision("enter");
                        break;
                    case "report open by city":
                        showFromInterface("Count of OPEN Opportunities by City", db.getCountOpenByCity());
                        promptDecision("enter");
                        break;

                    // BY INDUSTRY
                    case "report opportunity by industry":
                        showFromInterface("Count of Opportunities by Industry", db.getCountByIndustry());
                        promptDecision("enter");
                        break;
                    case "report closed-won by industry":
                        showFromInterface("Count of CLOSED_WON Opportunities by Industry", db.getCountClosedWonByIndustry());
                        promptDecision("enter");
                        break;
                    case "report closed-lost by industry":
                        showFromInterface("Count of CLOSED_LOST Opportunities by Industry", db.getCountClosedLostByIndustry());
                        promptDecision("enter");
                        break;
                    case "report open by industry":
                        showFromInterface("Count of OPEN Opportunities by Industry", db.getCountOpenByIndustry());
                        promptDecision("enter");
                        break;

                    // EMPLOYEECOUNT STATES
                    case "mean employeecount":
                        PrinterMenu.printQueryStat("Mean Employee Count", db.getMeanEmployeeCount());
                        promptDecision("enter");
                        break;
                    case "median employeecount":
                        PrinterMenu.printQueryStat("Median Employee Count", db.getMedianEmployeeCount());
                        promptDecision("enter");
                        break;
                    case "max employeecount":
                        PrinterMenu.printQueryStat("Max Employee Count", db.getMaxEmployeeCount());
                        promptDecision("enter");
                        break;
                    case "min employeecount":
                        PrinterMenu.printQueryStat("Min Employee Count", db.getMinEmployeeCount());
                        promptDecision("enter");
                        break;

                    // QUANTITY STATES
                    case "mean quantity":
                        PrinterMenu.printQueryStat("Mean Quantity", db.getMeanQuantity());
                        promptDecision("enter");
                        break;
                    case "median quantity":
                        System.out.println(db.getMedianQuantity());
                        PrinterMenu.printQueryStat("Median Quantity", db.getMedianQuantity());
                        promptDecision("enter");
                        break;
                    case "max quantity":
                        System.out.println(db.getMaxQuantity());
                        PrinterMenu.printQueryStat("Max Quantity", db.getMaxQuantity());
                        promptDecision("enter");
                        break;
                    case "min quantity":
                        System.out.println(db.getMinQuantity());
                        PrinterMenu.printQueryStat("Min Quantity", db.getMinQuantity());
                        promptDecision("enter");
                        break;

                    // OPPORTUNITY STATES
                    case "mean opps per account":
                        System.out.println(db.getMeanOppsPerAccount());
                        PrinterMenu.printQueryStat("Mean Opps per Account", db.getMeanOppsPerAccount());
                        promptDecision("enter");
                        break;
                    case "median opps per account":
                        System.out.println(db.getMedianOppsPerAccount());
                        PrinterMenu.printQueryStat("Median Opps per Account", db.getMedianOppsPerAccount());
                        promptDecision("enter");
                        break;
                    case "max opps per account":
                        System.out.println(db.getMaxOppsPerAccount());
                        PrinterMenu.printQueryStat("Max Opps per Account", db.getMaxOppsPerAccount());
                        promptDecision("enter");
                        break;
                    case "min opps per account":
                        System.out.println(db.getMinOppsPerAccount());
                        PrinterMenu.printQueryStat("Min Opps per Account", db.getMinOppsPerAccount());
                        promptDecision("enter");

                        break;

                    default:
                        break;
                }
                break;
            // show help menu with all available commands
            case "help":
                if (inputArray.length == 2) {
                    if (inputArray[1].equals("-a"))
                        setShowAllHelp(true);
                } else
                    setShowHelp(true);
                break;
            case "exit":
                PrinterMenu.printMenu("exit");
                return false;
            default:
                break;
        }
        return true;
    }

    // Method to create the menu when looking up an account
    private void lookupAccountMenu(Account account) {
        PrinterMenu.lookupObject(account);
        // Allow user to see list of contacts and opportunities n the looked up account
        while (true) {
            int answer = promptMultipleDecisions("contacts", "opportunities", "back");
            switch (answer) {
                case 0:
                    showFromListMenu(account.getContactList(), "contacts");
                    PrinterMenu.lookupObject(account);
                    break;
                case 1:
                    showFromListMenu(account.getOpportunityList(), "opportunities");
                    PrinterMenu.lookupObject(account);
                    break;
                case 2:
                    return;
            }
        }
    }

    // Method to create the menu showing all available leads
    private void showMenu(String objectType) {
        int maxElements = PrinterMenu.getPrintMultipleObjectsMax();
        int currentIndex = 0;
        int currentPage = 0;
        int numPages;
        switch (objectType.toLowerCase()) {
            case "leads":
                List<Lead> leadList = db.getAllLeads();
                if (leadList.size() > 0) {
                    List<ArrayList<Lead>> listList = new ArrayList<>();
                    listList.add(new ArrayList<>());
                    for (Lead lead : leadList) {
                        if (currentIndex + Printer.numberOfTextRows(lead.toString()) < maxElements) {
                            currentIndex = currentIndex + Printer.numberOfTextRows(lead.toString());
                            listList.get(currentPage).add(lead);
                        } else {
                            listList.add(new ArrayList<>());
                            listList.get(++currentPage).add(lead);
                        }
                    }
                    currentPage = 0;
                    numPages = listList.size();
                    while (true) {
                        PrinterMenu.showLeads(listList.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){
                            return;
                        }
                    }
                } else {
                    PrinterMenu.showLeads(new ArrayList<Lead>(), true, true);
                    promptDecision("enter");
                    return;
                }
            case "contacts":
                List<Contact> contactList = db.getAllContacts();
                if (contactList.size() > 0) {
                    List<ArrayList<Contact>> listList = new ArrayList<>();
                    listList.add(new ArrayList<>());
                    for (Contact contact : contactList) {
                        if (currentIndex + Printer.numberOfTextRows(contact.toString()) < maxElements) {
                            currentIndex = currentIndex + Printer.numberOfTextRows(contact.toString());
                            listList.get(currentPage).add(contact);
                        } else {
                            listList.add(new ArrayList<>());
                            listList.get(++currentPage).add(contact);
                        }
                    }
                    currentPage = 0;
                    numPages = listList.size();
                    while (true) {
                        PrinterMenu.showContacts(listList.get(currentPage), currentPage == 0, currentPage + 1 == numPages, false);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){
                            return;
                        }
                    }
                } else {
                    PrinterMenu.showContacts(new ArrayList<Contact>(), true, true, false);
                    promptDecision("enter");
                    return;
                }
            case "opportunities":
                List<Opportunity> opportunityList = db.getAllOpportunities();
                if (opportunityList.size() > 0) {
                    List<ArrayList<Opportunity>> listList = new ArrayList<>();
                    listList.add(new ArrayList<>());
                    for (Opportunity opportunity : opportunityList) {
                        if (currentIndex + Printer.numberOfTextRows(opportunity.toString()) < maxElements) {
                            currentIndex = currentIndex + Printer.numberOfTextRows(opportunity.toString());
                            listList.get(currentPage).add(opportunity);
                        } else {
                            listList.add(new ArrayList<>());
                            listList.get(++currentPage).add(opportunity);
                        }
                    }
                    currentPage = 0;
                    numPages = listList.size();
                    while (true) {
                        PrinterMenu.showOpportunities(listList.get(currentPage), currentPage == 0, currentPage + 1 == numPages, false);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){
                            return;
                        }
                    }
                } else {
                    PrinterMenu.showOpportunities(new ArrayList<Opportunity>(), true, true, false);
                    promptDecision("enter");
                    return;
                }
            case "accounts":
                List<Account> accountList = db.getAllAccounts();
                if (accountList.size() > 0) {
                    List<ArrayList<Account>> listList = new ArrayList<>();
                    listList.add(new ArrayList<>());
                    for (Account account : accountList) {
                        if (currentIndex + Printer.numberOfTextRows(account.toString()) < maxElements) {
                            currentIndex = currentIndex + Printer.numberOfTextRows(account.toString());
                            listList.get(currentPage).add(account);
                        } else {
                            listList.add(new ArrayList<>());
                            listList.get(++currentPage).add(account);
                        }
                    }
                    currentPage = 0;
                    numPages = listList.size();
                    while (true) {
                        PrinterMenu.showAccounts(listList.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1) {
                            return;
                        }
                    }
                } else {
                    PrinterMenu.showAccounts(new ArrayList<Account>(), true, true);
                    promptDecision("enter");
                    return;
                }

            case "salesreps":
                List<SalesRep> salesRepList = db.getAllSalesRep();
                if (salesRepList.size() > 0) {
                    List<ArrayList<SalesRep>> listList = new ArrayList<>();
                    listList.add(new ArrayList<>());
                    for (SalesRep salesRep : salesRepList) {
                        if (currentIndex + Printer.numberOfTextRows(salesRep.toString()) < maxElements) {
                            currentIndex = currentIndex + Printer.numberOfTextRows(salesRep.toString());
                            listList.get(currentPage).add(salesRep);
                        } else {
                            listList.add(new ArrayList<>());
                            listList.get(++currentPage).add(salesRep);
                        }
                    }
                    currentPage = 0;
                    numPages = listList.size();
                    while (true) {
                        PrinterMenu.showSalesRep(listList.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){
                            return;
                        }
                    }
                } else {
                    PrinterMenu.showSalesRep(new ArrayList<SalesRep>(), true, true);
                    promptDecision("enter");
                    return;
                }

            default:
                throw new IllegalArgumentException("There is no implementation of show method to the object type " + objectType);
        }
    }

    // Method to create the menu showing all available Contacts in a List
    private void showFromListMenu(List<?> list, String objectType) {
        // List is separated in multiple lists (pages)
        int maxElements = PrinterMenu.getPrintMultipleObjectsMax();
        int currentPage = 0;
        int currentIndex = 0;
        int numPages;
        switch (objectType.toLowerCase().trim()){
            case "contacts":
                if (list.size() == 0){
                    PrinterMenu.showContacts(new ArrayList<>(), true, true, true);
                    promptDecision("enter");
                    return;
                } else {
                    List<ArrayList<Contact>> listListContact = new ArrayList<>();
                    listListContact.add(new ArrayList<>());
                    try {
                        for (Object object : list) {
                            if (currentIndex++ < maxElements) {
                                listListContact.get(currentPage).add((Contact) object);
                            } else {
                                listListContact.add(new ArrayList<>());
                                listListContact.get(++currentPage).add((Contact) object);
                            }
                        }
                    } catch (ClassCastException e){
                        PrinterMenu.setWarning("Could not cast Object as Contact");
                    }
                    // Allow user to change between the pages
                    numPages = listListContact.size();
                    while (true) {
                        PrinterMenu.showContacts(listListContact.get(currentPage), currentPage == 0, currentPage + 1 == numPages, true);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){ return; }
                    }
                }
            case "opportunities":
                if (list.size() == 0){
                    PrinterMenu.showContacts(new ArrayList<>(), true, true, true);
                    promptDecision("enter");
                    return;
                } else {
                    List<ArrayList<Opportunity>> listListOpportunity = new ArrayList<>();
                    listListOpportunity.add(new ArrayList<>());
                    try {
                        for (Object object : list) {
                            if (currentIndex++ < maxElements) {
                                listListOpportunity.get(currentPage).add((Opportunity) object);
                            } else {
                                listListOpportunity.add(new ArrayList<>());
                                listListOpportunity.get(++currentPage).add((Opportunity) object);
                            }
                        }
                    } catch (ClassCastException e){
                        PrinterMenu.setWarning("Could not cast Object as Contact");
                    }
                    // Allow user to change between the pages
                    numPages = listListOpportunity.size();
                    while (true) {
                        PrinterMenu.showOpportunities(listListOpportunity.get(currentPage), currentPage == 0, currentPage + 1 == numPages, true);
                        currentPage = pageHandler(currentPage, numPages);
                        if (currentPage == -1){ return; }
                    }
                }
        }
    }

    public void showFromInterface(String query, List<?> objectList){
        int maxElements = PrinterMenu.getPrintMultipleObjectsMax();
        int currentPage = 0;
        int currentIndex = 0;
        int numPages;
        if (objectList.size() == 0){
            PrinterMenu.printQueryCount(query, new ArrayList<>(), new ArrayList<>(), true, true);
            promptDecision("enter");
        } else {
            if (query.toLowerCase().contains("city") || query.toLowerCase().contains("country")){
                List<ArrayList<String>> listListString = new ArrayList<>();
                List<ArrayList<Long>> listListCount = new ArrayList<>();
                listListString.add(new ArrayList<>());
                listListCount.add(new ArrayList<>());
                try {
                    for (Object object : objectList) {
                        IOpportunityCountryOrCityCount countryOrCityCount = (IOpportunityCountryOrCityCount) object;
                        if (currentIndex++ < maxElements) {
                            listListString.get(currentPage).add(countryOrCityCount.getCountryOrCityComment());
                            listListCount.get(currentPage).add(countryOrCityCount.getCountryOrCityCount());
                        } else {
                            listListString.add(new ArrayList<>());
                            listListCount.add(new ArrayList<>());
                            listListString.get(++currentPage).add(countryOrCityCount.getCountryOrCityComment());
                            listListCount.get(++currentPage).add(countryOrCityCount.getCountryOrCityCount());
                        }
                    }
                } catch (ClassCastException e){
                    PrinterMenu.setWarning("Could not cast Object as IOpportunityCountryOrCityCount");
                }
                // Allow user to change between the pages
                numPages = listListString.size();
                while (true) {
                    PrinterMenu.printQueryCount(query, listListString.get(currentPage), listListCount.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                    currentPage = pageHandler(currentPage, numPages);
                    if (currentPage == -1){ return; }
                }
            } else if (query.toLowerCase().contains("industry")){
                List<ArrayList<String>> listListString = new ArrayList<>();
                List<ArrayList<Long>> listListCount = new ArrayList<>();
                listListString.add(new ArrayList<>());
                listListCount.add(new ArrayList<>());
                try {
                    for (Object object : objectList) {
                        IOpportunityIndustryCount industryCount = (IOpportunityIndustryCount) object;
                        if (currentIndex++ < maxElements) {
                            listListString.get(currentPage).add(industryCount.getIndustryComment().toString());
                            listListCount.get(currentPage).add(industryCount.getIndustryCount());
                        } else {
                            listListString.add(new ArrayList<>());
                            listListCount.add(new ArrayList<>());
                            listListString.get(++currentPage).add(industryCount.getIndustryComment().toString());
                            listListCount.get(++currentPage).add(industryCount.getIndustryCount());
                        }
                    }
                } catch (ClassCastException e) {
                    PrinterMenu.setWarning("Could not cast Object as IOpportunityCountryOrCityCount");
                }
                // Allow user to change between the pages
                numPages = listListString.size();
                while (true) {
                    PrinterMenu.printQueryCount(query, listListString.get(currentPage), listListCount.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                    currentPage = pageHandler(currentPage, numPages);
                    if (currentPage == -1) {
                        return;
                    }
                }
            } else if(query.toLowerCase().contains("lead")){
                List<ArrayList<String>> listListString = new ArrayList<>();
                List<ArrayList<Long>> listListCount = new ArrayList<>();
                listListString.add(new ArrayList<>());
                listListCount.add(new ArrayList<>());
                try {
                    for (Object object : objectList) {
                        ILeadsCountBySalesRep leadsCount = (ILeadsCountBySalesRep) object;
                        if (currentIndex++ < maxElements) {
                            listListString.get(currentPage).add(leadsCount.getSalesRepName());
                            listListCount.get(currentPage).add(leadsCount.getLeadsCount());
                        } else {
                            listListString.add(new ArrayList<>());
                            listListCount.add(new ArrayList<>());
                            listListString.get(++currentPage).add(leadsCount.getSalesRepName());
                            listListCount.get(++currentPage).add(leadsCount.getLeadsCount());
                        }
                    }
                } catch (ClassCastException e) {
                    PrinterMenu.setWarning("Could not cast Object as ILeadsCountBySalesRep");
                }
                // Allow user to change between the pages
                numPages = listListString.size();
                while (true) {
                    PrinterMenu.printQueryCount(query, listListString.get(currentPage), listListCount.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                    currentPage = pageHandler(currentPage, numPages);
                    if (currentPage == -1) {
                        return;
                    }
                }
            } else if(query.toLowerCase().contains("opportunities by salesrep")){
                List<ArrayList<String>> listListString = new ArrayList<>();
                List<ArrayList<Long>> listListCount = new ArrayList<>();
                listListString.add(new ArrayList<>());
                listListCount.add(new ArrayList<>());
                try {
                    for (Object object : objectList) {
                        IOpportunityCountBySalesRep opportunityCountBySalesRep = (IOpportunityCountBySalesRep) object;
                        if (currentIndex++ < maxElements) {
                            listListString.get(currentPage).add(opportunityCountBySalesRep.getSalesRepName());
                            listListCount.get(currentPage).add(opportunityCountBySalesRep.getOpportunitiesCount());
                        } else {
                            listListString.add(new ArrayList<>());
                            listListCount.add(new ArrayList<>());
                            listListString.get(++currentPage).add(opportunityCountBySalesRep.getSalesRepName());
                            listListCount.get(++currentPage).add(opportunityCountBySalesRep.getOpportunitiesCount());
                        }
                    }
                } catch (ClassCastException e) {
                    PrinterMenu.setWarning("Could not cast Object as IOpportunityCountBySalesRep");
                }
                // Allow user to change between the pages
                numPages = listListString.size();
                while (true) {
                    PrinterMenu.printQueryCount(query, listListString.get(currentPage), listListCount.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                    currentPage = pageHandler(currentPage, numPages);
                    if (currentPage == -1) {
                        return;
                    }
                }
            } else if(query.toLowerCase().contains("product")){
                List<ArrayList<String>> listListString = new ArrayList<>();
                List<ArrayList<Long>> listListCount = new ArrayList<>();
                listListString.add(new ArrayList<>());
                listListCount.add(new ArrayList<>());
                try {
                    for (Object object : objectList) {
                        IOpportunityProduct opportunityProduct = (IOpportunityProduct) object;
                        if (currentIndex++ < maxElements) {
                            listListString.get(currentPage).add(opportunityProduct.getProductComment().toString());
                            listListCount.get(currentPage).add(opportunityProduct.getProductCount());
                        } else {
                            listListString.add(new ArrayList<>());
                            listListCount.add(new ArrayList<>());
                            listListString.get(++currentPage).add(opportunityProduct.getProductComment().toString());
                            listListCount.get(++currentPage).add(opportunityProduct.getProductCount());
                        }
                    }
                } catch (ClassCastException e) {
                    PrinterMenu.setWarning("Could not cast Object as IOpportunityProduct");
                }
                // Allow user to change between the pages
                numPages = listListString.size();
                while (true) {
                    PrinterMenu.printQueryCount(query, listListString.get(currentPage), listListCount.get(currentPage), currentPage == 0, currentPage + 1 == numPages);
                    currentPage = pageHandler(currentPage, numPages);
                    if (currentPage == -1) {
                        return;
                    }
                }
            }
        }
    }


    private int pageHandler(int currentPage, int numPages){
        int decision;
        if (numPages > 1) {
            if (currentPage == 0) {
                decision = promptMultipleDecisions("next", "back");
                switch (decision) {
                    case 0:
                        return ++currentPage;
                    case 1:
                        return -1;
                    default:
                        throw new IllegalArgumentException("Prompt decision non existent");
                }
            } else if (currentPage + 1 == numPages) {
                decision = promptMultipleDecisions("previous", "back");
                switch (decision) {
                    case 0:
                        return --currentPage;
                    case 1:
                        return -1;
                    default:
                        throw new IllegalArgumentException("Prompt decision non existent");
                }
            } else {
                decision = promptMultipleDecisions("next", "previous", "back");
                switch (decision) {
                    case 0:
                        return ++currentPage;
                    case 1:
                        return --currentPage;
                    case 2:
                        return -1;
                    default:
                        throw new IllegalArgumentException("Prompt decision non existent");
                }
            }
        } else {
            promptDecision("enter");
            return -1;
        }
    }

    private SalesRep promptSalesRep(){
        PrinterMenu.printMenu("salesrep");
        String name = promptString("name");
        PrinterMenu.printMenu("salesrep", name);
        if (promptDecision("enter back")){
            return db.addSalesRep(name);
        }
        return null;
    }

    //Method that handles the prompts to convert a lead
    private void promptConvert(int id) {
        // check if Lead exists, if not print error message
        if (db.hasLead(id)) {
            String contactName = db.getLeadById(id).getName();
            String salesRepName = db.getLeadById(id).getSalesRep().getName();
            //call methods to prompt Opportunity's product and quantity
            PrinterMenu.printMenu("convert");
            Product product = promptProduct();
            PrinterMenu.printMenu("convert", "product", product.toString());
            int quantity = promptPositiveNumber();
            //print also the contact (from the lead's info)

            PrinterMenu.printMenu("convert", "quantity and contact", Integer.toString(quantity), contactName, salesRepName);
            if (!promptDecision("enter back")){

                return;
            }
            PrinterMenu.printMenu("convert", "account_select", Integer.valueOf(db.getAllAccounts().size()).toString());
            int decision;
            if (db.getAllAccounts().size() == 0) {
                if (promptDecision("enter back")) {
                    decision = 0;
                } else {
                    return;
                }
            } else {
                decision = promptMultipleDecisions("y", "n", "back");
            }
            switch (decision) {
                case 0:
                    //call methods to prompt Account's industry, employee count, city and country
                    PrinterMenu.printMenu("convert", "account");
                    Industry industry = promptIndustry();
                    PrinterMenu.printMenu("convert", "industry", industry.toString());
                    int employeeCount = promptPositiveNumber();
                    PrinterMenu.printMenu("convert", "employees", Integer.toString(employeeCount));
                    String city = promptString("location");
                    PrinterMenu.printMenu("convert", "city", city);
                    String country = promptString("location");
                    PrinterMenu.printMenu("convert", "country", country);
                    if (promptDecision("enter back")) {
                        db.convertLead(id, product, quantity, industry, employeeCount, city, country);
                    }
                    break;
                case 1:
                    PrinterMenu.printMenu("convert", "account_id");
                    Integer accountId = promptId("account");
                    Account account = db.getAccountById(accountId);
                    if (account != null) {
                        PrinterMenu.printMenu("convert", "account_id", accountId.toString(),
                                account.getIndustry().toString(), Integer.valueOf(account.getEmployeeCount()).toString()
                                , account.getCity(), account.getCountry());
                        if (promptDecision("enter back")) {
                            db.convertLead(id, product, quantity, accountId);
                        }
                    } else {
                        PrinterMenu.setWarning("Error: Account could not be fetched!");
                    }
                    break;
            }

        } else {
            PrinterMenu.setWarning("There is no lead with id " + id + " to convert!");
        }
    }

    // Method that handles the prompts to create a new lead
    private void promptLead() {
        // call methods to prompt name, phone number, email and company name
        PrinterMenu.printMenu("lead");
        String name = promptString("name");
        PrinterMenu.printMenu("lead", "name", name);
        String phoneNumber = promptString("phone");
        phoneNumber = phoneNumber.replaceAll(" ", "");
        PrinterMenu.printMenu("lead", "phone", phoneNumber);
        String email = promptString("email");
        PrinterMenu.printMenu("lead", "email", email);
        String companyName = promptString("");
        PrinterMenu.printMenu("lead", "company", companyName, Integer.valueOf(db.getAllSalesRep().size()).toString());
        int decision;
        if (db.getAllSalesRep().size() == 0){
            if (promptDecision("enter back")){
                decision = 0;
            }else {
                return;
            }
        }else {
            decision = promptMultipleDecisions("y", "n", "back");
            if (decision == 2){
                return;
            }
        }
        PrinterMenu.printMenu("lead","salesrep", decision == 0 ? "new" : "old");
        SalesRep salesRep;
        switch (decision) {
            case 0:
                String salesRepName = promptString("name");
                PrinterMenu.printMenu("lead", "salesrep_name", salesRepName);
                if (promptDecision("enter back")){
                    salesRep = db.addSalesRep(salesRepName);
                    db.addLead(name, phoneNumber, email, companyName, salesRep);
                    PrinterMenu.printMenu("lead", "salesrep_id", salesRep.getId().toString());
                } else {
                    return;
                }
                promptDecision("enter");
                break;
            case 1:
                Integer salesRepId = promptId("salesrep");
                salesRep = db.getSalesRepById(salesRepId);
                if (salesRep != null){
                    PrinterMenu.printMenu("lead","salesrep", salesRepId.toString(),
                            salesRep.getName());
                    if (promptDecision("enter back")) {
                        db.addLead(name, phoneNumber, email, companyName, salesRep);
                    }
                }else{
                    PrinterMenu.setWarning("Error: Account could not be fetched!");
                }
                break;
        }
    }

    // Method to ask for the user decision - one or two outcomes
    private boolean promptDecision(String decision) {
        String input;
        switch (decision) {
            case "enter back":
                do {
                    input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "":
                            return true;
                        case "back":
                            return false;
                    }
                    PrinterMenu.setWarning("Please input a valid command from the highlighted above!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                } while (true);
            case "enter":
                scanner.nextLine();
                return true;
            case "exit":
                do {
                    input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "":
                            return true;
                        case "exit":
                            return false;
                    }
                    PrinterMenu.setWarning("Please input a valid command from the highlighted above!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                } while (true);
        }
        return false;
    }

    // Method to ask for the user decision - more than 2 outcomes
    private int promptMultipleDecisions(String... choices) {
        if (choices.length == 0) {
            throw new IllegalArgumentException();
        }
        String input;
        while (true) {
            input = scanner.nextLine().trim().toLowerCase();
            for (int i = 0; i < choices.length; i++) {
                if (input.equals(choices[i].trim().toLowerCase())) {
                    return i;
                }
            }
            PrinterMenu.setWarning("Please input a valid command from the highlighted above!");
            PrinterMenu.printMenu("");
            PrinterMenu.clearWarning();
        }
    }

    // prompt Product and validate
    private Product promptProduct() {
        String input;
        input = scanner.nextLine().trim().toUpperCase();
        while (!validProduct(input)) {
            PrinterMenu.setWarning("Please input a valid Product option!");
            PrinterMenu.printMenu("");
            PrinterMenu.clearWarning();
            input = scanner.nextLine().trim().toUpperCase();
        }
        return Product.valueOf(input);
    }

    //prompt Industry and validate
    private Industry promptIndustry() {
        String input;
        input = scanner.nextLine().trim().toUpperCase();
        while (!validIndustry(input)) {
            PrinterMenu.setWarning("Please input a valid Industry option!");
            PrinterMenu.printMenu("");
            PrinterMenu.clearWarning();
            input = scanner.nextLine().trim().toUpperCase();
        }
        return Industry.valueOf(input);
    }

    //prompt number and validate
    private int promptPositiveNumber() {
        String input = scanner.nextLine().trim();
        while (!isValidPositiveNumber(input)) {
            PrinterMenu.setWarning("Please input a valid integer number! Must be positive!");
            PrinterMenu.printMenu("");
            PrinterMenu.clearWarning();
            input = scanner.nextLine().trim();
        }
        return Integer.parseInt(input);
    }

    //prompt phone number / email / name / location and validate
    private String promptString(String checkCondition) {
        String input;
        switch (checkCondition) {
            case "phone":
                input = scanner.nextLine().trim();
                while (!validPhone(input)) {
                    PrinterMenu.setWarning("Please input a valid Phone Number!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    input = scanner.nextLine().trim();
                }
                return input;
            case "email":
                input = scanner.nextLine().trim();
                while (!validEmail(input)) {
                    PrinterMenu.setWarning("Please input a valid Email!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    input = scanner.nextLine().trim();
                }
                return input;
            case "name":
                input = scanner.nextLine().trim();
                while (!validName(input)) {
                    PrinterMenu.setWarning("Please input a valid Name!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    input = scanner.nextLine().trim();
                }
                return input;
            case "location":
                input = scanner.nextLine().trim();
                while (!validLocation(input)) {
                    PrinterMenu.setWarning("Please input a valid location (case sensitive)!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    input = scanner.nextLine().trim();
                }
                return input;
            default:
                input = scanner.nextLine().trim();
                while (input.isEmpty()) {
                    PrinterMenu.setWarning("Please input a non empty string!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    input = scanner.nextLine().trim();
                }
                return input;
        }
    }

    //prompt id
    private Integer promptId(String condition) {
        int id;
        switch (condition) {
            case "account":
                id = promptPositiveNumber();
                while (!db.hasAccount(id)) {
                    PrinterMenu.setWarning("Please input the id of an existing Account!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    id = promptPositiveNumber();
                }
                return id;
            case "opportunity":
                id = promptPositiveNumber();
                while (!db.hasOpportunity(id)) {
                    PrinterMenu.setWarning("Please input the id of an existing Opportunity!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    id = promptPositiveNumber();
                }
                return id;
            case "contact":
                id = promptPositiveNumber();
                while (!db.hasContact(id)) {
                    PrinterMenu.setWarning("Please input the id of an existing Contact!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    id = promptPositiveNumber();
                }
                return id;
            case "lead":
                id = promptPositiveNumber();
                while (!db.hasLead(id)) {
                    PrinterMenu.setWarning("Please input the id of an existing Lead!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    id = promptPositiveNumber();
                }
                return id;
            case "salesrep":
                id = promptPositiveNumber();
                while (!db.hasSalesRep(id)) {
                    PrinterMenu.setWarning("Please input the id of an existing SalesRep!");
                    PrinterMenu.printMenu("");
                    PrinterMenu.clearWarning();
                    id = promptPositiveNumber();
                }
                return id;
            default:
                throw new IllegalArgumentException("The condition " + condition + " is not implemented!");
        }
    }
}
