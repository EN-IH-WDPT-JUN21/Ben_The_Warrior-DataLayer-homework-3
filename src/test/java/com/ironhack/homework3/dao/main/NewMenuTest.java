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
import org.springframework.test.context.TestPropertySource;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {      // For testing it uses a "datalayer_tests" database and the same user
        "spring.datasource.url=jdbc:mysql://localhost:3306/datalayer_test",
        "spring.datasource.username=Ben",
        "spring.datasource.password=Password-123",
        "spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver",
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.jpa.show-sql=true",
        "spring.datasource.initialization-mode=never"
})
class NewMenuTest {

    private ByteArrayInputStream input;

    @Autowired
    private Menu menu;

    @Autowired
    SalesRepRepository salesRepRepository;

    @Autowired
    LeadRepository leadRepository;

    @Autowired
    ContactRepository contactRepository;

    @Autowired
    OpportunityRepository opportunityRepository;

    @Autowired
    AccountRepository accountRepository;

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
    @DisplayName("Display Account's List of Contacts and Opportunities")
    void mainNewMenu_LookupAccount_ValidLookup() {

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream(("lookup account 1\ncontacts\n \nopportunities\n \nback\nexit").getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("Michael Scott"));
        assertTrue(output.contains("Bob Vance") && output.contains("123123123") &&
                output.contains("bob@vance_refrigeration.com") && output.contains("Vance Refrigeration"));
        assertTrue(output.contains("BOX") && output.contains("13") && output.contains("OPEN"));
        assertTrue(output.contains("PRODUCE") && output.contains("5") &&
                output.contains("Scranton") && output.contains("United States"));
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
        assertTrue(output.contains("Michael Scott: 0"));
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
        assertTrue(output.contains("Michael Scott: 0"));
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
        assertTrue(output.contains("BOX: 0"));
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
        assertTrue(output.contains("Count of CLOSED_LOST Opportunities by Product"));
        assertTrue(output.contains("BOX: 0"));
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
        SalesRep salesRep1 = new SalesRep("SalesRep 1");
        SalesRep salesRep2 = new SalesRep("SalesRep 2");
        SalesRep salesRep3 = new SalesRep("SalesRep 3");
        SalesRep salesRep4 = new SalesRep("SalesRep 4");
        SalesRep salesRep5 = new SalesRep("SalesRep 5");
        SalesRep salesRep6 = new SalesRep("SalesRep 6");
        SalesRep salesRep7 = new SalesRep("SalesRep 7");
        SalesRep salesRep8 = new SalesRep("SalesRep 8");
        SalesRep salesRep9 = new SalesRep("SalesRep 9");
        SalesRep salesRep10 = new SalesRep("SalesRep 10");
        SalesRep salesRep11 = new SalesRep("SalesRep 11");
        SalesRep salesRep12 = new SalesRep("SalesRep 12");
        SalesRep salesRep13 = new SalesRep("SalesRep 13");

        Lead lead1 = new Lead("Lead 1", "111111111", "lead@abc.com", "Leads", salesRep1);
        Lead lead2 = new Lead("Lead 2", "111111111", "lead@abc.com", "Leads", salesRep2);
        Lead lead3 = new Lead("Lead 3", "111111111", "lead@abc.com", "Leads", salesRep3);
        Lead lead4 = new Lead("Lead 4", "111111111", "lead@abc.com", "Leads", salesRep4);
        Lead lead5 = new Lead("Lead 5", "111111111", "lead@abc.com", "Leads", salesRep5);
        Lead lead6 = new Lead("Lead 6", "111111111", "lead@abc.com", "Leads", salesRep6);
        Lead lead7 = new Lead("Lead 7", "111111111", "lead@abc.com", "Leads", salesRep7);
        Lead lead8 = new Lead("Lead 8", "111111111", "lead@abc.com", "Leads", salesRep8);
        Lead lead9 = new Lead("Lead 9", "111111111", "lead@abc.com", "Leads", salesRep9);
        Lead lead10 = new Lead("Lead 10", "111111111", "lead@abc.com", "Leads", salesRep10);
        Lead lead11 = new Lead("Lead 11", "111111111", "lead@abc.com", "Leads", salesRep11);
        Lead lead12 = new Lead("Lead 12", "111111111", "lead@abc.com", "Leads", salesRep12);
        Lead lead13 = new Lead("Lead 13", "111111111", "lead@abc.com", "Leads", salesRep13);

        Account account1 = new Account(Industry.PRODUCE, 5, "City 1", "Country 1");
        Account account2 = new Account(Industry.PRODUCE, 5, "City 2", "Country 2");
        Account account3 = new Account(Industry.PRODUCE, 5, "City 3", "Country 3");
        Account account4 = new Account(Industry.PRODUCE, 5, "City 4", "Country 4");
        Account account5 = new Account(Industry.PRODUCE, 5, "City 5", "Country 5");
        Account account6 = new Account(Industry.PRODUCE, 5, "City 6", "Country 6");
        Account account7 = new Account(Industry.PRODUCE, 5, "City 7", "Country 7");
        Account account8 = new Account(Industry.PRODUCE, 5, "City 8", "Country 8");
        Account account9 = new Account(Industry.PRODUCE, 5, "City 9", "Country 8");
        Account account10 = new Account(Industry.PRODUCE, 5, "City 10", "Country 10");
        Account account11 = new Account(Industry.PRODUCE, 5, "City 11", "Country 11");
        Account account12 = new Account(Industry.PRODUCE, 5, "City 12", "Country 12");
        Account account13 = new Account(Industry.PRODUCE, 5, "City 13", "Country 13");

        Contact contact1 = new Contact("Contact 1", "222222222", "contacts@abc.com", "Contacts", account1);
        Contact contact2 = new Contact("Contact 2", "222222222", "contacts@abc.com", "Contacts", account2);
        Contact contact3 = new Contact("Contact 3", "222222222", "contacts@abc.com", "Contacts", account3);
        Contact contact4 = new Contact("Contact 4", "222222222", "contacts@abc.com", "Contacts", account4);
        Contact contact5 = new Contact("Contact 5", "222222222", "contacts@abc.com", "Contacts", account5);
        Contact contact6 = new Contact("Contact 6", "222222222", "contacts@abc.com", "Contacts", account6);
        Contact contact7 = new Contact("Contact 7", "222222222", "contacts@abc.com", "Contacts", account7);
        Contact contact8 = new Contact("Contact 8", "222222222", "contacts@abc.com", "Contacts", account8);
        Contact contact9 = new Contact("Contact 9", "222222222", "contacts@abc.com", "Contacts", account9);
        Contact contact10 = new Contact("Contact 10", "222222222", "contacts@abc.com", "Contacts", account10);
        Contact contact11 = new Contact("Contact 11", "222222222", "contacts@abc.com", "Contacts", account11);
        Contact contact12 = new Contact("Contact 12", "222222222", "contacts@abc.com", "Contacts", account12);
        Contact contact13 = new Contact("Contact 13", "222222222", "contacts@abc.com", "Contacts", account13);

        Opportunity opportunity1 = new Opportunity(Product.BOX, 13, contact1, Status.OPEN, account1, salesRep1);
        Opportunity opportunity2 = new Opportunity(Product.BOX, 13, contact2, Status.OPEN, account2, salesRep1);
        Opportunity opportunity3 = new Opportunity(Product.BOX, 13, contact3, Status.OPEN, account3, salesRep1);
        Opportunity opportunity4 = new Opportunity(Product.BOX, 13, contact4, Status.OPEN, account4, salesRep1);
        Opportunity opportunity5 = new Opportunity(Product.BOX, 13, contact5, Status.OPEN, account5, salesRep1);
        Opportunity opportunity6 = new Opportunity(Product.BOX, 13, contact6, Status.OPEN, account6, salesRep1);
        Opportunity opportunity7 = new Opportunity(Product.BOX, 13, contact7, Status.OPEN, account7, salesRep1);
        Opportunity opportunity8 = new Opportunity(Product.BOX, 13, contact8, Status.OPEN, account8, salesRep1);
        Opportunity opportunity9 = new Opportunity(Product.BOX, 13, contact9, Status.OPEN, account9, salesRep1);
        Opportunity opportunity10 = new Opportunity(Product.BOX, 13, contact10, Status.OPEN, account10, salesRep1);
        Opportunity opportunity11 = new Opportunity(Product.BOX, 13, contact11, Status.OPEN, account11, salesRep1);
        Opportunity opportunity12 = new Opportunity(Product.BOX, 13, contact12, Status.OPEN, account12, salesRep1);
        Opportunity opportunity13 = new Opportunity(Product.BOX, 13, contact13, Status.OPEN, account13, salesRep1);

        salesRepRepository.saveAll(Arrays.asList(salesRep1, salesRep2, salesRep3, salesRep4, salesRep5, salesRep6, salesRep7,
                salesRep8, salesRep9, salesRep10, salesRep11, salesRep12, salesRep13));
        leadRepository.saveAll(Arrays.asList(lead1, lead2, lead3, lead4, lead5, lead6, lead7,  lead8, lead9, lead10, lead11,
                lead12, lead13));
        accountRepository.saveAll(Arrays.asList(account1, account2, account3, account4, account5, account6, account7,  account8,
                account9, account10, account11, account12, account13));
        contactRepository.saveAll(Arrays.asList(contact1, contact2, contact3, contact4, contact5, contact6, contact7,  contact8,
                contact9, contact10, contact11, contact12, contact13));
        opportunityRepository.saveAll(Arrays.asList(opportunity1, opportunity2, opportunity3, opportunity4, opportunity5, opportunity6,
                opportunity7,  opportunity8, opportunity9, opportunity10, opportunity11, opportunity12, opportunity13));

        menu.getDb().setSalesRepRepository(salesRepRepository);
        menu.getDb().setLeadRepository(leadRepository);
        menu.getDb().setAccountRepository(accountRepository);
        menu.getDb().setContactRepository(contactRepository);
        menu.getDb().setOpportunityRepository(opportunityRepository);

        PrintStream sysOutBackup = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        input = new ByteArrayInputStream(("show salesreps\nnext\nnext\nprevious\nback\nshow leads\nnext\nnext\nprevious" +
                "\nback\nshow contacts\nnext\nnext\nprevious\nback\nshow opportunities\nnext\nnext\nprevious\nback" +
                "\nshow accounts\nnext\nnext\nprevious\nback\nexit").getBytes());

        menu.setScanner(new Scanner(input));
        menu.mainMenu();

        String output = outputStream.toString();
        assertTrue(output.contains("SalesRep 1") && output.contains("SalesRep 2") && output.contains("SalesRep 3") && output.contains("SalesRep 4")
                && output.contains("SalesRep 5") && output.contains("SalesRep 6") && output.contains("SalesRep 7") && output.contains("SalesRep 8")
                && output.contains("SalesRep 9") && output.contains("SalesRep 10") && output.contains("SalesRep 11") && output.contains("SalesRep 12")
                && output.contains("SalesRep 13"));
        assertTrue(output.contains("Lead 1") && output.contains("Lead 2") && output.contains("Lead 3") && output.contains("Lead 4")
                && output.contains("Lead 5") && output.contains("Lead 6") && output.contains("Lead 7") && output.contains("Lead 8")
                && output.contains("Lead 9") && output.contains("Lead 10") && output.contains("Lead 11") && output.contains("Lead 12")
                && output.contains("Lead 13"));
        assertTrue(output.contains("City 1") && output.contains("City 2") && output.contains("City 3") && output.contains("City 4")
                && output.contains("City 5") && output.contains("City 6") && output.contains("City 7") && output.contains("City 8")
                && output.contains("City 9") && output.contains("City 10") && output.contains("City 11") && output.contains("City 12")
                && output.contains("City 13"));
        assertTrue(output.contains("Contact 1") && output.contains("Contact 2") && output.contains("Contact 3") && output.contains("Contact 4")
                && output.contains("Contact 5") && output.contains("Contact 6") && output.contains("Contact 7") && output.contains("Contact 8")
                && output.contains("Contact 9") && output.contains("Contact 10") && output.contains("Contact 11") && output.contains("Contact 12")
                && output.contains("Contact 13"));
        System.setOut(sysOutBackup);
    }

    @Test
    @DisplayName("Show objects with multiple pages")
    void mainNewMenu_ShowObjectsMultiplePages_ShowExistingObjects() {

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

    @Test
    @DisplayName("Prompt id for existing objects")
    void promptId_Validate() {

        input = new ByteArrayInputStream("5\n6\n-5\n1\n5\n6\n-5\n1\n5\n6\n-5\n1\n5\n6\n-5\n1\n5\n6\n-5\n1\n".getBytes());
        menu.setScanner(new Scanner(input));
        int salesrep = menu.promptId("salesrep");
        int lead = menu.promptId("lead");
        int contact = menu.promptId("contact");
        int opportunity = menu.promptId("opportunity");
        int account = menu.promptId("account");

        assertEquals(1, salesrep);
        assertEquals(1, lead);
        assertEquals(1, contact);
        assertEquals(1, opportunity);
        assertEquals(1, account);
    }
}