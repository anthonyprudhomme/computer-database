package org.excilys.computer_database.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Company {
	
	private int id;
	private String name;
	
	public Company(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public Company(ResultSet resultSet) throws SQLException {
		this.id = resultSet.getInt("company.id");
		this.name =  resultSet.getString("company.name");
	}

	public int getId(){
		return this.id;
	}
	
	public String getName(){
		return this.name;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + "]";
	}
	
	

}
