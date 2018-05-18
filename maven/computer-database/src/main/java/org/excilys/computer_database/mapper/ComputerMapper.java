package org.excilys.computer_database.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.excilys.computer_database.model.Computer;

public class ComputerMapper {

  private static ComputerMapper instance = null;

  /**
   * Singleton.
   */
  private ComputerMapper() { }

  /**
   * @return the computer associated
   */
  public static ComputerMapper getInstance() {
    if (instance == null) {
      instance = new ComputerMapper();
    }
    return instance;
  }

  /**
   * @param resultSet the resulSet received from the JDBCRequest
   * @return the computer associated
   * @throws SQLException if there was an error when accessing the resulSet
   */
  public Computer mapComputer(ResultSet resultSet) throws SQLException {

    int id = resultSet.getInt("computer.id");
    String name = resultSet.getString("computer.name");
    Date introduced = resultSet.getDate("computer.introduced");
    Date discontinued = resultSet.getDate("computer.discontinued");
    int companyId = resultSet.getInt("company.id");
    String companyName = resultSet.getString("company.name");
    return new Computer(id, name, introduced, discontinued, companyId, companyName);
  }
}
