package org.excilys.computer_database.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.excilys.computer_database.mapper.ComputerMapper;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.persistence.JdbcRequest;
import org.excilys.computer_database.persistence.RequestName;

public class ComputerDaoImpl implements ComputerDao {

  private static final String QUERY_GET_ALL_COMPUTERS = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id=company.id ";
  private static final String QUERY_COMPUTER_DETAIL = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id=";
  private static final String QUERY_CREATE_COMPUTER = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
  private static final String QUERY_UPDATE_COMPUTER = "UPDATE computer SET name=?, introduced=?, discontinued=?, company_id=? WHERE id=?";
  private static final String QUERY_DELETE_COMPUTER = "DELETE FROM computer WHERE id=";
  private static final String QUERY_COUNT_COMPUTER = "SELECT COUNT(computer.id) AS total FROM computer";
  private static final String QUERY_COUNT_COMPUTER_WITH_SEARCH = "SELECT COUNT(computer.id) AS total FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE (computer.name LIKE ? OR company.name LIKE ? ) ";

  private static final String SEARCH = "WHERE (computer.name LIKE ? OR company.name LIKE ? ) ";
  private static final String PAGE = "LIMIT ? OFFSET ? ";
  private static final String ORDER_BY = "ORDER BY ";

  private JdbcRequest jdbcRequest = new JdbcRequest();
  private static ComputerDaoImpl instance = null;
  /**
   * Singleton.
   */
  private ComputerDaoImpl() {
    jdbcRequest = new JdbcRequest();
  }

  /**
   * @return singleton
   */
  public static ComputerDaoImpl getInstance() {
    if (instance == null) {
      instance = new ComputerDaoImpl();
    }
    return instance;
  }

  @Override
  public ArrayList<Computer> getComputers() {
    String query = QUERY_GET_ALL_COMPUTERS;
    ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.LIST_COMPUTERS);
    return getListOfComputersFromResultSet(resultSet);
  }

  /**
   * Return the list of computers from the resultSet object.
   * @param resultSet ResultSet object from the request.
   * @return the list of computers from the resultSet object.
   */
  private ArrayList<Computer> getListOfComputersFromResultSet(ResultSet resultSet) {
    ArrayList<Computer> computers = new ArrayList<Computer>();
    try {
      while (resultSet.next()) {
        computers.add(ComputerMapper.getInstance().mapComputer(resultSet));
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
    String query = QUERY_COMPUTER_DETAIL + id;
    ResultSet resultSet = jdbcRequest.doARequest(query, RequestName.COMPUTER_DETAILS);
    try {
      while (resultSet.next()) {
        computer = ComputerMapper.getInstance().mapComputer(resultSet);
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
    String query = QUERY_CREATE_COMPUTER;
    jdbcRequest.doARequest(query, RequestName.CREATE_COMPUTER, computer);

  }

  @Override
  public void updateComputer(Computer computer) {
    String query = QUERY_UPDATE_COMPUTER;
    jdbcRequest.doARequest(query, RequestName.UPDATE_COMPUTER, computer);

  }

  @Override
  public void deleteComputer(int id) {
    String query = QUERY_DELETE_COMPUTER + id;
    jdbcRequest.doARequest(query, RequestName.DELETE_COMPUTER);
  }

  @Override
  public int countComputers() {
    String query = QUERY_COUNT_COMPUTER;
    return jdbcRequest.countRequest(query, RequestName.COUNT_COMPUTERS);
  }

  @Override
  public int countComputers(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return countComputers();
    }
    String query = QUERY_COUNT_COMPUTER_WITH_SEARCH;
    return jdbcRequest.countWithKeywordRequest(query, RequestName.COUNT_COMPUTERS, keyword);
  }

  @Override
  public ArrayList<Computer> getComputersWithParams(int numberOfItemPerPage, int page, String keyword,
      OrderByParams orderByParams) {
    String query;
    ResultSet resultSet;
    if (keyword == null || keyword.isEmpty()) {
      if (orderByParams == null) {
        query = QUERY_GET_ALL_COMPUTERS + PAGE;
        resultSet = jdbcRequest.itemsWithPage(query, numberOfItemPerPage, page);
      } else {
        query = QUERY_GET_ALL_COMPUTERS + ORDER_BY + " ISNULL(" + orderByParams.getColumnToOrder() + ")," + orderByParams.getColumnToOrder() + " " + orderByParams.getAscOrDesc() + " " + PAGE;
        resultSet = jdbcRequest.itemsWithPageAndOrderBy(query, numberOfItemPerPage, page, orderByParams);
      }
    } else {
      if (orderByParams == null) {
        query = QUERY_GET_ALL_COMPUTERS + SEARCH + PAGE;
        resultSet = jdbcRequest.itemsWithPageAndSearch(query, numberOfItemPerPage, page, keyword);
      } else {
        query = QUERY_GET_ALL_COMPUTERS + SEARCH + ORDER_BY + " ISNULL(" + orderByParams.getColumnToOrder() + ")," + orderByParams.getColumnToOrder() + " " + orderByParams.getAscOrDesc() + " " + PAGE;
        resultSet = jdbcRequest.itemsWithParams(query, numberOfItemPerPage, page, keyword, orderByParams);
      }
    }
    return getListOfComputersFromResultSet(resultSet);
  }

}
