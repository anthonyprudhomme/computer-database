package org.excilys.computer_database.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.excilys.computer_database.model.Company;

public class CompanyMapper {
	
	private static CompanyMapper instance = null;

	private CompanyMapper(){}

	public static CompanyMapper getInstance(){
		if(instance == null){
			instance = new CompanyMapper();
		}
		return instance;
	}

	public Company mapCompany(ResultSet resultSet) throws SQLException{
		int id = resultSet.getInt("company.id");
		String name = resultSet.getString("company.name");
		return new Company(id, name);
	}

}
