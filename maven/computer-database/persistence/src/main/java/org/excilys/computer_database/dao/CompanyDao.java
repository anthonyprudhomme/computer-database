package org.excilys.computer_database.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.model.Company;
import org.springframework.stereotype.Repository;


@Repository
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
  Optional<Company> getCompany(int id);
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
  
  /**
   * Get the list of companies with the given params.
   * @param ascOrDesc Whether it is asc or desc
   * @param orderBy which param to order by with
   * @param search keyword you are looking for
   * @param numberOfItemPerPage number of items you want to display per page
   * @param currentPage the current page number
   * @param id of the company to delete
   */
  ArrayList<Company> getCompaniesWithParams(Integer currentPage, Integer numberOfItemPerPage, String search, String orderBy, String ascOrDesc);
  
  /**
   * Create a company.
   * @param company to create
   */
  void createCompany(Company company);
  
  /**
   * Update a company.
   * @param company to update
   */
  void updateCompany(Company company);
}
