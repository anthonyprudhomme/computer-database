package org.excilys.computer_database.validation;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;

public class ComputerValidation {

  private static ComputerValidation instance = null;

  /**
   * Singleton for ComputerValidation.
   */
  private ComputerValidation() { }

  /**
   * Returns an instance of the singleton for ComputerValidation.
   * @return an instance of the singleton for ComputerValidation
   */
  public static ComputerValidation getInstance() {
    if (instance == null) {
      instance = new ComputerValidation();
    }
    return instance;
  }

  /**
   * Tells if the company id links to a a company in the database.
   * @param id if of the company
   * @return whether the company exists or not
   */
  public boolean validateCompanyId(int id) {
    if (id != -1) {
      return CompanyService.getInstance().getCompany(id) != null;
    } else {
      return true;
    }
  }

  /**
   * Tells if the computer introduced and discontinued dates are valid.
   * @param computer to check
   * @return whether the computer dates are valid or not.
   */
  public boolean validateDate(Computer computer) {
    if (computer.getIntroduced() == null) {
      return true;
    } else {
      if (computer.getDiscontinued() == null) {
        return true;
      } else {
        return computer.getIntroduced().before(computer.getDiscontinued());
      }
    }
  }

}
