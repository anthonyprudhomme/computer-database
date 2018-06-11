package org.excilys.computer_database.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.model.Computer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository("computerDao")
@Transactional
public interface ComputerDao {
  /**
   * @return List of computers
   */
  ArrayList<Computer> getComputers();
  /**
   * Returns the list of computers at the specific page.
   * @param numberOfItemPerPage The number of items per pages.
   * @param currentPage The number of the page.
   * @param keyword The keyword you are looking for
   * @param orderByParams Params to order by with
   * @return the list of computers at the specific page.
   */
  ArrayList<Computer> getComputersWithParams(int numberOfItemPerPage, int currentPage, String keyword,
      OrderByParams orderByParams);
  /**
   * @param id Id of the computer
   * @return Computer with id
   */

  Optional<Computer> getComputerDetails(int id);
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
  /**
   * @param ids ids of the computers to delete
   */
  void deleteComputers(int[] ids);
}
