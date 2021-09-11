# Ben_The_Warrior-CRM-homework-3

## Table of Contents

1. [**Introduction**](#Introduction)
    1. [Functions](#Functions)
2. [**Installation**](#Installation)
    1. [Setup](#Setup)
3. [**Menu**](#Menu)
    1. [How to navigate the menu](#How-to-navigate-the-menu)
4. [**Help menu**](#Help-menu)
5. [**Description of the CRM Components**](#Description-of-the-CRM-Components)
    1. [Lead](#Lead)
    2. [Contact](#Contact)
    3. [Opportunity](#Opportunity)
    4. [Account](#Account)
6. [**Walkthrough CRM**](#Walkthrough-CRM)
    1. [Create New Lead](#Create-New-Lead)
    2. [Convert a Lead into an Opportunity](#Convert-a-Lead-into-an-Opportunity)
    3. [Close Won Opportunity](#Close-Won-Opportunity)
    4. [Close Lost Opportunity](#Close-Lost-Opportunity)
    5. [Search for specific Lead, Opportunity, Account or Contact](#Search-for-specific-Lead,-Opportunity,-Account-or-Contact)
    6. [List all Leads, Opportunities, Accounts or Contacts](#List-all-Leads,-Opportunities,-Accounts-or-Contacts)
    7. [Reporting Functionality](#Reporting-Functionality)
    8. [Save the changed data](#Save-the-changed-data)
    9. [Save and exit the program](#Save-and-exit-the-program)
7. [**Use Case Diagram**](#Use-Case-Diagram)
8. [**Class Diagram**](#Class-Diagram)
9. [**The Team**](#The-Team)

## Introduction

Currently, companies seek to be able to deal with their customers on a one-to-one basis and thus be able to provide a
better service with a shorter response time. In order to achieve this, this CRM (Customer Relationship Manager) was born
as a collaborative assignment for the Ironhack bootcamp. This program aims to improve the relationship management with
customers of a company that sells trucks. The software allows you to keep track of all prospects, contacts,
opportunities and accounts; and update information about each database entry.

### Functions

* Intuitive and easy to use interface
* Lead management, tracking and status
* Creating contacts when a potential customer is interested in purchasing a product
* Opportunities management
* Accounts management
* Sales Representative management
* Save all changes made in a database

## Installation

Clone or download the project from this git repository.

**Setup** the recommended database.

There are two methods to run the application:

1. Run from an IDE such as IntelliJ **(recommended)**
    - Open the repository as a project;
    - Run the main application "Homework3Application.java", on the path:
      `./src/main/java/com/ironhack/homework3/Homework3Application.java`
    - Run the tests present in the tests' path:
      `./src/test/java`


2. Run from command line (beta)
    - Open command line in the programs' root directory;
    - Use the command: `mvn spring-boot:run`
    - Resize window to fit the application interface.

The latest option, although would be visually more clean, is more prone to errors and can be bugged depending on the
console in use.

### Setup

To run the program it is important to set up a database. For that, it's recommended to use the following sql script to
define the databases that were predefined by the program and its user:

```
CREATE DATABASE IF NOT EXISTS datalayer;
CREATE DATABASE IF NOT EXISTS datalayer_tests;

CREATE USER IF NOT EXISTS 'Ben'@'localhost' IDENTIFIED BY 'Password-123';
GRANT ALL PRIVILEGES ON datalayer.* TO 'Ben'@'localhost';
GRANT ALL PRIVILEGES ON datalayer_tests.* TO 'Ben'@'localhost';
FLUSH PRIVILEGES;
```

It is possible to change the properties in the application.properties file to a custom user and database. The tests
properties are defined in each test.

## Menu

The Menu consists of an interpreter that listens and recognizes keywords that allow the user to create, update or delete
data. It can also display information to help the user navigate the software. Once a CRM operation has been selected,
the menu prompts you to enter information about the topic of interest. Once you have finished using the CRM, it gives
you the option to exit the program and save the entered records.

### How to navigate the menu

In order to navigate through the CRM, the user must enter the corresponding command to execute the action.

```
Enter help (-a) for a list of valid commands!
Enter exit to close the application!
```

- If the player wants to see the commands of the CRM, he must input: **help** or **help -a**.
- If the player wants to exit the program, he must input: **exit**.

## Help menu

If you typed **help**, the menu with the essential commands shows up.

```
“new lead” - Creates a new Lead
"new salesrep" - Creates a new Sales Representative
“convert <ID>” - Converts a Lead into an Opportunity
“close-won <ID>” - Close Won Opportunity
“close-lost <ID>” - Close Lost Opportunity
“lookup <OBJECT> <ID>” - Search for specific Lead, Opportunity, Account, Contact or SalesRep
“show <OBJECT PLURAL>” - List all Leads, Opportunities, Accounts, Contacts or SalesRep
                                                                                          
"help (-a)" - Lists essential/all help commands
"exit" - Exits the program
```

And **help -a** will show all available commands. Being the ones missing:

```
"Report Lead by SalesRep" - Reports the number of leads per SalesRep
"Report Opportunity by <PROPERTY>" - Reports the number of opportunities per <SalesRep>, <Product>, <Country>, <City> or <Industry>
"Report CLOSED-WON by <PROPERTY>" - Reports the number of closed-won opportunities per <SalesRep>, <Product>, <Country>, <City> or <Industry>
"Report CLOSED-LOST by <PROPERTY>" - Reports the number of closed-lost opportunities per <SalesRep>, <Product>, <Country>, <City> or <Industry>
"Report OPEN by <PROPERTY>" - Reports the number of open opportunities per <SalesRep>, <Product>, <Country>, <City> or <Industry>
"Mean <PROPERTY>" - Reports the mean value of <EmployeeCount>, <Quantity> or <Opps per Account>
"Median <PROPERTY>" - Reports the median value of <EmployeeCount>, <Quantity> or <Opps per Account>
"Max <PROPERTY>" - Reports the maximum value of <EmployeeCount>, <Quantity> or <Opps per Account>
"Min <PROPERTY>" - RReports the minimum value of <EmployeeCount>, <Quantity> or <Opps per Account>
```

- where ID is a valid number assigned to one of the Leads/Opportunities in the database
- where OBJECT is **Lead**, **Opportunity**, **Account**, **Contact** or **SalesRep**
- where OBJECT PLURAL is **Leads**, **Opportunities**, **Accounts**, **Contacts** or **SalesReps**
- where PROPERTY can be **SalesRep**, **Product**, **Country**, **City**, **Industry**, **EmployeeCount**, **Quantity**
  or **Opps per Account**, depending on the operation.

## Description of the CRM Components

### Lead

Lead will be understood as a prospect, that is, a person who shows interest in the product. It is important to keep in
mind that lead is the first instance of the relationship with a potential client. In the next section we will see what
attributes a lead has.

### Contact

When a prospect or lead, in addition to showing interest, already requests a quote, then the Contact is generated, which
will have the information of the lead.

### Opportunity

Let's understand an opportunity as a possible transaction between the client and the company, the opportunity has three
possible states, these are:

* OPEN (if the client has not yet decided whether or not to accept the quote)
* CLOSED_WON (if the client accepted the quote)
* CLOSED_LOST (if the client did not accept the quote)

### SalesRep

A salesrep or sales representative is the person responsible for negotiating with the client and managing the sales
process, either on the lead phase or on the opportunity one.

### Account

An account is the most detailed record of the client who has requested a quote or inquiry. This means that it is stored
as a potential customer in the CRM history.

## Walkthrough CRM

### Create New Lead

If you typed „new lead” in the Main menu (point 1) or Help menu (point 2), then the menu for creating new lead shows up.

```
Create New Lead

      Name:
      
      Phone Number:
      
      Email:
      
      Company Name:
```

You can see there all the information that will be needed: name, phone number, email and company name.

You will be asked for this data, please type them in the terminal below:

* name of new Lead (for example “John Snow”)

```
Insert Lead Name: “John Snow”
```

* phone number of new Lead (for example “0012123456789”)

```
Insert Lead Phone Number: “0012123456789”
```

* email of new Lead for example “john.snow@gmail.com”

```
Insert Lead Email: “john.snow@gmail.com”
```

* company name this new lead works for (for example “The Wall”)

```
Insert Lead Company Name: “The Wall”
```

Next you will see all the data you typed, please hit:

* **ENTER** to confirm Lead creation or type

* **back** to cancel Lead creation.

The program will then ask you to associate a SalesRep to the lead. If there is no SalesRep available, it will start the
creation of one. Otherwise, the following will show up:

```
y - create new SalesRep | n - use existing SalesRep | back - return to the main menu
```

If you select to create a new SalesRep, the application will show a new lead menu. Using an existing SalesRep the
application will ask for the SalesRep id.

In the end, the program will display all the information and prompt confirmation for the creation of the data, and
return to the main menu.

### Create New SalesRep

The creation of a lead can be done by typing:

```
new  salesrep
```

or after creating a Lead.

The creation of the SalesRep will only prompt for the name:

```
Create New SalesRep

Name:
```

After creating a SalesRep, a confirmation will appear, and then, it will return to the main menu.

### Convert Lead into an Opportunity

The command for converting a Lead into an Opportunity is:

```
convert <ID>
```

in the Main menu (point 1) or Help menu (point 2), where ID is a valid number assigned to one of the Leads in the
database (for example “convert 1”).

When you type “convert 1” the program will ask to enter all the information that will be needed to Create New
Opportunity.

```
Create New Opportunity

      Product:
      
      Quantity:
```

You will be asked for this data, please type them in the terminal below:

```
Insert Product type [HYBRID, FLATBED or BOX]:
```

In this case will be “HYBRID”

```
Insert Quantity:
```

We going to use as example 20.

Next you will see the data you typed together with Contact Name and Status OPEN.

```
Create New Opportunity

Product: HYBRID

Quantity: 20

Contact Name: Jose

Status: OPEN


ENTER - confirm Opportunity information | back - return to the main menu
```

Please hit:

* **ENTER** key to confirm Opportunity information or type
* **back** to return to the main menu.

If you hit ENTER, there will be window with all the information that will be needed to Create New Account: Industry,
Number of Employees, City and Country.

```
Create New Account

Industry:

Number of Employees:

City:

Country:
```

You will be asked for this data, please type them in the terminal below:

```
Insert Industry [PRODUCE, ECOMMERCE, MANUFACTURING, MEDICAL, or OTHER]:
```

For example type “PRODUCE”.

```
Insert Employee Count:
```

This time we take 10.

```
Insert City:
```

we choose “Madrid”.

```
Insert Country:
```

"Spain"

Next you will see the data you typed for New Account.

```
Create New Account

Industry: PRODUCE

Number of Employees: 10

City: Madrid

Country: Spain

ENTER - delete Lead and create Contact, Opportunity and Account | back - cancel Lead conversion
```

* Hit **ENTER** key to delete Lead and create Contact, Opportunity and Account or type
* **back** to cancel Lead conversion.

Then Main menu shows up (back to point 1).

### Close Won Opportunity

When an opportunity is accepted by the client, then the opportunity is closed as Close Won.

Procedure for changing the Opportunity status to Close_Won.

It will show up if you typed:

```
close-won <ID>
```

in the Main menu (point 1) or Help menu (point 2), where ID is a valid number assigned to one of the Opportunities in
the database (for example “close-won 1”).

The status of this Opportunity is changed into CLOSED_WON and then Main menu shows up (back to point 1).

```
Available Opportunities 

Id: 1, Product: HYBRID, Quantity: 10, Decision Maker: Jose, Status: CLOSED_WON
```

### Close Lost Opportunity

In case the client rejects the quote then the opportunity will be closed as Close_Lost.

Procedure for changing the Opportunity status to Close_Lost.

It will show up if you typed:

```
close-lost <ID>
```

in the Main menu (point 1) or Help menu (point 2), where ID is a valid number assigned to one of the Opportunities in
the database (for example “close-lost 2”).

The status of this Opportunity is changed into CLOSED_LOST and then Main menu shows up (back to point 1).

```
Available Opportunities 

Id: 1, Product: HYBRID, Quantity: 10, Decision Maker: Jose, Status: CLOSED_LOST
```

### Search for specific Lead, Opportunity, Account or Contact

Menu for searching for specific Lead, Opportunity, Account. Contact or SalesRep.

It will show up if you typed:

```
lookup <OBJECT> <ID>
```

in the Main menu (point 1) or Help menu (point 2)

* where ID is a valid number assigned to one of the Leads/Opportunities in the database,
* where OBJECT is “Lead”, “Opportunity”, “Account”, “Contact” or "SalesRep"

(for example type “lookup Lead 2”).

If there is Lead/Opportunity/Account/Contact with given ID, then all the information will be shown.

For example for Lead it will be Name, Phone Number, Email and Company Name.

```
Information of Lead with id 3

Name: simon

Phone Number: +4915254567956

Email: ms@uc.cl

Company Name: air

ENTER - return to the main menu
```

Please hit ENTER to return to the main menu.

Then Main menu shows up (back to point 1).

If there is no Lead/Opportunity/Account/Contact/SalesRep with given ID, then you will see warning for example “There is
no Lead with id 1” and go back to the Main menu.

```
There is no Lead with id 1 
```

### List all Leads, Opportunities, Accounts or Contacts

Procedure for listing all Leads, Opportunities, Accounts, Contacts or SalesReps.

It will show up if you typed:

```
show <OBJECT PLURAL>
```

in the Main menu (point 1) or Help menu (point 2),

* where OBJECT PLURAL is “Leads”, “Opportunities”, “Accounts”, “Contacts” or "SalesRep"

(for example type “show Leads”).

```
show leads
```

Then the list of Leads/Opportunities/Accounts/Contacts will appear with all the information.

For example for Leads it will be:

```
Available Leads

Id: 3, Name: simon, Email: ms@uc.cl, Phone: +4921561648232, Company: air


ENTER - return to the main menu 
```

Please hit **ENTER** to return to the main menu.

Then Main menu shows up (back to point 1).

### Reporting Functionality

Returns specific info concerning the database.

All reporting can be accessed by typing "Report" followed by specific commands

Reports for opportunities limited by Product, Country, City, Industry and Salesrep can be accessed using the following
format:

```
Report Opportunity by [Product, Country, City, Industry or Salesrep]
Report CLOSED-WON by [Product, Country, City, Industry or Salesrep]
Report CLOSED-LOST by [Product, Country, City, Industry or Salesrep]
Report OPEN by [Product, Country, City, Industry or Salesrep]
```

You can also get a report of Leads by Salesrep using the command :

```
Report Lead by Salesrep
```

Reports for Mean, Median, Max and Min statistics can be accessed in the following format:

```
[Mean, Median, Max or Min] [EmployeeCount, Quantity or Opps per Account]
```

### Exit the program

Procedure for saving and exiting the program.

It will show up if you typed:

```
exit
```

in the Main menu (point 1) or Help menu (point 2).

Then the menu will show:

```
Goodbye.
```

and the program stops.

## Use Case Diagram

![Use Case Diagram](https://user-images.githubusercontent.com/84567251/128645874-090c8890-2091-489b-ba51-d1bf88a2222e.png)

## Class Diagram

![Class Diagram](https://user-images.githubusercontent.com/84567251/128645939-aa22a449-5c6f-498b-8fa5-702bc6e89c25.png)

## Notes

- Some tests, from PrinterTest, will not pass in Ubuntu or Mac machines due to the different type of linebreak.

## The Team

by:  
**Ben The Warrior**

- [Joaodss](https://github.com/Joaodss)
- [MigNeves](https://github.com/MigNeves)
- [ShaunRly](https://github.com/ShaunRly)
- [simonpedrorios](https://github.com/simonpedrorios)
- [UrszulaF](https://github.com/UrszulaF)
