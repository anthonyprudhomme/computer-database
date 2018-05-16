package org.excilys.computer_database.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.dao.CompanyDaoImpl;
import org.excilys.computer_database.model.Company;

public class CompanyService {
	
	CompanyDaoImpl companyDaoImpl = new CompanyDaoImpl();
	
	public ArrayList<Company> getCompanies() {
		return companyDaoImpl.getCompanies();
	}
	
	public boolean checkIdInCompanyTable(int id) throws SQLException {
		return companyDaoImpl.checkIdInCompanyTable(id);
	}

}
