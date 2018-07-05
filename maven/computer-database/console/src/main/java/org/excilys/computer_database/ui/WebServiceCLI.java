package org.excilys.computer_database.ui;

import java.net.URI;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Scanner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

import org.excilys.computer_database.dto.CompanyDto;
import org.excilys.computer_database.dto.ComputerDto;
import org.excilys.computer_database.dto.UserDto;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.model.DBModelType;
import org.excilys.computer_database.util.Util;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class WebServiceCLI {

  private Client client;
  private WebTarget target;
  private Invocation.Builder invocationBuilder;
  private Response response;

  private URI getBaseURI() {  
    return UriBuilder.fromUri("http://localhost:8080/webservice").build();  
  }  

  public void run() {
    client = ClientBuilder.newClient();
    Scanner scan = new Scanner(System.in);

    WSMenuOption menuOption = null;
    while (menuOption != WSMenuOption.DONE) {

      menuOption = displayMenuOptionAndGetSelectedOption(scan);

      switch (menuOption) {

      case AUTHENTICATE:
        authenticate(scan);
        break;

      case CHANGE_PASSWORD:
        changePassword(scan);
        break;

      case CREATE_COMPANY:
        createCompany(scan);
        break;

      case CREATE_COMPUTER:
        createComputer(scan);
        break;

      case DELETE_COMPANY:
        deleteCompany(scan);
        break;

      case DELETE_COMPUTER:
        deleteComputer(scan);
        break;

      case LIST_COMPANIES:
        listCompanies(scan);
        break;

      case LIST_COMPUTERS:
        listComputers(scan);
        break;

      case REGISTER:
        register(scan);
        break;

      case UPDATE_COMPANY:
        updateCompany(scan);
        break;

      case UPDATE_COMPUTER:
        updateComputer(scan);
        break;

      case DONE:
        System.out.println("Thank you for using Computer Database");
        break;

      default:
        System.out.println("You didn't select a valid option.");
        break;
      }

      if (menuOption != WSMenuOption.DONE) {
        System.out.println("\n\nPress enter to continue...");
        scan.nextLine();
      }
    }
    scan.close();
    if (client != null) {
      client.close();
    }
  }

  private void updateCompany(Scanner scan) {
    boolean foundSomething = false;
    CompanyDto companyToUpdate = null;
    System.out.println("Update a company");
    while (!foundSomething) {
      int idToUpdateInput = askForInt("Please enter the id of the company you want to update:", scan, false);
      target = client.target(getBaseURI());  
      target = target.path("companies").path(Integer.toString(idToUpdateInput));
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      companyToUpdate = response.readEntity(CompanyDto.class);
      if (response.getStatus() == 200) {
        foundSomething = true;
      }
    }
    System.out.println(companyToUpdate.toString());
    System.out.print("Please enter the new name of the company:");
    companyToUpdate.setName(scan.nextLine());

    target = client.target(getBaseURI());  
    target = target.path("companies").path(Integer.toString(companyToUpdate.getId()));
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.put(Entity.entity(companyToUpdate, MediaType.APPLICATION_JSON));
    if (response.getStatus() != 200) {
      System.out.println(response.getStatusInfo());
    }
  }


  private void register(Scanner scan) {
    System.out.println("Please enter the username:");
    String username = scan.nextLine();
    System.out.println("Please enter the password:");
    String password = scan.nextLine();
    UserDto userDto = new UserDto();
    userDto.setUsername(username);
    userDto.setPassword(password);
    target = client.target(getBaseURI());  
    target = target.path("users");
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.post(Entity.entity(userDto, MediaType.APPLICATION_JSON));
  }
  /**
   * Creates a company.
   * @param scan to get user inputs.
   */
  private void createCompany(Scanner scan) {
    System.out.println("Company creation");
    System.out.println("Please enter the name of the company you want to create:");
    String companyNameInput = scan.nextLine();
    Company companyToCreate = new Company();
    companyToCreate.setName(companyNameInput);
    CompanyDto companyDtoToCreate = new CompanyDto(companyToCreate);
    target = client.target(getBaseURI());  
    target = target.path("companies");
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.post(Entity.entity(companyDtoToCreate, MediaType.APPLICATION_JSON));
    if (response.getStatus() != 200) {
      System.out.println(response.getStatusInfo());
    }
  }

  private void changePassword(Scanner scan) {
    System.out.println("Please enter your username:");
    String username = scan.nextLine();
    System.out.println("Please enter your new password:");
    String password = scan.nextLine();
    UserDto userDto = new UserDto();
    userDto.setUsername(username);
    userDto.setPassword(password);
    target = client.target(getBaseURI());  
    target = target.path("users");
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.put(Entity.entity(userDto, MediaType.APPLICATION_JSON));
  }

  private void authenticate(Scanner scan) {
    System.out.println("Please enter your username:");
    String username = scan.nextLine();
    System.out.println("Please enter your password:");
    String password = scan.nextLine();
    if (client != null) {
      client.close();
    }
    client = ClientBuilder.newClient(); 
    HttpAuthenticationFeature auth = HttpAuthenticationFeature.basic(username, password);
    client.register(auth);
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
  public static WSMenuOption displayMenuOptionAndGetSelectedOption(Scanner scan) {
    System.out.println("Welcome to the CLI of Computer Database!");
    System.out.println("Pick one of the following:");
    System.out.println(" 1 - Authenticate.");
    System.out.println(" 2 - Register.");
    System.out.println(" 3 - Change your password.");
    System.out.println(" 4 - List all COMPUTERS in the database.");
    System.out.println(" 5 - CREATE a computer in the database.");
    System.out.println(" 6 - UPDATE informations about a computer.");
    System.out.println(" 7 - DELETE a computer of the database.");
    System.out.println(" 8 - List all COMPANIES in the database.");
    System.out.println(" 9 - CREATE a company in the database.");
    System.out.println("10 - UPDATE informations about a company.");
    System.out.println("11 - DELETE a company of the database.");
    System.out.println("12 - I am done, thank you.");
    int userInputChoice = -1;
    while (userInputChoice < 1 || userInputChoice > WSMenuOption.values().length) {
      userInputChoice = askForInt("Your choice: ", scan, false);
    }
    return WSMenuOption.values()[userInputChoice - 1];
  }

  /**
   * Get the list of computers.
   * @param scan to get user inputs.
   */
  public void listComputers(Scanner scan) {
    System.out.println("Computer list");
    startPageNavigation(scan, DBModelType.COMPUTER);
  }

  /**
   * List the companies.
   * @param scan to get user inputs.
   */
  private void listCompanies(Scanner scan) {
    System.out.println("Company list");
    startPageNavigation(scan, DBModelType.COMPANY);
  }

  /**
   * Creates a computer.
   * @param scan to get user inputs.
   */
  private void createComputer(Scanner scan) {
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
    ComputerDto computerDtoToCreate = new ComputerDto(computerToCreate);
    target = client.target(getBaseURI());  
    target = target.path("computers");
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.post(Entity.entity(computerDtoToCreate, MediaType.APPLICATION_JSON));
    if (response.getStatus() != 200) {
      System.out.println(response.getStatusInfo());
    }
  }

  /**
   * Updates a computer.
   * @param scan to get user inputs.
   */
  private void updateComputer(Scanner scan) {
    boolean foundSomething = false;
    ComputerDto computerToUpdate = null;
    System.out.println("Update a computer");
    while (!foundSomething) {
      int idToUpdateInput = askForInt("Please enter the id of the computer you want to update:", scan, false);
      target = client.target(getBaseURI());  
      target = target.path("computers").path(Integer.toString(idToUpdateInput));
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      computerToUpdate = response.readEntity(ComputerDto.class);
      if (response.getStatus() == 200) {
        foundSomething = true;
      }
    }

    System.out.println(computerToUpdate.toString());
    UpdateOption updateOption = null;
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
        computerToUpdate.setIntroduced(askForDate("Please enter the new introduced date of the computer with the format yyyy-mm-dd.", scan, true).toString());
        break;

      case DISCONTINUED:
        computerToUpdate.setDiscontinued(askForDate("Please enter the new discontinued date of the computer with the format yyyy-mm-dd.", scan, true).toString());
        break;

      case COMPANY_ID:
        computerToUpdate.setCompanyId(askForInt("Please enter the new id of the company that made the computer:", scan, true));
        break;

      case DONE:
        target = client.target(getBaseURI());  
        target = target.path("computers").path(Integer.toString(computerToUpdate.getId()));
        invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
        response = invocationBuilder.put(Entity.entity(computerToUpdate, MediaType.APPLICATION_JSON));
        if (response.getStatus() != 200) {
          System.out.println(response.getStatusInfo());
        }              
        break;

      default:
        System.out.println("You didn't enter a valid option");
        break;
      }
    }
  }

  /**
   * Deletes a computer.
   * @param scan to get user inputs.
   */
  private void deleteComputer(Scanner scan) {
    System.out.println("Delete a computer");
    int idToDelete = askForInt("Please enter the id of the computer you want to delete:", scan, false);
    target = client.target(getBaseURI());  
    target = target.path("computers").path(Integer.toString(idToDelete));
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.delete();
    if (response.getStatus() != 200) {
      System.out.println("You must be logged in and have admin role to delete a computer.");
    }
  }
  /**
   * Deletes a company and its related computers.
   * @param scan to get user inputs.
   */
  private void deleteCompany(Scanner scan) {
    System.out.println("Delete a company");
    int idToDelete = askForInt("Please enter the id of the company you want to delete:", scan, false);
    target = client.target(getBaseURI());  
    target = target.path("companies").path(Integer.toString(idToDelete));
    invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
    response = invocationBuilder.delete();
    if (response.getStatus() != 200) {
      System.out.println("You must be logged in and have admin role to delete a company.");
    }
  }

  /**
   * Display the computers per pages.
   * @param scan Scanner object to read user inputs
   * @param type type of the item to display
   */
  public void startPageNavigation(Scanner scan, DBModelType type) {
    int currentPage = 1;
    String userInput = "";
    int numberOfItems = 0;
    switch (type) {
    case COMPANY:
      target = client.target(getBaseURI());  
      target = target.path("companies").path("count");
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      if (response.getStatus() == 200) {
        numberOfItems = response.readEntity(Integer.class);
      }
      break;

    case COMPUTER:
      target = client.target(getBaseURI());  
      target = target.path("computers").path("count");
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      if (response.getStatus() == 200) {
        numberOfItems = response.readEntity(Integer.class);
      }
      break;
    }
    int numberOfPages = (int) numberOfItems / 10 + 1;
    if (numberOfItems % 10 == 0) {
      numberOfPages = numberOfItems / 10;
    }
    currentPage = printPage(currentPage, type, numberOfPages);

    while (!userInput.equalsIgnoreCase("d")) {
      if (numberOfItems != 0) {
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
      } else {
        userInput = "d";
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
  public int printPage(int pageNumber, DBModelType type, int numberOfPages) {
    if (numberOfPages < pageNumber) {
      pageNumber = numberOfPages;
    }
    if (pageNumber < 1) {
      pageNumber = 1;
    }
    switch (type) {
    case COMPANY:
      target = client.target(getBaseURI());  
      target = target.path("companies").queryParam("currentPage", pageNumber).queryParam("numberOfItemPerPage", 10);
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      if (response.getStatus() == 200) {
        ArrayList<CompanyDto> companies = response.readEntity(new GenericType<ArrayList<CompanyDto>>() {});
        for (CompanyDto company: companies) {
          System.out.println(company.toString());
        }
      } else {
        System.out.println("An error occured, are you logged in ?");
      }

      break;

    case COMPUTER:
      target = client.target(getBaseURI());  
      target = target.path("computers").queryParam("currentPage", pageNumber).queryParam("numberOfItemPerPage", 10);
      invocationBuilder =  target.request(MediaType.APPLICATION_JSON);
      response = invocationBuilder.get();
      if (response.getStatus() == 200) {
        ArrayList<ComputerDto> computers = response.readEntity(new GenericType<ArrayList<ComputerDto>>() {});
        for (ComputerDto computer: computers) {
          System.out.println(computer.toString());
        }
      } else {
        System.out.println("An error occured, are you logged in ?");
      }
      break;
    }
    return pageNumber;
  }


}
