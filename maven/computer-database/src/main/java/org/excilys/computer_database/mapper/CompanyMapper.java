package org.excilys.computer_database.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.excilys.computer_database.model.Company;

public class CompanyMapper {

  private static CompanyMapper instance = null;

  /**
   * Singleton.
   */
  private CompanyMapper() { }

  /**
   * @return singleton
   */
  public static CompanyMapper getInstance() {
    if (instance == null) {
      instance = new CompanyMapper();
    }
    return instance;
  }

  /**
   * @param resultSet the resulSet received from the JDBCRequest
   * @return the company associated
   * @throws SQLException if there was an error when accessing the resulSet
   */
  public Company mapCompany(ResultSet resultSet) throws SQLException {
    int id = resultSet.getInt("company.id");
    String name = resultSet.getString("company.name");
    return new Company(id, name);
  }

}
