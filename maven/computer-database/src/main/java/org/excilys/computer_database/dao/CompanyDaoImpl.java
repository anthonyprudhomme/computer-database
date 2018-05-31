package org.excilys.computer_database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.mapper.CompanyMapper;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.persistence.JdbcRequest;
import org.excilys.computer_database.persistence.RequestName;

/**
 * @author anthony
 *
 */
public class CompanyDaoImpl implements CompanyDao {
  private static final String QUERY_GET_ALL_COMPANIES = "SELECT company.id, company.name FROM company";
  private static final String QUERY_GET_COMPANY = "SELECT company.id, company.name FROM company WHERE id=";
  private static final String QUERY_COUNT_COMPANIES = "SELECT COUNT(company.id) AS total FROM company";
  private static final String QUERY_GET_COMPANIES_AT_PAGE = "SELECT company.id, company.name FROM company LIMIT ? OFFSET ?";
  private JdbcRequest jdbcRequest;
  private static CompanyDaoImpl instance = null;

  /**
   * Singleton.
   */
  private CompanyDaoImpl() {
    jdbcRequest = new JdbcRequest();
  }

  /**
   * @return CompanyDaoImpl
   */
  public static CompanyDaoImpl getInstance() {
    if (instance == null) {
      instance = new CompanyDaoImpl();
    }
    return instance;
  }

  @Override
  public ArrayList<Company> getCompanies() {
    String query = QUERY_GET_ALL_COMPANIES;
    ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.LIST_COMPANIES);
    return getListOfCompaniesFromResultSet(resultSet);
  }

  @Override
  public Company getCompany(int id) {
    Company company = null;
    String query = QUERY_GET_COMPANY + id;
    ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.COMPANY);
    try {
      while (resultSet.next()) {
        company = CompanyMapper.getInstance().mapCompany(resultSet);
      }
      resultSet.close();
      JdbcConnection.getConnection().close();
    } catch (SQLException exception) {
      jdbcRequest.handleException(resultSet, exception);
    }
    return company;
  }

  @Override
  public int countCompanies() {
    String query = QUERY_COUNT_COMPANIES;
    return jdbcRequest.countRequest(query, RequestName.COUNT_COMPANIES);
  }

  @Override
  public ArrayList<Company> getCompaniesAtPage(int numberOfItemPerPage, int page) {
    String query = QUERY_GET_COMPANIES_AT_PAGE;
    ResultSet resultSet = jdbcRequest.itemsAtPageRequest(numberOfItemPerPage, page, query, RequestName.LIST_COMPANIES_AT_PAGE);
    return getListOfCompaniesFromResultSet(resultSet);
  }

  /**
   * Return the list of companies from the resultSet object.
   * @param resultSet ResultSet object from the request.
   * @return the list of companies from the resultSet object.
   */
  private ArrayList<Company> getListOfCompaniesFromResultSet(ResultSet resultSet) {
    ArrayList<Company> companies = new ArrayList<Company>();
    try {
      while (resultSet.next()) {
        companies.add(CompanyMapper.getInstance().mapCompany(resultSet));
      }
      resultSet.close();
      JdbcConnection.getConnection().close();
    } catch (SQLException exception) {
      jdbcRequest.handleException(resultSet, exception);
    }
    return companies;
  }

}
