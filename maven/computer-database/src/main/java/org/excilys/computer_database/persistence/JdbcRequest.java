package org.excilys.computer_database.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.excilys.computer_database.model.*;

public class JdbcRequest {
	
	final private Logger logger = LoggerFactory.getLogger(JdbcRequest.class);
	private String loggerDatabasePrefix = "Request to SQL database: ";
	
	public ResultSet doARequest(String query, RequestName requestName, Computer computer){
		logger.info(loggerDatabasePrefix + query);
		ResultSet resultSet = null;
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
			preparedStatement = this.prepareRequest(preparedStatement, requestName, computer);
			if (preparedStatement.execute()) {
				return preparedStatement.getResultSet();
			}
		}
		catch (SQLException sqlException){
			logger.error("SQLException: " + sqlException.getMessage());
			logger.error("SQLState: " + sqlException.getSQLState());
			logger.error("VendorError: " + sqlException.getErrorCode());
		}
		return resultSet;
	}

	public ResultSet doARequest(String query, RequestName requestName){
		return this.doARequest(query, requestName, null);
	}
	
	private PreparedStatement prepareRequest(PreparedStatement preparedStatement, RequestName requestName, Computer computer) throws SQLException{
		switch(requestName){

		case COMPUTER_DETAILS:
		case LIST_COMPANIES:
		case LIST_COMPUTERS:
		case DELETE_COMPUTER:
			break;
		case UPDATE_COMPUTER:
			preparedStatement.setString(1, computer.getName());		
			preparedStatement.setDate(2, computer.getIntroduced());
			preparedStatement.setDate(3, computer.getDiscontinued());
			if(computer.getCompany().getId() == -1){
				preparedStatement.setNull(4, Types.INTEGER);
			}else{
				preparedStatement.setInt(4, computer.getCompany().getId());
			}
			preparedStatement.setInt(5, computer.getId());
			break;
			
		case CREATE_COMPUTER:
			preparedStatement.setString(1, computer.getName());		
			preparedStatement.setDate(2, computer.getIntroduced());
			preparedStatement.setDate(3, computer.getDiscontinued());
			if(computer.getCompany().getId() == -1){
				preparedStatement.setNull(4, Types.INTEGER);
			}else{
				preparedStatement.setInt(4, computer.getCompany().getId());
			}
			break;
			
		default:
			break;
		}
		return preparedStatement;
	}

	public void handleException(ResultSet resultSet, SQLException exception){
		logger.error("Error with request.");
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException sqlException) { } // ignore
			resultSet = null;
		}
		exception.printStackTrace();
	}
}
