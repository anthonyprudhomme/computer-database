package org.excilys.computer_database.exceptions;

public class CDBObjectCompanyIdException extends CDBObjectException {

  private static final long serialVersionUID = 1L;

  /**
   * Exception that occur when there was an error when accessing the database due to a company id error.
   * @param message To display.
   */
  public CDBObjectCompanyIdException(String message) {
    super(message);
  }
}
