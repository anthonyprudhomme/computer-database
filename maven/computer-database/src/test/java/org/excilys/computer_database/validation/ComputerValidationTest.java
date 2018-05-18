package org.excilys.computer_database.validation;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Date;

import org.excilys.computer_database.model.Computer;
import org.junit.Test;

public class ComputerValidationTest {

  /**
   * Test the date and see if an earlier discontinued date returns false.
   */
  @Test
  public void validationDateOfDiscontinuedDateEarlierThanIntroducedDateShouldReturnFalse() {
    Computer computer = new Computer(-1, null, Date.valueOf("1999-10-10"), Date.valueOf("1998-10-10"), -1, null);
    assertFalse(ComputerValidation.getInstance().validateDate(computer));
  }
  /**
   * Test the date and see if a later discontinued date returns true.
   */
  @Test
  public void validationDateOfDiscontinuedDateLaterThanIntroducedDateShouldReturnTrue() {
    Computer computer = new Computer(-1, null, Date.valueOf("1998-10-10"), Date.valueOf("1999-10-10"), -1, null);
    assertTrue(ComputerValidation.getInstance().validateDate(computer));
  }

}
