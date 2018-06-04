package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Company;
import org.springframework.stereotype.Repository;


@Repository("companyDao")
public interface CompanyDao {
  /**
   * @return list of companies
   */
  ArrayList<Company> getCompanies();
  /**
   * Return the list of computers for the defined page.
   * @param numberOfItemPerPage Number of items per page.
   * @param page Page number
   * @return the list of computers for the defined page.
   */
  ArrayList<Company> getCompaniesAtPage(int numberOfItemPerPage, int page);
  /**
   * @param id of the company
   * @return list of companies
   */
  Company getCompany(int id);
  /**
   * Return the number of companies.
   * @return the number of companies
   */
  int countCompanies();
  /**
   * Delete the company and all associated computers with the corresponding company id.
   * @param id of the company to delete
   */
  void deleteCompany(int id);
}
