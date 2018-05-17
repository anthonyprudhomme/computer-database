package org.excilys.computer_database.service;

import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.dao.CompanyDaoImpl;
import org.excilys.computer_database.model.Company;

public class CompanyService {
	
	private static CompanyService instance = null;
	
	private CompanyService(){}
	
	public static CompanyService getInstance(){
		if(instance == null){
			instance = new CompanyService();
		}
		return instance;
	}
	
	public ArrayList<Company> getCompanies() {
		return CompanyDaoImpl.getInstance().getCompanies();
	}
	
	public boolean checkIdInCompanyTable(int id) throws SQLException {
		return CompanyDaoImpl.getInstance().checkIdInCompanyTable(id);
	}

}
