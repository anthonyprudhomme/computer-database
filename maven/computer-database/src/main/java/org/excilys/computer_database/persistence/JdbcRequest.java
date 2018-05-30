package org.excilys.computer_database.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.excilys.computer_database.model.Computer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JdbcRequest {

  private final Logger LOGGER = LoggerFactory.getLogger(JdbcRequest.class);
  private String loggerDatabasePrefix = "Request to SQL database: ";

  /**
   * Starts an MySQL request.
   * @param query Query you want to do
   * @param requestName type of request you are doing
   * @param computer Computer parameter
   * @return the resultSet from the request
   */
  public ResultSet doARequest(String query, RequestName requestName, Computer computer) {
    LOGGER.info(loggerDatabasePrefix + query);
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
      preparedStatement = this.prepareRequest(preparedStatement, requestName, computer);
      if (preparedStatement.execute()) {
        return preparedStatement.getResultSet();
      }
    } catch (SQLException sqlException) {
      LOGGER.error("SQLException: " + sqlException.getMessage());
      LOGGER.error("SQLState: " + sqlException.getSQLState());
      LOGGER.error("VendorError: " + sqlException.getErrorCode());
    }
    return resultSet;
  }

  /**
   * Starts an MySQL request.
   * @param query Query you want to do
   * @param requestName type of request you are doing
   * @return the resultSet from the request
   */
  public ResultSet doARequest(String query, RequestName requestName) {
    return this.doARequest(query, requestName, null);
  }

  /**
   * Prepare the preparedStatement.
   * @param preparedStatement preparedStatement you want to prepare
   * @param requestName type of request you are doing
   * @param computer computer parameter
   * @return the preparedStatement
   * @throws SQLException if there is an error when preparing the statement
   */
  private PreparedStatement prepareRequest(PreparedStatement preparedStatement, RequestName requestName, Computer computer) throws SQLException {
    switch (requestName) {

    case COMPUTER_DETAILS:
    case LIST_COMPANIES:
    case LIST_COMPUTERS:
    case DELETE_COMPUTER:
      break;
    case UPDATE_COMPUTER:
      preparedStatement.setString(1, computer.getName());
      preparedStatement.setDate(2, computer.getIntroduced());
      preparedStatement.setDate(3, computer.getDiscontinued());
      if (computer.getCompany().getId() == -1) {
        preparedStatement.setNull(4, Types.INTEGER);
      } else {
        preparedStatement.setInt(4, computer.getCompany().getId());
      }
      preparedStatement.setInt(5, computer.getId());
      break;

    case CREATE_COMPUTER:
      preparedStatement.setString(1, computer.getName());
      preparedStatement.setDate(2, computer.getIntroduced());
      preparedStatement.setDate(3, computer.getDiscontinued());
      if (computer.getCompany().getId() == -1) {
        preparedStatement.setNull(4, Types.INTEGER);
      } else {
        preparedStatement.setInt(4, computer.getCompany().getId());
      }
      break;

    default:
      break;
    }
    return preparedStatement;
  }

  /**
   * Handle exceptions.
   * @param resultSet To close it
   * @param exception exception that was caught
   */
  public void handleException(ResultSet resultSet, SQLException exception) {
    LOGGER.error("Error with request.");
    if (resultSet != null) {
      try {
        resultSet.close();
      } catch (SQLException sqlException) { } // ignore
      resultSet = null;
    }
    exception.printStackTrace();
  }

  /**
   * Count the number of computers.
   * @param query query to send to the database
   * @param requestName Type of request
   * @return the number of computers
   */
  public int countRequest(String query, RequestName requestName) {
    int count = -1;
    LOGGER.info(loggerDatabasePrefix + query);
    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
      if (preparedStatement.execute()) {
        resultSet = preparedStatement.getResultSet();
        count = resultSet.getInt("total");
      }
    } catch (SQLException sqlException) {
      LOGGER.error("SQLException: " + sqlException.getMessage());
      LOGGER.error("SQLState: " + sqlException.getSQLState());
      LOGGER.error("VendorError: " + sqlException.getErrorCode());
    }
    return count;
  }

  /**
   * Return the result from the query to get the items at a specified page.
   * @param limit The number of items you want per page
   * @param page the page number
   * @param query the query you want to ask to the database
   * @param requestName The type of request
   * @return the result from the query to get the items at a specified page.
   */
  public ResultSet itemsAtPageRequest(int limit, int page, String query, RequestName requestName) {

    ResultSet resultSet = null;
    PreparedStatement preparedStatement = null;
    try {
      preparedStatement = JdbcConnection.getConnection().prepareStatement(query);
      preparedStatement.setInt(1, limit);
      int offset = limit * (page - 1);
      preparedStatement.setInt(2, offset);
      LOGGER.info(loggerDatabasePrefix + preparedStatement.toString());
      if (preparedStatement.execute()) {
        resultSet = preparedStatement.getResultSet();
      }
    } catch (SQLException sqlException) {
      LOGGER.error("SQLException: " + sqlException.getMessage());
      LOGGER.error("SQLState: " + sqlException.getSQLState());
      LOGGER.error("VendorError: " + sqlException.getErrorCode());
    }
    return resultSet;
  }
}
