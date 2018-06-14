package org.excilys.computer_database.mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.excilys.computer_database.dto.ComputerDto;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.util.Util;
import org.springframework.jdbc.core.RowMapper;

public class ComputerMapper implements RowMapper<Computer> {

  /**
   * @param resultSet the resulSet received from JDBCTemplate
   * @return the computer associated
   * @throws SQLException if there was an error when accessing the resulSet
   */
  @Override
  public Computer mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
    int id = resultSet.getInt("computer.id");
    String name = resultSet.getString("computer.name");
    Date introduced = resultSet.getDate("computer.introduced");
    Date discontinued = resultSet.getDate("computer.discontinued");
    int companyId = resultSet.getInt("company.id");
    String companyName = resultSet.getString("company.name");
    return new Computer(id, name, introduced, discontinued, companyId, companyName);
  }
  
  /**
   * ComputerDto to Computer.
   * @param computerDto the computerDto you want a computer from
   */
  public Computer mapComputer(ComputerDto computerDto) {
    return new Computer(computerDto.getId(),
        computerDto.getName(),
        Util.getDateFromString(computerDto.getIntroduced()),
        Util.getDateFromString(computerDto.getDiscontinued()),
        computerDto.getCompanyId(),
        computerDto.getCompanyName());
  }
}
