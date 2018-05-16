package org.excilys.computer_database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.persistence.JdbcRequest;
import org.excilys.computer_database.persistence.RequestName;

public class ComputerDaoImpl implements ComputerDao{

	JdbcRequest jdbcRequest = new JdbcRequest();

	@Override
	public ArrayList<Computer> getComputers() {
		ArrayList<Computer> computers = new ArrayList<Computer>();
		String query = "SELECT * FROM computer";
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.LIST_COMPUTERS);
		try {
			while (resultSet.next()) {
				computers.add(new Computer(resultSet));
			}
			resultSet.close();
			JdbcConnection.getConnection().close();
		} catch (SQLException exception) {
			jdbcRequest.handleException(resultSet, exception);
		}
		return computers;
	}

	@Override
	public Computer getComputerDetails(int id) {
		Computer computer = null;
		String query = "SELECT * FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id="+id;
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.COMPUTER_DETAILS);
		try {
			while (resultSet.next()) {
				computer = new Computer(resultSet);
			}
			resultSet.close();
			JdbcConnection.getConnection().close();
		} catch (SQLException exception) {
			jdbcRequest.handleException(resultSet, exception);
		}
		return computer;
	}

	@Override
	public void createComputer(Computer computer) {
		String query = "INSERT INTO computer (name, introduced, discontinued, company_id) "
				+ "VALUES(?,?,?,?)";
		jdbcRequest.doARequest(query, RequestName.CREATE_COMPUTER, computer);

	}

	@Override
	public void updateComputer(Computer computer) {
		String updateParams = "";

		if(computer.getName() != null){
			updateParams+= " name=\""+computer.getName()+ "\",";	
		}
		if(computer.getIntroduced() != null){
			updateParams+= " introduced=\""+computer.getIntroduced().toString()+ "\",";	
		}
		if(computer.getDiscontinued() != null){
			updateParams+= " discontinued=\""+computer.getDiscontinued().toString()+ "\",";	
		}
		if(computer.getCompanyId() != -1){
			updateParams+= " company_id="+computer.getCompanyId()+ ",";	
		}
		// Removes the last "," to have valid params
		if (updateParams != null && updateParams.length() > 0 && updateParams.charAt(updateParams.length() - 1) == ',') {
			updateParams = updateParams.substring(0, updateParams.length() - 1);
		}
		String query = "UPDATE computer SET"+updateParams+" WHERE id="+computer.getId();
		jdbcRequest.doARequest(query, RequestName.UPDATE_COMPUTER, computer);

	}

	@Override
	public void deleteComputer(int id) {
		String query = "DELETE FROM computer WHERE id="+id;
		jdbcRequest.doARequest(query, RequestName.DELETE_COMPUTER);
	}

	public boolean checkIdInComputerTable(int id) throws SQLException {
		String query = "SELECT * FROM computer WHERE id="+id;
		ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.CHECK_ID_COMPUTER);		
		boolean isAvailable = resultSet.isBeforeFirst();
		resultSet.close();
		JdbcConnection.getConnection().close();
		return isAvailable;
	}



}
