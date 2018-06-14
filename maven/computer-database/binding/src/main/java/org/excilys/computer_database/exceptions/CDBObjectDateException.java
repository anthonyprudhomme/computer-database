package org.excilys.computer_database.exceptions;

public class CDBObjectDateException extends CDBObjectException {

  private static final long serialVersionUID = 1L;

  /**
   * Exception that occur when there was an error when accessing the database due to a date error.
   * @param message To display.
   */
  public CDBObjectDateException(String message) {
    super(message);
  }

}
