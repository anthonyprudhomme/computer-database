package org.excilys.computer_database.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Optional;

import javax.sql.DataSource;

import org.excilys.computer_database.mapper.ComputerMapper;
import org.excilys.computer_database.model.Computer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;

@Component
public class ComputerDaoImpl implements ComputerDao {

  private static final String QUERY_GET_ALL_COMPUTERS = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id=company.id ";
  private static final String QUERY_COMPUTER_DETAIL = "SELECT computer.id, computer.name, computer.introduced, computer.discontinued, company.id, company.name FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE computer.id= ?";
  private static final String QUERY_CREATE_COMPUTER = "INSERT INTO computer (name, introduced, discontinued, company_id) VALUES(?,?,?,?)";
  private static final String QUERY_UPDATE_COMPUTER = "UPDATE computer SET name= ? , introduced= ? , discontinued= ? , company_id= ? WHERE id= ?";
  private static final String QUERY_DELETE_COMPUTER = "DELETE FROM computer WHERE id= ?";
  private static final String QUERY_DELETE_COMPUTERS = "DELETE FROM computer WHERE id IN ";
  private static final String QUERY_COUNT_COMPUTER = "SELECT COUNT(computer.id) AS total FROM computer";
  private static final String QUERY_COUNT_COMPUTER_WITH_SEARCH = "SELECT COUNT(computer.id) AS total FROM computer LEFT JOIN company ON computer.company_id=company.id WHERE (computer.name LIKE ? OR company.name LIKE ? ) ";
  private static final String SEARCH = "WHERE (computer.name LIKE ? OR company.name LIKE ? ) ";
  private static final String PAGE = "LIMIT ? OFFSET ? ";
  private static final String ORDER_BY = "ORDER BY ";

  private JdbcTemplate jdbcTemplate;

  @Autowired
  public void setDataSource(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public ArrayList<Computer> getComputers() {
    return (ArrayList<Computer>) jdbcTemplate.query(QUERY_GET_ALL_COMPUTERS, new ComputerMapper());
  }

  @Override
  public Optional<Computer> getComputerDetails(int id) {
    try {
      return Optional.of((Computer) jdbcTemplate.queryForObject(QUERY_COMPUTER_DETAIL, new Object[]{id}, new ComputerMapper()));
    } catch (EmptyResultDataAccessException exception) {
      return Optional.empty();
    }
  }

  @Override
  public void createComputer(Computer computer) {
    PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
      public void setValues(PreparedStatement preparedStatement) throws
      SQLException {
        preparedStatement.setString(1, computer.getName());
        preparedStatement.setDate(2, computer.getIntroduced());
        preparedStatement.setDate(3, computer.getDiscontinued());
        if (computer.getCompany() == null || computer.getCompany().getId() == -1) {
          preparedStatement.setNull(4, Types.INTEGER);
        } else {
          preparedStatement.setInt(4, computer.getCompany().getId());
        }
      }
    };
    jdbcTemplate.update(QUERY_CREATE_COMPUTER, preparedStatementSetter);
  }

  @Override
  public void updateComputer(Computer computer) {
    PreparedStatementSetter preparedStatementSetter = new PreparedStatementSetter() {
      public void setValues(PreparedStatement preparedStatement) throws
      SQLException {
        preparedStatement.setString(1, computer.getName());
        preparedStatement.setDate(2, computer.getIntroduced());
        preparedStatement.setDate(3, computer.getDiscontinued());
        if (computer.getCompany() == null || computer.getCompany().getId() == -1) {
          preparedStatement.setNull(4, Types.INTEGER);
        } else {
          preparedStatement.setInt(4, computer.getCompany().getId());
        }
        preparedStatement.setInt(5, computer.getId());
      }
    };
    jdbcTemplate.update(QUERY_UPDATE_COMPUTER, preparedStatementSetter);
  }

  @Override
  public void deleteComputer(int id) {
    jdbcTemplate.update(QUERY_DELETE_COMPUTER, new Object[]{id});
  }

  @Override
  public int countComputers() {
    return jdbcTemplate.queryForObject(QUERY_COUNT_COMPUTER, Integer.class);
  }

  @Override
  public int countComputers(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return countComputers();
    }
    return jdbcTemplate.queryForObject(QUERY_COUNT_COMPUTER_WITH_SEARCH, new Object[]{"%" + keyword + "%", "%" + keyword + "%"}, Integer.class);
  }

  @Override
  public ArrayList<Computer> getComputersWithParams(int numberOfItemPerPage, int page, String keyword,
      OrderByParams orderByParams) {
    String query = null;
    int offset = numberOfItemPerPage * (page - 1);
    if (keyword == null || keyword.isEmpty()) {
      if (orderByParams == null) {
        query = QUERY_GET_ALL_COMPUTERS + PAGE;
        return (ArrayList<Computer>) jdbcTemplate.query(query, new Object[]{numberOfItemPerPage, offset}, new ComputerMapper());
      } else {
        query = QUERY_GET_ALL_COMPUTERS + ORDER_BY + " ISNULL(" + orderByParams.getColumnToOrder() + ")," + orderByParams.getColumnToOrder() + " " + orderByParams.getAscOrDesc() + " " + PAGE;
        return (ArrayList<Computer>) jdbcTemplate.query(query, new Object[]{numberOfItemPerPage, offset}, new ComputerMapper());
      }
    } else {
      if (orderByParams == null) {
        query = QUERY_GET_ALL_COMPUTERS + SEARCH + PAGE;
        return (ArrayList<Computer>) jdbcTemplate.query(query, new Object[]{"%" + keyword + "%", "%" + keyword + "%", numberOfItemPerPage, offset}, new ComputerMapper());
      } else {
        query = QUERY_GET_ALL_COMPUTERS + SEARCH + ORDER_BY + " ISNULL(" + orderByParams.getColumnToOrder() + ")," + orderByParams.getColumnToOrder() + " " + orderByParams.getAscOrDesc() + " " + PAGE;
        return (ArrayList<Computer>) jdbcTemplate.query(query, new Object[]{"%" + keyword + "%", "%" + keyword + "%", numberOfItemPerPage, offset}, new ComputerMapper());
      }
    }
  }

  @Override
  public void deleteComputers(int[] ids) {
    String query = QUERY_DELETE_COMPUTERS + "(";
    for (int i = 0; i < ids.length; i++) {
      query += ids[i];
      if (i != ids.length - 1) {
        query += ",";
      }
    }
    query += ")";
    System.out.println(query);
    jdbcTemplate.update(query);
  }
}
