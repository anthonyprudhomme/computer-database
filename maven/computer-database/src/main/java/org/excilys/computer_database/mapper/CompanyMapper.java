package org.excilys.computer_database.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.excilys.computer_database.model.Company;
import org.springframework.jdbc.core.RowMapper;

public class CompanyMapper implements RowMapper<Company> {
  /**
   * @param resultSet the resulSet given by JDBCTemplate
   * @return the company associated
   * @throws SQLException if there was an error when accessing the resulSet
   */
  @Override
  public Company mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
    int id = resultSet.getInt("company.id");
    String name = resultSet.getString("company.name");
    return new Company(id, name);
  }

}
