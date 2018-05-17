package org.excilys.computer_database.dao;

import java.util.ArrayList;

import org.excilys.computer_database.model.Company;


public interface CompanyDao {
	public ArrayList<Company> getCompanies();
	public Company getCompany(int id);
}
