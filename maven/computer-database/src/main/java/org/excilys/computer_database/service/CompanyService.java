package org.excilys.computer_database.service;

import java.util.ArrayList;

import org.excilys.computer_database.dao.CompanyDao;
import org.excilys.computer_database.dao.CompanyDaoImpl;
import org.excilys.computer_database.model.Company;

public class CompanyService {
	
	private static CompanyService instance = null;
	private CompanyDao companyDao = CompanyDaoImpl.getInstance();
	private CompanyService(){}
	
	public static CompanyService getInstance(){
		if(instance == null){
			instance = new CompanyService();
		}
		return instance;
	}
	
	public ArrayList<Company> getCompanies() {
		return companyDao.getCompanies();
	}
	
	public Company getCompany(int id) {
		return companyDao.getCompany(id);
	}

}
