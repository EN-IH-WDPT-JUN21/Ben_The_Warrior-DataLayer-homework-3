package com.ironhack.homework3.dao.main;

import com.ironhack.homework3.dao.classes.*;
import com.ironhack.homework3.enums.Industry;
import com.ironhack.homework3.enums.Product;
import com.ironhack.homework3.enums.Status;
import com.ironhack.homework3.repository.*;
import com.ironhack.homework3.utils.DatabaseUtility;
import com.ironhack.homework3.utils.PrinterMenu;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class NewMenuTest {

    private MainMenuAutowired mainMenuAutowired;

    private DatabaseUtility initialDatabase;
    private ByteArrayInputStream input;

    @Autowired
    private Menu menu;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    private Menu menu;

    @BeforeEach
    void setUp() {
        SalesRep salesRep = new SalesRep("Michael Scott");
        Lead lead = new Lead("Leo", "111111111", "leo@vance_refrigeration.com", "Vance Refrigeration", salesRep);
        Account account = new Account(Industry.PRODUCE, 5, "Scranton", "United States");
        Contact contact = new Contact("Bob Vance", "123123123", "bob@vance_refrigeration.com", "Vance Refrigeration", account);
        Opportunity opportunity = new Opportunity(Product.BOX, 13, contact, Status.OPEN, account, salesRep);

        salesRepRepository.save(salesRep);
        leadRepository.save(lead);
        accountRepository.save(account);
        contactRepository.save(contact);
        opportunityRepository.save(opportunity);
        menu.getDb().setSalesRepRepository(salesRepRepository);
        menu.getDb().setLeadRepository(leadRepository);
        menu.getDb().setAccountRepository(accountRepository);
        menu.getDb().setContactRepository(contactRepository);
        menu.getDb().setOpportunityRepository(opportunityRepository);
    }


    @AfterEach
    void tearDown() {
        opportunityRepository.deleteAll();
        contactRepository.deleteAll();
        leadRepository.deleteAll();
        salesRepRepository.deleteAll();
        accountRepository.deleteAll();
    }

    @Test
    @DisplayName("Print help menu when command help")
    void mainNewMenu_HelpCommand_PrintHelpNewMenu() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("help\nexit\nexit".getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("new lead"));
        assertTrue(output.contains("convert <ID>"));
        assertTrue(output.contains("close-won <ID>"));
        assertTrue(output.contains("close-lost <ID>"));
        assertTrue(output.contains("lookup <OBJECT> <ID>"));
        assertTrue(output.contains("show <OBJECT PLURAL>"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Print help menu when command help")
    void mainNewMenu_HelpAllCommand_PrintHelpAllNewMenu() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("help -a\nexit\nexit".getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("new lead"));
        assertTrue(output.contains("new salesrep"));
        assertTrue(output.contains("convert <ID>"));
        assertTrue(output.contains("close-won <ID>"));
        assertTrue(output.contains("close-lost <ID>"));
        assertTrue(output.contains("lookup <OBJECT> <ID>"));
        assertTrue(output.contains("show <OBJECT PLURAL>"));
        assertTrue(output.contains("report lead by salesrep"));
        assertTrue(output.contains("report opportunity by <PROPERTY>"));
        assertTrue(output.contains("report closed-won by <PROPERTY>"));
        assertTrue(output.contains("report closed-lost by <PROPERTY>"));
        assertTrue(output.contains("report open by <PROPERTY>"));
        assertTrue(output.contains("mean <PROPERTY>"));
        assertTrue(output.contains("median <PROPERTY>"));
        assertTrue(output.contains("max <PROPERTY>"));
        assertTrue(output.contains("min <PROPERTY>"));
        assertTrue(output.contains("help (-a)"));
        assertTrue(output.contains("exit"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Add prompted SalesRep to the database")
    void mainNewMenu_NewSalesRepCommand_AddNewSalesRep(){
            input = new ByteArrayInputStream("new salesrep\nJim\n \n \nexit\n ".getBytes());

            long initialSize = menu.getDb().getAllSalesRep().size();

            menu.setScanner(new Scanner(input));

            menu.mainMenu();

            long finalSize = menu.getDb().getAllSalesRep().size();

            assertEquals(initialSize + 1, finalSize);
    }

    @Test
    @DisplayName("Add prompted lead to the database")
    void mainNewMenu_NewLeadCommand_AddNewLead(){

        input = new ByteArrayInputStream("new lead\nJohn\n987654321\njohn@ironhack.com\nIronhack\ny\nPeter\n \n \nexit\n ".getBytes());

        long initialSize = menu.getDb().getAllLeads().size();

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        long finalSize = menu.getDb().getAllLeads().size();

        assertEquals(initialSize + 1, finalSize);
    }

    @Test
    @DisplayName("Add Contact, Opportunity and Account when converting Lead")
    void mainNewMenu_ConvertCommand_NewLOpportunityAccountContact() {

        input = new ByteArrayInputStream("convert 1\nhybrid\n200\n \ny\nOtHer\n1\nLisbon\nPortugal\n \nexit\n ".getBytes());

        int initialLeadSize = menu.getDb().getAllLeads().size();
        int initialContactSize = menu.getDb().getAllContacts().size();
        int initialOpportunitySize = menu.getDb().getAllOpportunities().size();
        int initialAccountSize = menu.getDb().getAllAccounts().size();

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        int finalLeadSize = menu.getDb().getAllLeads().size();
        int finalContactSize = menu.getDb().getAllContacts().size();
        int finalOpportunitySize = menu.getDb().getAllOpportunities().size();
        int finalAccountSize = menu.getDb().getAllAccounts().size();

        assertEquals(initialLeadSize - 1, finalLeadSize);
        assertEquals(initialContactSize + 1, finalContactSize);
        assertEquals(initialOpportunitySize + 1, finalOpportunitySize);
        assertEquals(initialAccountSize + 1, finalAccountSize);
    }

    @Test
    @DisplayName("Display error message of invalid command")
    void mainNewMenu_InvalidCommand_ErrorMessage() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("invalid command\nexit\nexit".getBytes());
        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("There is no such command as \"invalid command\"! To see the list of available commands type help!"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Change status to close-won")
    void mainNewMenu_CloseWon_ChangeStatusToCloseWon() {

        input = new ByteArrayInputStream("close-won 1\nexit\n ".getBytes());
        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        assertEquals(Status.CLOSED_WON, menu.getDb().getOpportunityById(1).getStatus());
    }

    @Test
    @DisplayName("Change status to close-lost")
    void mainNewMenu_CloseLost_ChangeStatusToCloseLost() {

        input = new ByteArrayInputStream("close-lost 1\nexit\n ".getBytes());
        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        assertEquals(Status.CLOSED_LOST, menu.getDb().getOpportunityById(1).getStatus());
    }

    @Test
    @DisplayName("Display error message of nonexistent object to lookup")
    void mainNewMenu_LookupNonexistentObject_ErrorMessage() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("lookup salesrep 5\nlookup lead 5\nlookup contact 5\nlookup opportunity 5\nlookup account 5\nexit\nexit".getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("There is no SalesRep with id 5"));
        assertTrue(output.contains("There is no Lead with id 5"));
        assertTrue(output.contains("There is no Contact with id 5"));
        assertTrue(output.contains("There is no Opportunity with id 5"));
        assertTrue(output.contains("There is no Account with id 5"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Display object when looking up")
    void mainNewMenu_LookupExistentObject_ValidLookup() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream(("lookup salesrep 1\n \nlookup lead 1\n \nlookup contact 1\n \nlookup opportunity 1\nback\n" +
                "lookup account 1\nback\nexit").getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Michael Scott"));
        assertTrue(output.contains("Leo") && output.contains("111111111") &&
                output.contains("leo@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("Bob Vance") && output.contains("123123123") &&
                output.contains("bob@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("BOX") && output.contains("13") && output.contains("OPEN"));
        assertTrue(output.contains("PRODUCE") && output.contains("5") &&
                output.contains("Scranton") && output.contains("United States"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Display Opportunity's Contact Information")
    void mainNewMenu_LookupOpportunityAndExpandContact_ValidLookup() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream(("lookup opportunity 1\n \n \nexit").getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Bob Vance") && output.contains("123123123") &&
                output.contains("bob@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("BOX") && output.contains("13") && output.contains("OPEN"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Lead by SalesRep")
    void mainNewMenu_ReportLeadBySalesRep_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report lead by salesrep\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Leads by SalesRep"));
        assertTrue(output.contains("Michael Scott: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Opportunity by SalesRep")
    void mainNewMenu_ReportOpportunityBySalesRep_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report opportunity by salesrep\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Opportunities by SalesRep"));
        assertTrue(output.contains("Michael Scott: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_WON by SalesRep")
    void mainNewMenu_ReportClosedWonBySalesRep_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-won by salesrep\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_WON Opportunities by SalesRep"));
        assertTrue(output.contains("Michael Scott: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_LOST by SalesRep")
    void mainNewMenu_ReportClosedLostBySalesRep_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-lost by salesrep\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by SalesRep"));
        assertTrue(output.contains("Michael Scott: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report OPEN by SalesRep")
    void mainNewMenu_ReportOpenBySalesRep_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report open by salesrep\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of OPEN Opportunities by SalesRep"));
        assertTrue(output.contains("Michael Scott: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Opportunity by Product")
    void mainNewMenu_ReportOpportunityByProduct_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report opportunity by product\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Opportunities by Product"));
        assertTrue(output.contains("BOX: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_WON by Product")
    void mainNewMenu_ReportClosedWonByProduct_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-won by product\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_WON Opportunities by Product"));
        assertTrue(output.contains("BOX: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_LOST by Product")
    void mainNewMenu_ReportClosedLostByProduct_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-lost by product\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by SalesRep"));
        assertTrue(output.contains("BOX: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report OPEN by Product")
    void mainNewMenu_ReportOpenByProduct_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report open by product\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of OPEN Opportunities by Product"));
        assertTrue(output.contains("BOX: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Opportunity by City")
    void mainNewMenu_ReportOpportunityByCity_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report opportunity by city\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Opportunities by City"));
        assertTrue(output.contains("Scranton: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_WON by City")
    void mainNewMenu_ReportClosedWonByCity_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-won by city\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_WON Opportunities by City"));
        assertTrue(output.contains("Scranton: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_LOST by City")
    void mainNewMenu_ReportClosedLostByCity_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-lost by city\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by City"));
        assertTrue(output.contains("Scranton: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report OPEN by City")
    void mainNewMenu_ReportOpenByCity_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report open by city\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of OPEN Opportunities by City"));
        assertTrue(output.contains("Scranton: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Opportunity by Country")
    void mainNewMenu_ReportOpportunityByCountry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report opportunity by country\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Opportunities by Country"));
        assertTrue(output.contains("United States: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_WON by Country")
    void mainNewMenu_ReportClosedWonByCountry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-won by country\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_WON Opportunities by Country"));
        assertTrue(output.contains("United States: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_LOST by Country")
    void mainNewMenu_ReportClosedLostByCountry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-lost by country\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by Country"));
        assertTrue(output.contains("United States: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report OPEN by Country")
    void mainNewMenu_ReportOpenByCountry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report open by country\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of OPEN Opportunities by Country"));
        assertTrue(output.contains("United States: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report Opportunity by Industry")
    void mainNewMenu_ReportOpportunityByIndustry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report opportunity by industry\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of Opportunities by Industry"));
        assertTrue(output.contains("PRODUCE: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_WON by Industry")
    void mainNewMenu_ReportClosedWonByIndustry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-won by industry\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_WON Opportunities by Industry"));
        assertTrue(output.contains("PRODUCE: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report CLOSED_LOST by Industry")
    void mainNewMenu_ReportClosedLostByIndustry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report closed-lost by industry\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by Industry"));
        assertTrue(output.contains("PRODUCE: 0"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Report OPEN by Industry")
    void mainNewMenu_ReportOpenByIndustry_ValidReport(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("report open by industry\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Count of OPEN Opportunities by Industry"));
        assertTrue(output.contains("PRODUCE: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Mean Employee Count")
    void mainNewMenu_MeanEmployeeCount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("mean employeecount\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Mean Employee Count"));
        assertTrue(output.contains("Mean: 5"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Median Employee Count")
    void mainNewMenu_MedianEmployeeCount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("median employeecount\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Median Employee Count"));
        assertTrue(output.contains("Median: 5"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Min Employee Count")
    void mainNewMenu_MinEmployeeCount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("min employeecount\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Min Employee Count"));
        assertTrue(output.contains("Min: 5"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Max Employee Count")
    void mainNewMenu_MaxEmployeeCount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("max employeecount\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Max Employee Count"));
        assertTrue(output.contains("Max: 5"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Mean Quantity")
    void mainNewMenu_MeanQuantity_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("mean quantity\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Mean Quantity"));
        assertTrue(output.contains("Mean: 13"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Median Quantity")
    void mainNewMenu_MedianQuantity_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("median quantity\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Median Quantity"));
        assertTrue(output.contains("Median: 13"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Min Quantity")
    void mainNewMenu_MinQuantity_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("min quantity\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Min Quantity"));
        assertTrue(output.contains("Min: 13"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Max Quantity")
    void mainNewMenu_MaxQuantity_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("max quantity\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Max Quantity"));
        assertTrue(output.contains("Max: 13"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Mean Opps per Account")
    void mainNewMenu_MeanOppsPerAccount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("mean opps per account\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Mean Opportunities per Account"));
        assertTrue(output.contains("Mean: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Median Opps per Account")
    void mainNewMenu_MedianOppsPerAccount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("median opps per account\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Median Opportunities per Account"));
        assertTrue(output.contains("Median: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Min Opps per Account")
    void mainNewMenu_MinOppsPerAccount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("min opps per account\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Min Opportunities per Account"));
        assertTrue(output.contains("Min: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Max Opps per Account")
    void mainNewMenu_MaxOppsPerAccount_ValidStat(){

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("max opps per account\n \nexit\n ".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Max Opportunities per Account"));
        assertTrue(output.contains("Max: 1"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Show objects")
    void mainNewMenu_ShowObjects_ShowExistingObjects() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("show salesreps\n \nshow leads\n \nshow contacts\n \nshow opportunities\nback\nshow accounts\n \nexit".getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Available SalesReps") && output.contains("Michael Scott"));
        assertTrue(output.contains("Available Leads") && output.contains("Leo") && output.contains("111111111") &&
                output.contains("leo@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("Available Contacts") && output.contains("Bob Vance") && output.contains("123123123") &&
                output.contains("bob@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("Available Opportunities") && output.contains("BOX") && output.contains("13") && output.contains("OPEN"));
        assertTrue(output.contains("Available Accounts") && output.contains("PRODUCE") && output.contains("5") &&
                output.contains("Scranton") && output.contains("United States"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Compute help command")
    void computeCommand_HelpCommand_ShowHelpTrue() {
        assertFalse(menu.isShowHelp());
        menu.computeCommand("help");
        assertTrue(menu.isShowHelp());
    }

    @Test
    @DisplayName("Compute new lead command")
    void computeCommand_NewLeadCommand_AddNewLead() {

        int initialSize = menu.getDb().getAllLeads().size();

        input = new ByteArrayInputStream("John\n987654321\njohn@ironhack.com\nIronhack\nn\n1\n ".getBytes());
        menu.setScanner(new Scanner(input));
        menu.computeCommand("new lead");

        int finalSize = menu.getDb().getAllLeads().size();

        assertEquals(initialSize + 1, finalSize);
    }

    @Test
    @DisplayName("Compute convert command")
    void computeCommand_ConvertCommand_NewLOpportunityAccountContact() {

        int initialLeadSize = menu.getDb().getAllLeads().size();
        int initialContactSize = menu.getDb().getAllLeads().size();
        int initialOpportunitySize = menu.getDb().getAllLeads().size();
        int initialAccountSize = menu.getDb().getAllLeads().size();

        input = new ByteArrayInputStream("hybrid\n200\n \ny\nOtHer\n1\nLisbon\nPortugal\n ".getBytes());

        menu.setScanner(new Scanner(input));
        menu.computeCommand("convert 1");

        int finalLeadSize = menu.getDb().getAllLeads().size();
        int finalContactSize = menu.getDb().getAllContacts().size();
        int finalOpportunitySize = menu.getDb().getAllOpportunities().size();
        int finalAccountSize = menu.getDb().getAllAccounts().size();

        assertEquals(initialLeadSize - 1, finalLeadSize);
        assertEquals(initialContactSize + 1, finalContactSize);
        assertEquals(initialOpportunitySize + 1, finalOpportunitySize);
        assertEquals(initialAccountSize + 1, finalAccountSize);
    }

    @Test
    @DisplayName("Compute close-won command")
    void computeCommand_CloseWon_ChangeStatusToCloseWon() {

        menu.computeCommand("close-won 1");

        assertEquals(Status.CLOSED_WON, menu.getDb().getOpportunityById(1).getStatus());
    }

    @Test
    @DisplayName("Compute close-won command with invalid Id")
    void computeCommand_CloseWon_InvalidId() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("close-won 5\nexit".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();

        assertTrue(output.contains("There is no Opportunity with id 5"));
    }

    @Test
    @DisplayName("Compute close-lost command")
    void computeCommand_CloseLost_ChangeStatusToCloseLost() {

        menu.computeCommand("close-lost 1");

        assertEquals(Status.CLOSED_LOST, menu.getDb().getOpportunityById(1).getStatus());
    }

    @Test
    @DisplayName("Compute close-lost command with invalid Id")
    void computeCommand_CloseLost_InvalidId() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream("close-lost 5\nexit".getBytes());

        menu.setScanner(new Scanner(input));

        menu.mainMenu();

        String output = outputStream.toString();

        assertTrue(output.contains("There is no Opportunity with id 5"));
    }

    @Test
    @DisplayName("Compute lookup with nonexistent id")
    void computeCommand_LookupNonexistentObject_ErrorMessage() {

        menu.computeCommand("lookup lead 5");
        assertEquals(PrinterMenu.getWarning(), "There is no Lead with id 5");
        menu.computeCommand("lookup contact 5");
        assertEquals(PrinterMenu.getWarning(), "There is no Contact with id 5");
        menu.computeCommand("lookup opportunity 5");
        assertEquals(PrinterMenu.getWarning(), "There is no Opportunity with id 5");
        menu.computeCommand("lookup account 5");
        assertEquals(PrinterMenu.getWarning(), "There is no Account with id 5");
    }
}