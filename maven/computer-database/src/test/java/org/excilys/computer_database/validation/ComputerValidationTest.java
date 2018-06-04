package org.excilys.computer_database.validation;

import java.sql.Date;

import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Computer;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class ComputerValidationTest {

  @Autowired
  ComputerValidation computerValidation;

  /**
   * Test the date and see if a later discontinued date returns true.
   * @throws CDBObjectException If the validation failed
   */
  @Test(expected = CDBObjectException.class)
  public void testWrongDate() throws CDBObjectException {
    Computer computer = new Computer(-1, null, Date.valueOf("1999-10-10"), Date.valueOf("1998-10-10"), -1, null);
    computerValidation.validate(computer);
  }

  /**
   * Test the date and see if a later discontinued date returns true.
   * @throws CDBObjectException If the validation failed
   */
  @Test
  public void testGoodDate() throws CDBObjectException {
    Computer computer = new Computer(-1, null, Date.valueOf("1998-10-10"), Date.valueOf("1999-10-10"), -1, null);
    computerValidation.validate(computer);
  }

}
