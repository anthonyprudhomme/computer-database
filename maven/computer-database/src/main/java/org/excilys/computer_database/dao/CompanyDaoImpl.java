package org.excilys.computer_database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.persistence.JdbcRequest;
import org.excilys.computer_database.persistence.RequestName;

public class CompanyDaoImpl implements CompanyDao{
	
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
		String query = "SELECT * FROM company";
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
	public boolean checkIdInCompanyTable(int id) throws SQLException {
		String query = "SELECT * FROM company WHERE id="+id;
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.CHECK_ID_COMPANY);	
		boolean isAvailable = resultSet.isBeforeFirst();
		resultSet.close();
		JdbcConnection.getConnection().close();
		return isAvailable;
	}

}
