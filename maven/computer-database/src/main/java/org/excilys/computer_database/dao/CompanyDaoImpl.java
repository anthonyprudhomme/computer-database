package org.excilys.computer_database.dao;

import java.util.ArrayList;

import javax.sql.DataSource;

import org.excilys.computer_database.mapper.CompanyMapper;
import org.excilys.computer_database.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyDaoImpl implements CompanyDao {
  private static final String QUERY_GET_ALL_COMPANIES = "SELECT company.id, company.name FROM company";
  private static final String QUERY_GET_COMPANY = "SELECT company.id, company.name FROM company WHERE id= ?";
  private static final String QUERY_COUNT_COMPANIES = "SELECT COUNT(company.id) AS total FROM company";
  private static final String QUERY_GET_COMPANIES_AT_PAGE = "SELECT company.id, company.name FROM company LIMIT ? OFFSET ?";
  private static final String QUERY_DELETE_COMPANY = "DELETE FROM company WHERE id= ?";
  private static final String QUERY_DELETE_COMPUTER_WITH_COMPANY_ID = "DELETE FROM computer WHERE computer.company_id= ?";

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public ArrayList<Company> getCompanies() {
    return (ArrayList<Company>) jdbcTemplate.query(QUERY_GET_ALL_COMPANIES, new CompanyMapper());
  }

  @Override
  public Company getCompany(int id) {
    return (Company) jdbcTemplate.queryForObject(QUERY_GET_COMPANY, new Object[]{id}, new CompanyMapper());
  }

  @Override
  public int countCompanies() {
    return jdbcTemplate.queryForObject(QUERY_COUNT_COMPANIES, Integer.class);
  }

  @Override
  public ArrayList<Company> getCompaniesAtPage(int numberOfItemPerPage, int page) {
    int offset = numberOfItemPerPage * (page - 1);
    return (ArrayList<Company>) jdbcTemplate.query(QUERY_GET_COMPANIES_AT_PAGE, new Object[]{numberOfItemPerPage, offset}, new CompanyMapper());
  }

  @Override
  @Transactional
  public void deleteCompany(int companyId) {
    jdbcTemplate.update(QUERY_DELETE_COMPANY, new Object[]{companyId});
    jdbcTemplate.update(QUERY_DELETE_COMPUTER_WITH_COMPANY_ID, new Object[]{companyId});
  }

}
