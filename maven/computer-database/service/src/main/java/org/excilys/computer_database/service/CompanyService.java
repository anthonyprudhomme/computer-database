package org.excilys.computer_database.service;

import java.util.ArrayList;
import java.util.Optional;

import org.excilys.computer_database.dao.CompanyDao;
import org.excilys.computer_database.model.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CompanyService {

  @Autowired
  private CompanyDao companyDao;

  public ArrayList<Company> getCompanies() {
    return companyDao.getCompanies();
  }

  /**
   * Returns a company with the id.
   * @param id of the company
   * @return the company
   */
  public Optional<Company> getCompany(int id) {
    return companyDao.getCompany(id);
  }

  /**
   * Return the number of companies.
   * @return the number of companies
   */
  public int countCompanies() {
    return companyDao.countCompanies();
  }

  /**
   * Returns the list of companies at the specific page.
   * @param numberOfItemPerPage The number of items per pages.
   * @param page The number of the page.
   * @return the list of companies at the specific page.
   */
  public ArrayList<Company> getCompaniesAtPage(int numberOfItemPerPage, int page) {
    return companyDao.getCompaniesAtPage(numberOfItemPerPage, page);
  }
  
  /**
   * Delete the company matching the given id and its related computers.
   * @param idToDelete The id of the company to delete
   */
  public void deleteCompany(int idToDelete) {
    companyDao.deleteCompany(idToDelete);
  }
  
  /**
   * Create the company matching the given id.
   * @param company the company to create
   */
  public void createCompany(Company company) {
    companyDao.createCompany(company);
  }
  
  /**
   * Update the company matching the given id.
   * @param company the company to update
   */
  public void updateCompany(Company company) {
    companyDao.updateCompany(company);
  }

  public ArrayList<Company> getCompaniesWithParams(Integer currentPage, Integer numberOfItemPerPage, String search,
      String orderBy, String ascOrDesc) {
    return companyDao.getCompaniesWithParams(currentPage, numberOfItemPerPage, search, orderBy, ascOrDesc);
  }

}
