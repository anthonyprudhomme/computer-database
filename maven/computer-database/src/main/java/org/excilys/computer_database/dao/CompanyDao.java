package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Company;

/**
 * @author anthony.
 *
 */


public interface CompanyDao {
  /**
   * @return list of companies
   */
  ArrayList<Company> getCompanies();
  /**
   * @param id of the company
   * @return list of companies
   */
  Company getCompany(int id);
}
