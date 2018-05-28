package org.excilys.computer_database.model;

import java.util.ArrayList;
import java.util.Scanner;

import org.excilys.computer_database.util.Util;

public class Page {

  private int numberOfItemPerPage = 10;
  private ArrayList<Object> items;
  private int currentPage;
  private int numberOfPages;
  private int numberOfItems;
  private Scanner scan;

  /**
   * Page class to display content of database as pages.
   * @param items items you want to put in pages
   * @param scan Scanner object to handle CLI navigation
   * @param numberOfItemPerPage Number of items you want per page
   */
  public Page(ArrayList<Object> items, Scanner scan, int numberOfItemPerPage) {
    this.items = items;
    this.numberOfItemPerPage = numberOfItemPerPage;
    this.numberOfItems = items.size();
    this.currentPage = 0;
    int remainder = items.size() % numberOfItemPerPage;
    if (remainder == 0) {
      this.numberOfPages = (int) (items.size() / numberOfItemPerPage);
    } else {
      this.numberOfPages = (int) (items.size() / numberOfItemPerPage) + 1;
    }
    this.scan = scan;
    this.fillPages();
  }

  /**
   * Page class to display content of database as pages.
   * @param items items you want to put in pages
   */
  public Page(ArrayList<Object> items) {
    this(items, null, 10);
  }

  /**
   * Page class to display content of database as pages.
   * @param items items you want to put in pages
   * @param numberOfItemPerPage Number of items you want per page
   */
  public Page(ArrayList<Object> items, int numberOfItemPerPage) {
    this(items, null, numberOfItemPerPage);
  }

  /**
   * Page class to display content of database as pages.
   * @param items items you want to put in pages
   * @param scan Scanner object to handle CLI navigation
   */
  public Page(ArrayList<Object> items, Scanner scan) {
    this(items, scan, 10);
  }

  private ArrayList<ArrayList<Object>> pages;

  /**
   * Uses items to create all the pages using the parameter number of line per page.
   */
  private void fillPages() {
    this.pages = new ArrayList<ArrayList<Object>>();
    int lastLine = items.size();
    int currentLine = 0;
    for (int i = 0; i < this.numberOfPages; i++) {
      this.pages.add(new ArrayList<Object>());
      int lineLimit = numberOfItemPerPage;
      if (i == this.numberOfPages - 1) {
        lineLimit = lastLine - currentLine;
      }
      for (int j = 0; j < lineLimit; j++) {
        currentLine++;
        this.pages.get(i).add(this.items.get(i * numberOfItemPerPage + j));
      }
    }
  }

  /**
   * Return the page with the number pageNumber.
   * @param pageNumber the number of the page you want to access
   * @return the list of lines
   */
  private ArrayList<Object> getPage(int pageNumber) {
    if (pageNumber > this.numberOfPages - 1 || pageNumber < 0) {
      pageNumber = this.currentPage;
      System.out.println("Page unreachable");
    }
    this.currentPage = pageNumber;
    return this.pages.get(this.currentPage);
  }

  /**
   * Return the next page.
   * @return the list of lines of the next page
   */
  private ArrayList<Object> getNextPage() {
    if (this.currentPage + 1 <= this.numberOfPages - 1) {
      this.currentPage++;
      return getPage(this.currentPage);
    } else {
      System.out.println("Last page reached");
      return null;
    }
  }

  /**
   * Return the previous page.
   * @return the list of lines of the previous page
   */
  private ArrayList<Object> getPreviousPage() {
    if (this.currentPage - 1 >= 0) {
      this.currentPage--;
      return getPage(this.currentPage);
    } else {
      System.out.println("First page reached");
      return null;
    }
  }

  /**
   * Starts the page navigation.
   */
  public void startPageNavigation() {
    printPage(getPage(0));
    String userInput = "";
    while (!userInput.equalsIgnoreCase("d")) {
      System.out.println((this.currentPage + 1) + "/" + (this.numberOfPages) + " (n: next, p: previous, xx: go to xx page, d: done)");

      userInput = scan.nextLine();
      if (Util.isInteger(userInput)) {
        int pageNumber = Integer.parseInt(userInput);
        printPage(getPage(pageNumber - 1));
      } else {
        switch (userInput) {

        case "n":
        case "N":
          printPage(getNextPage());
          break;

        case "p":
        case "P":
          printPage(getPreviousPage());
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
   * Shows the current page.
   * @param linesForPage lines you want to show
   */
  private void printPage(ArrayList<Object> linesForPage) {
    if (linesForPage != null) {
      for (Object line: linesForPage) {
        System.out.println(line.toString());
      }
    }
  }

  /**
   * Shows the current page.
   * @param pageNumber lines you want to show
   * @return return the list of companies for the current page
   */
  public ArrayList<Company> getCompaniesForPage(int pageNumber) {
    ArrayList<Company> companiesForPage = new ArrayList<>();
    ArrayList<Object> lines = getPage(pageNumber);
    if (lines != null) {
      for (Object line: lines) {
        companiesForPage.add((Company) line);
      }
    }
    return companiesForPage;
  }

  /**
   * Shows the current page.
   * @param pageNumber lines you want to show
   * @return return the list of computers for the current page
   */
  public ArrayList<Computer> getComputersForPage(int pageNumber) {
    ArrayList<Computer> computersForPage = new ArrayList<>();
    ArrayList<Object> lines = getPage(pageNumber);
    if (lines != null) {
      for (Object line: lines) {
        computersForPage.add((Computer) line);
      }
    }
    return computersForPage;
  }

  /**
   * Getter of numberOfPages.
   * @return numberOfPages
   */
  public int getNumberOfPages() {
    return this.numberOfPages;
  }

  /**
   * Getter of numberOfPages.
   * @return numberOfPages
   */
  public int getNumberOfItems() {
    return this.numberOfItems;
  }

  /**
   * Change the number of item per page.
   * @param numberOfItemPerPage The new number of items
   */
  public void setNumberOfItemsPerPages(int numberOfItemPerPage) {
    this.numberOfItemPerPage = numberOfItemPerPage;
    this.fillPages();
  }

}
