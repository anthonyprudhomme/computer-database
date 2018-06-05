package org.excilys.computer_database.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;

import org.excilys.computer_database.dao.ComputerDao;
import org.excilys.computer_database.dao.OrderByParams;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.validation.ComputerValidation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComputerService {

  @Autowired
  private ComputerDao computerDao;

  @Autowired
  private ComputerValidation computerValidation;

  public ArrayList<Computer> getComputers() {
    return computerDao.getComputers();
  }

  /**
   * Returns details about a computer.
   * @param id Id of the computer
   * @return the computer with the id you gave.
   */
  public Computer getComputerDetails(int id) {
    return computerDao.getComputerDetails(id);
  }

  /**
   * Check if the is a problem with the computer given as parameter then creates a computer.
   * @param computer computer to check and create
   * @exception CDBObjectException Thrown if there was an error when validating the computer
   */
  public void createComputer(Computer computer) throws CDBObjectException {
    updateDates(computer);
    computerValidation.validate(computer);
    computerDao.createComputer(computer);
  }

  /**
   * Check if the is a problem with the computer given as parameter then updates a computer.
   * @param computer computer to check and update
   * @exception CDBObjectException Thrown if there is an error with the update
   */
  public void updateComputer(Computer computer) throws CDBObjectException {
    updateDates(computer);
    computerValidation.validate(computer);
    computerDao.updateComputer(computer);
  }
  /**
   * Update the date to avoid day difference.
   * @param computer to update
   */
  private void updateDates(Computer computer) {
    if (computer.getIntroduced() != null) {
      computer.setIntroduced(updateDate(computer.getIntroduced()));
    }
    if (computer.getDiscontinued() != null) {
      computer.setDiscontinued(updateDate(computer.getDiscontinued()));
    }
  }

  /**
   * Update the date to avoid day difference.
   * @param date to update
   * @return the date changed to avoid day difference
   */
  private Date updateDate(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_YEAR, 1);
    return new Date(calendar.getTimeInMillis());
  }

  /**
   * Delete a computer with the given id.
   * @param id id of the computer you want to delete
   */
  public void deleteComputer(int id) {
    computerDao.deleteComputer(id);
  }

  /**
   * Count the number of computers.
   * @return the number of computers
   */
  public int countComputers() {
    return computerDao.countComputers();
  }

  /**
   * Count the number of computers.
   * @param keyword The keyword you are looking for.
   * @return the number of computers
   */
  public int countComputers(String keyword) {
    return computerDao.countComputers(keyword);
  }

  /**
   * Returns the list of computers at the specific page.
   * @param numberOfItemPerPage The number of items per pages.
   * @param currentPage The number of the page.
   * @param keyword The keyword you are looking for
   * @param orderByParams Params to order by with
   * @return the list of computers at the specific page.
   */
  public ArrayList<Computer> getComputersWithParams(int numberOfItemPerPage, int currentPage, String keyword,
      OrderByParams orderByParams) {
    return computerDao.getComputersWithParams(numberOfItemPerPage, currentPage, keyword, orderByParams);
  }

}
