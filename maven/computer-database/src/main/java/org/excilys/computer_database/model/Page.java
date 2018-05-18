package org.excilys.computer_database.model;

import java.util.ArrayList;
import java.util.Scanner;

import org.excilys.computer_database.util.Util;

public class Page {

  private int numberOfLinePerPage = 10;
  private ArrayList<String> lines;
  private int currentPage;
  private int numberOfPages;
  private Scanner scan;

  /**
   * Page class to display content of database as pages.
   * @param lines Lines you want to put in pages
   * @param scan Scanner object to handle CLI navigation
   */
  public Page(ArrayList<String> lines, Scanner scan) {
    this.lines = lines;
    this.currentPage = 0;
    this.numberOfPages = (int) (lines.size() / numberOfLinePerPage) + 1;
    this.scan = scan;
    this.fillPages();
    this.startPageNavigation();
  }

  ArrayList<ArrayList<String>> pages;

  /**
   * Uses the lines to create all the pages using the parameter number of line per page.
   */
  private void fillPages() {
    this.pages = new ArrayList<ArrayList<String>>();
    int lastLine = lines.size();
    int currentLine = 0;
    for (int i = 0; i < this.numberOfPages; i++) {
      this.pages.add(new ArrayList<String>());
      int lineLimit = numberOfLinePerPage;
      if (i == this.numberOfPages - 1) {
        lineLimit = lastLine - currentLine;
      }
      for (int j = 0; j < lineLimit; j++) {
        currentLine++;
        this.pages.get(i).add(this.lines.get(i * numberOfLinePerPage + j));

      }
    }
  }

  /**
   * Return the page with the number pageNumber.
   * @param pageNumber the number of the page you want to access
   * @return the list of lines
   */
  private ArrayList<String> getPage(int pageNumber) {
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
  private ArrayList<String> getNextPage() {
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
  private ArrayList<String> getPreviousPage() {
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
  private void startPageNavigation() {
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
  private void printPage(ArrayList<String> linesForPage) {
    if (linesForPage != null) {
      for (String line: linesForPage) {
        System.out.println(line);
      }
    }
  }

}
