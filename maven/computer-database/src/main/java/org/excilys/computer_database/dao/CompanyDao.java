package org.excilys.computer_database.dao;

import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.model.Company;


public interface CompanyDao {
	public ArrayList<Company> getCompanies();
	public boolean checkIdInCompanyTable(int id) throws SQLException;
}
