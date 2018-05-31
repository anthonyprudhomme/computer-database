package org.excilys.computer_database.exceptions;

public class CDBObjectException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Exception that occur when there was an error when accessing the database.
   * @param message To display.
   */
  public CDBObjectException(String message) {
    super(message);
  }
}
