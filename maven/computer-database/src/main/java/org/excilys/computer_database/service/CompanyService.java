package org.excilys.computer_database.service;

import java.util.ArrayList;

import org.excilys.computer_database.dao.CompanyDao;
import org.excilys.computer_database.dao.CompanyDaoImpl;
import org.excilys.computer_database.model.Company;

public class CompanyService {

  private static CompanyService instance = null;
  private CompanyDao companyDao = CompanyDaoImpl.getInstance();
  /**
   * Singleton of CompanyService.
   */
  private CompanyService() { }

  /**
   * Return an instance of CompanyService.
   * @return an instance of CompanyService
   */
  public static CompanyService getInstance() {
    if (instance == null) {
      instance = new CompanyService();
    }
    return instance;
  }

  public ArrayList<Company> getCompanies() {
    return companyDao.getCompanies();
  }

  /**
   * Returns a company with the id.
   * @param id of the company
   * @return the company
   */
  public Company getCompany(int id) {
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

}
