package org.excilys.computer_database.ui;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.model.Page;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.validation.ComputerValidationStatus;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public class Main {

  /**
   * Main function containing the CLI.
   * @param args not used
   */
  public static void main(String[] args) {
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
    System.out.println("7 - I am done, thank you.");
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
    ArrayList<Object> computers = new ArrayList<Object>(ComputerService.getInstance().getComputers());
    new Page(computers, scan).startPageNavigation();
  }

  /**
   * List the companies.
   * @param scan to get user inputs.
   */
  private static void listCompanies(Scanner scan) {
    System.out.println("Company list");
    ArrayList<Object> companies = new ArrayList<Object>(CompanyService.getInstance().getCompanies());
    new Page(companies, scan).startPageNavigation();
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
      Computer computerDetails = ComputerService.getInstance().getComputerDetails(idInput);
      if (computerDetails != null) {
        foundSomething = true;
        System.out.println(computerDetails.toString());
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
    ComputerValidationStatus status = null;
    System.out.println("Computer creation");
    System.out.println("Please enter the name of the computer you want to create:");
    String computerNameInput = scan.nextLine();
    Date introduced = askForDate("Please enter the introduced date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
    Date discontinued = askForDate("Please enter the discontinued date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true);
    int companyId = askForInt("Please enter the id of the company that made the computer you want to add to the database. Type \"skip\" to skip.", scan, true);
    Computer computerToCreate = new Computer(-1, computerNameInput, introduced, discontinued, companyId, null);
    Pair<ComputerValidationStatus, String> result = ComputerService.getInstance().createComputer(computerToCreate);
    status = result.left;
    System.out.println(result.right);
    while (status != ComputerValidationStatus.OK) {
      switch (status) {

      case COMPANY_ID_ERROR:
        companyId = askForInt("Please enter the id of the company that made the computer you want to add to the database. Type \"skip\" to skip.", scan, true);
        break;

      case DATE_ERROR:
        computerToCreate.setIntroduced(askForDate("Please enter the introduced date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true));
        computerToCreate.setDiscontinued(askForDate("Please enter the discontinued date of the computer you want to create with the format yyyy-mm-dd. Type \"skip\" to skip", scan, true));
        break;

      default:
        break;

      }
      result = ComputerService.getInstance().createComputer(computerToCreate);
      status = result.left;
      System.out.println(result.right);
    }
  }

  /**
   * Updates a computer.
   * @param scan to get user inputs.
   */
  private static void updateComputer(Scanner scan) {
    boolean foundSomething = false;
    Computer computerToUpdate = null;
    System.out.println("Update a computer");
    while (!foundSomething) {
      int idToUpdateInput = askForInt("Please enter the id of the computer you want to update:", scan, false);
      computerToUpdate = ComputerService.getInstance().getComputerDetails(idToUpdateInput);
      if (computerToUpdate != null) {
        foundSomething = true;
      }
    }

    System.out.println(computerToUpdate.toString());
    UpdateOption updateOption = null;
    ComputerValidationStatus status = null;

    while (status != ComputerValidationStatus.OK) {
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
          computerToUpdate.setName(scan.nextLine());
          break;

        case INTRODUCED:
          computerToUpdate.setIntroduced(askForDate("Please enter the new introduced date of the computer with the format yyyy-mm-dd.", scan, true));
          break;

        case DISCONTINUED:
          computerToUpdate.setDiscontinued(askForDate("Please enter the new discontinued date of the computer with the format yyyy-mm-dd.", scan, true));
          break;

        case COMPANY_ID:
          computerToUpdate.getCompany().setId(askForInt("Please enter the new id of the company that made the computer:", scan, true));
          break;

        case DONE:
          Pair<ComputerValidationStatus, String> result = ComputerService.getInstance().updateComputer(computerToUpdate);
          status = result.left;
          if (status != ComputerValidationStatus.OK) {
            updateOption = null;
          }
          System.out.println(result.right);
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
    ComputerService.getInstance().deleteComputer(idToDelete);
  }

}