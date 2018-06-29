package org.excilys.computer_database.validator;

import org.excilys.computer_database.exceptions.CDBObjectCompanyIdException;
import org.excilys.computer_database.exceptions.CDBObjectDateException;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository("comuterValidation")
public class ComputerValidator {

  @Autowired
  private CompanyService companyService;

  /**
   * Tells if the company id links to a a company in the database.
   * @param id if of the company
   * @throws CDBObjectCompanyIdException Thrown when the company id is invalid
   */
  private void validateCompanyId(Integer id) throws CDBObjectCompanyIdException {
    if (id != null && id != -1) {
      if (!companyService.getCompany(id).isPresent()) {
        throw new CDBObjectCompanyIdException("Wrong company id");
      }
    }
  }

  /**
   * Tells if the computer introduced and discontinued dates are valid.
   * @param computer to check
   * @throws CDBObjectDateException Thrown when the dates are invalid
   */
  private void validateDate(Computer computer) throws CDBObjectDateException {
    if (computer.getIntroduced() != null && computer.getDiscontinued() != null) {
      if (!computer.getIntroduced().before(computer.getDiscontinued())) {
        throw new CDBObjectDateException("Introduced date should be earlier than the discontinued date.");
      }
    }
  }

  /**
   * Check the computer is valid.
   * @param computer The computer to validate
   * @throws CDBObjectException Thrown if there is an error during the validation
   */
  public void validate(Computer computer) throws CDBObjectException {
    validateDate(computer);
    if (computer.getCompany() != null) {
      validateCompanyId(computer.getCompany().getId());
    }
  }

}