package org.excilys.computer_database.exceptions;

public class RegistrationFailedException extends Exception {

  private static final long serialVersionUID = 1L;

  /**
   * Exception that occur when there was an error when creating a new account.
   * @param message To display.
   */
  public RegistrationFailedException(String message) {
    super(message);
  }
}
