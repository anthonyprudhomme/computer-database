package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Computer;

public interface ComputerDao {
  /**
   * @return List of computers
   */
  ArrayList<Computer> getComputers();
  /**
   * @param id Id of the computer
   * @return Computer with id
   */
  Computer getComputerDetails(int id);
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
