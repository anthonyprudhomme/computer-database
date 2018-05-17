package org.excilys.computer_database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.persistence.JdbcRequest;
import org.excilys.computer_database.persistence.RequestName;

public class CompanyDaoImpl implements CompanyDao{
	
	private static final String QUERY_GET_ALL_COMPANIES = "SELECT company.id, company.name FROM company";
	private static final String QUERY_GET_COMPANY = "SELECT company.id, company.name FROM company WHERE id=";
	
	private JdbcRequest jdbcRequest;
	
	private static CompanyDaoImpl instance = null;
	
	private CompanyDaoImpl(){
		jdbcRequest = new JdbcRequest();
	}
	
	public static CompanyDaoImpl getInstance(){
		if(instance == null){
			instance = new CompanyDaoImpl();
		}
		return instance;
	}
	
	@Override
	public ArrayList<Company> getCompanies() {
		ArrayList<Company> companies = new ArrayList<Company>();
		String query = QUERY_GET_ALL_COMPANIES;
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.LIST_COMPANIES);
		try {
			while (resultSet.next()) {
				companies.add(new Company(resultSet));
			}
			resultSet.close();
			JdbcConnection.getConnection().close();
		} catch (SQLException exception) {
			jdbcRequest.handleException(resultSet, exception);
		}
		return companies;
	}

	@Override
	public Company getCompany(int id) {
		Company company = null;
		String query = QUERY_GET_COMPANY+id;
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.COMPANY);
		try {
			while (resultSet.next()) {
				company = new Company(resultSet);
			}
			resultSet.close();
			JdbcConnection.getConnection().close();
		} catch (SQLException exception) {
			jdbcRequest.handleException(resultSet, exception);
		}
		return company;
	}

}
