package org.excilys.computer_database.ui;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;

import org.excilys.computer_database.exceptions.CDBObjectCompanyIdException;
import org.excilys.computer_database.exceptions.CDBObjectDateException;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.model.DBModelType;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.spring.PersistenceConfig;
import org.excilys.computer_database.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class Main {

  private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

  private static CompanyService companyService;
  private static ComputerService computerService;

  /**
   * Main function containing the CLI.
   * @param args not used
   */
  public static void main(String[] args) {
    AbstractApplicationContext context = new AnnotationConfigApplicationContext(PersistenceConfig.class);
    companyService = context.getBean(CompanyService.class);
    computerService = context.getBean(ComputerService.class);
    Scanner scan = new Scanner(System.in);
    MenuOption menuOption = null;
    while (menuOption != MenuOption.DONE) {

      menuOption = displayMenuOptionAndGetSelectedOption(scan);

      switch (menuOption) {
      case LIST_COMPUTERS:
        listComputers(scan);
        break;

      case LIST_COMPANIES:
        listCompanies(scan);
        break;

      case COMPUTER_DETAILS:
        computerDetails(scan);
        break;

      case CREATE_COMPUTER:
        createComputer(scan);
        break;

      case UPDATE_COMPUTER:
        updateComputer(scan);
        break;

      case DELETE_COMPUTER:
        deleteComputer(scan);
        break;

      case DELETE_COMPANY:
        deleteCompany(scan);
        break;

      case DONE:
        System.out.println("Thank you for using Computer Database");
        break;

      default:
        System.out.println("You didn't select a valid option.");
        break;
      }
      if (menuOption != MenuOption.DONE) {
        System.out.println("\n\nPress enter to continue...");
        scan.nextLine();
      }
    }
    context.close();
    scan.close();
  }

  /**
   * Ask the user of a date and make sure it is in the good format.
   * @param query Message you want to ask the user
   * @param scan To get user inputs
   * @param skippable True if you want the user to be able to skip this parameter and therefore returning null.
   * @return the date
   */
  public static Date askForDate(String query, Scanner scan, boolean skippable) {
    Date date = null;
    boolean forcedNull = false;
    while (date == null && !(forcedNull && skippable)) {
      try {
        System.out.println(query);
        System.out.print("You can type \"now\" to set the date at today's date.");
        String userDateInput = scan.nextLine();
        if (userDateInput.equalsIgnoreCase("now")) {
          date = new Date(System.currentTimeMillis());
        } else if (userDateInput.equalsIgnoreCase("skip")) {
          date = null;
          forcedNull = true;
        } else {
          date = Date.valueOf(userDateInput);
        }
      } catch (IllegalArgumentException exception) {
        date = null;
        System.out.println("The date you typed is not in the right format!");
      }
    }
    return date;
  }

  /**
   * Ask the user of an integer and make sure it is in the good format.
   * @param query Message you want to ask the user
   * @param scan To get user inputs
   * @param skippable True if you want the user to be able to skip this parameter and therefore returning null.
   * @return the int
   */
  public static int askForInt(String query, Scanner scan, boolean skippable) {
    int userInt = -1;
    boolean forcedNull = false;
    while (userInt == -1 && !(forcedNull && skippable)) {

      System.out.println(query);
      String input = scan.nextLine();
      if (input.equalsIgnoreCase("skip")) {
        System.out.println("You skipped this part");
        forcedNull = true;
      } else {
        try {
          userInt = Integer.parseInt(input);
        } catch (NumberFormatException exception) {
          userInt = -1;
          System.out.println("The value you typed is not in the right format!");
        }
      }

    }
    return userInt;
  }

  /**
   * Displays the menu options.
   * @param scan To get user inputs
   * @return the menuOption that the user selected
   */
  public static MenuOption displayMenuOptionAndGetSelectedOption(Scanner scan) {
    System.out.println("Welcome to the CLI of Computer Database!");
    System.out.println("Pick one of the following:");
    System.out.println("1 - List all COMPUTERS in the database.");
    System.out.println("2 - List all COMPANIES in the database.");
    System.out.println("3 - View detailed informations about a computer.");
    System.out.println("4 - CREATE a computer in the database.");
    System.out.println("5 - UPDATE informations about a computer.");
    System.out.println("6 - DELETE a computer of the database.");
    System.out.println("7 - DELETE a company of the database.");
    System.out.println("8 - I am done, thank you.");
    int userInputChoice = -1;
    while (userInputChoice < 1 || userInputChoice > MenuOption.values().length) {
      userInputChoice = askForInt("Your choice: ", scan, false);
    }
    return MenuOption.values()[userInputChoice - 1];
  }

  /**
   * Get the list of computers.
   * @param scan to get user inputs.
   */
  public static void listComputers(Scanner scan) {
    System.out.println("Computer list");
    startPageNavigation(scan, DBModelType.COMPUTER);
  }

  /**
   * List the companies.
   * @param scan to get user inputs.
   */
  private static void listCompanies(Scanner scan) {
    System.out.println("Company list");
    startPageNavigation(scan, DBModelType.COMPANY);
  }

  /**
   * Gives details about a computer.
   * @param scan to get user inputs.
   */
  private static void computerDetails(Scanner scan) {
    boolean foundSomething = false;
    System.out.println("Details about a computer");
    while (!foundSomething) {
      int idInput = askForInt("Please enter the id of the computer you want details of:", scan, false);
      Optional<Computer> computerDetails = computerService.getComputerDetails(idInput);
      if (computerDetails.isPresent()) {
        foundSomething = true;
        System.out.println(computerDetails.get().toString());
      } else {
        System.out.println("The id you typed doesn't match any computer in the database.");
      }
    }
  }

  /**
   * CreatesGives details about a computer.
   * @param scan to get user inputs.
   */
  private static void createComputer(Scanner scan) {
    System.out.println("Computer creation");
    System.out.println("Please enter the name of the computer you want to create:");
    String computerNameInput = scan.nextLine();
    Date introduced = askForDate("Please enter the introduced date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
    Date discontinued = askForDate("Please enter the discontinued date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
    Integer companyId = askForInt("Please enter the id of the company that made the computer you want to add to the database. Type \"skip\" to skip.", scan, true);
    if (companyId == -1) {
      companyId = null;
    }
    Computer computerToCreate = new Computer(-1, computerNameInput, introduced, discontinued, companyId, null);
    boolean creationSuccessful = false;
    while (!creationSuccessful) {
      try {
        computerService.createComputer(computerToCreate);
        creationSuccessful = true;
      } catch (CDBObjectCompanyIdException exception) {
        System.out.println(exception.getMessage());
        companyId = askForInt("Please enter the id of the company that made the computer you want to add to the database. Type \"skip\" to skip.", scan, true);
        LOGGER.error("Error when creating computer - " + exception.getMessage() + " " + computerToCreate.toString());
      } catch (CDBObjectDateException exception) {
        System.out.println(exception.getMessage());
        computerToCreate.setIntroduced(askForDate("Please enter the introduced date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true));
        computerToCreate.setDiscontinued(askForDate("Please enter the discontinued date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true));
        LOGGER.error("Error when creating computer - " + exception.getMessage() + " " + computerToCreate.toString());
      } catch (CDBObjectException exception) {
        System.out.println(exception.getMessage());
        LOGGER.error("Error when creating computer - " + exception.getMessage() + " " + computerToCreate.toString());
      }
    }
  }

  /**
   * Updates a computer.
   * @param scan to get user inputs.
   */
  private static void updateComputer(Scanner scan) {
    boolean foundSomething = false;
    Optional<Computer> computerToUpdate = null;
    System.out.println("Update a computer");
    while (!foundSomething) {
      int idToUpdateInput = askForInt("Please enter the id of the computer you want to update:", scan, false);
      computerToUpdate = computerService.getComputerDetails(idToUpdateInput);
      if (computerToUpdate.isPresent()) {
        foundSomething = true;
      }
    }

    System.out.println(computerToUpdate.toString());
    UpdateOption updateOption = null;
    boolean updateSucessful = false;
    while (!updateSucessful) {
      while (updateOption != UpdateOption.DONE) {
        System.out.println("\n\nWhat do you want to update ?");
        System.out.println("Pick one of the following:");
        System.out.println("1 - Name");
        System.out.println("2 - Introduced date");
        System.out.println("3 - Discontinued date");
        System.out.println("4 - Company id");
        System.out.println("5 - Nothing I'm done");
        int userInputChoice = -1;
        while (userInputChoice < 1 || userInputChoice > UpdateOption.values().length) {
          userInputChoice = askForInt("Your choice: ", scan, false);
        }

        updateOption = UpdateOption.values()[userInputChoice - 1];

        switch (updateOption) {

        case NAME:
          System.out.print("Please enter the new name of the computer:");
          computerToUpdate.get().setName(scan.nextLine());
          break;

        case INTRODUCED:
          computerToUpdate.get().setIntroduced(askForDate("Please enter the new introduced date of the computer with the format yyyy-mm-dd.", scan, true));
          break;

        case DISCONTINUED:
          computerToUpdate.get().setDiscontinued(askForDate("Please enter the new discontinued date of the computer with the format yyyy-mm-dd.", scan, true));
          break;

        case COMPANY_ID:
          computerToUpdate.get().getCompany().setId(askForInt("Please enter the new id of the company that made the computer:", scan, true));
          break;

        case DONE:
          try {
            computerService.updateComputer(computerToUpdate.get());
            updateSucessful = true;
          } catch (CDBObjectException exception) {
            updateOption = null;
            System.out.println(exception.getMessage());
            LOGGER.error("Error when updating computer - " + exception.getMessage() + " " + computerToUpdate.toString());
          }
          break;

        default:
          System.out.println("You didn't enter a valid option");
          break;
        }
      }
    }
  }

  /**
   * Deletes a computer.
   * @param scan to get user inputs.
   */
  private static void deleteComputer(Scanner scan) {
    System.out.println("Delete a computer");
    int idToDelete = askForInt("Please enter the id of the computer you want to delete:", scan, false);
    computerService.deleteComputer(idToDelete);
  }
  /**
   * Deletes a company and its related computers.
   * @param scan to get user inputs.
   */
  private static void deleteCompany(Scanner scan) {
    System.out.println("Delete a company");
    int idToDelete = askForInt("Please enter the id of the company you want to delete:", scan, false);
    companyService.deleteCompany(idToDelete);
  }

  /**
   * Display the computers per pages.
   * @param scan Scanner object to read user inputs
   * @param type type of the item to display
   */
  public static void startPageNavigation(Scanner scan, DBModelType type) {
    int currentPage = 1;
    String userInput = "";
    int numberOfItems = 0;
    switch (type) {
    case COMPANY:
      numberOfItems = companyService.countCompanies();
      break;

    case COMPUTER:
      numberOfItems = computerService.countComputers();
      break;
    }
    int numberOfPages = (int) numberOfItems / 10 + 1;
    if (numberOfItems % 10 == 0) {
      numberOfPages = numberOfItems / 10;
    }
    currentPage = printPage(currentPage, type, numberOfPages);

    while (!userInput.equalsIgnoreCase("d")) {
      System.out.println((currentPage) + "/" + (numberOfPages) + " (n: next, p: previous, xx: go to xx page, d: done)");

      userInput = scan.nextLine();
      if (Util.isInteger(userInput)) {
        currentPage = Integer.parseInt(userInput);
        currentPage = printPage(currentPage, type, numberOfPages);
      } else {
        switch (userInput) {

        case "n":
        case "N":
          currentPage = printPage(++currentPage, type, numberOfPages);
          break;

        case "p":
        case "P":
          currentPage = printPage(--currentPage, type, numberOfPages);
          break;

        case "d":
        case "D":
          System.out.println("Stopped page navigation");
          break;
        }
      }
    }
  }
  /**
   * Print the item of the page.
   * @param pageNumber page you want to display
   * @param type of item to display
   * @param numberOfPages Number of pages
   * @return The new page number
   */
  public static int printPage(int pageNumber, DBModelType type, int numberOfPages) {
    if (numberOfPages < pageNumber) {
      pageNumber = numberOfPages;
    }
    if (pageNumber < 1) {
      pageNumber = 1;
    }
    switch (type) {
    case COMPANY:
      ArrayList<Company> companies = companyService.getCompaniesAtPage(10, pageNumber);
      for (Company company: companies) {
        System.out.println(company.toString());
      }
      break;

    case COMPUTER:
      ArrayList<Computer> computers = computerService.getComputersWithParams(10, pageNumber, null, null);
      for (Computer computer: computers) {
        System.out.println(computer.toString());
      }
      break;
    }
    return pageNumber;
  }

}