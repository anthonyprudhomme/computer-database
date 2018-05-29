package org.excilys.computer_database.service;

import java.util.ArrayList;

import org.excilys.computer_database.dao.ComputerDao;
import org.excilys.computer_database.dao.ComputerDaoImpl;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.validation.ComputerValidation;
import org.excilys.computer_database.validation.ComputerValidationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

public class ComputerService {

  private static ComputerService instance = null;
  private ComputerDao computerDao = ComputerDaoImpl.getInstance();
  /**
   * ComputerService singleton.
   */
  private ComputerService() { }

  private final Logger LOGGER = LoggerFactory.getLogger(ComputerService.class);

  /**
   * Returns an instance of the ComputerService singleton.
   * @return an instance of the ComputerService singleton
   */
  public static ComputerService getInstance() {
    if (instance == null) {
      instance = new ComputerService();
    }
    return instance;
  }

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
   * @return the status of the validation and an associated message
   */
  public Pair<ComputerValidationStatus, String> createComputer(Computer computer) {
    ComputerValidationStatus status = null;
    String message = "";
    if (ComputerValidation.getInstance().validateDate(computer)) {
      if (ComputerValidation.getInstance().validateCompanyId(computer.getCompany().getId())) {
        computerDao.createComputer(computer);
        status = ComputerValidationStatus.OK;
        message = "Computer successfully created.";
        LOGGER.info("Computer created: " + computer.toString());
      } else {
        status = ComputerValidationStatus.COMPANY_ID_ERROR;
        message = "The computer couldn't be created, the id of the company doesn't belong to any company in the database.";
        LOGGER.error("Company id error when trying to update computer: " + computer.toString());
      }
    } else {
      status = ComputerValidationStatus.DATE_ERROR;
      message = "The computer couldn't be created, the discontinued date must be later than the introduced date.";
      LOGGER.error("Date error when trying to create computer: " + computer.toString());
    }
    return new Pair<ComputerValidationStatus, String>(status, message);
  }

  /**
   * Check if the is a problem with the computer given as parameter then updates a computer.
   * @param computer computer to check and update
   * @return the status of the validation and an associated message
   */
  public Pair<ComputerValidationStatus, String> updateComputer(Computer computer) {
    ComputerValidationStatus status = null;
    String message = "";
    if (ComputerValidation.getInstance().validateDate(computer)) {
      if (ComputerValidation.getInstance().validateCompanyId(computer.getCompany().getId())) {
        computerDao.updateComputer(computer);
        status = ComputerValidationStatus.OK;
        message = "Computer successfully updated.";
        LOGGER.info("Computer updated: " + computer.toString());
      } else {
        status = ComputerValidationStatus.COMPANY_ID_ERROR;
        message = "The computer couldn't be updated, the id of the company doesn't belong to any company in the database.";
        LOGGER.error("Company id error when trying to update computer: " + computer.toString());
      }
    } else {
      status = ComputerValidationStatus.DATE_ERROR;
      message = "The computer couldn't be updated, the discontinued date must be later than the introduced date.";
      LOGGER.error("Date error when trying to update computer: " + computer.toString());
    }
    return new Pair<ComputerValidationStatus, String>(status, message);
  }

  /**
   * Delete a computer with the given id.
   * @param id id of the computer you want to delete
   */
  public void deleteComputer(int id) {
    computerDao.deleteComputer(id);
  }

}
