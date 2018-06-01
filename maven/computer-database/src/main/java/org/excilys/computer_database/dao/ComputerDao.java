package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Computer;

public interface ComputerDao {
  /**
   * @return List of computers
   */
  ArrayList<Computer> getComputers();
  /**
   * Return the list of computers for the defined page.
   * @param numberOfItemPerPage Number of items per page.
   * @param page Page number
   * @param keyword Keyword you are looking for
   * @return the list of computers for the defined page.
   */
  ArrayList<Computer> getComputersWithPageAndSearch(int numberOfItemPerPage, int page, String keyword);
  /**
   * @param id Id of the computer
   * @return Computer with id
   */
  Computer getComputerDetails(int id);
  /**
   * Count the number of computers.
   * @param keyword Keyword you are searching.
   * @return the number of computers
   */
  int countComputers(String keyword);
  /**
   * Count the number of computers.
   * @return the number of computers
   */
  int countComputers();
  /**
   * @param computer Computer to create
   */
  void createComputer(Computer computer);
  /**
   * @param computer Computer to update
   */
  void updateComputer(Computer computer);
  /**
   * @param id id of the computer to delete
   */
  void deleteComputer(int id);
}
