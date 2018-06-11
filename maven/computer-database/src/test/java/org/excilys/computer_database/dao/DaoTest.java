package org.excilys.computer_database.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

import javax.sql.DataSource;

import org.excilys.computer_database.AppTestConfig;
import org.excilys.computer_database.exceptions.CDBObjectCompanyIdException;
import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class DaoTest {

  @Autowired
  private CompanyService companyService;

  @Autowired
  private ComputerService computerService;

  @Autowired
  private DataSource dataSource;
  /**
   * Prepares the database.
   * @throws SQLException if there is an SQLException
   * @throws ClassNotFoundException if there is a ClassNotFoundException
   * @throws IOException if there is an IOException
   */
  @Before
  public void init() throws SQLException, ClassNotFoundException, IOException {
    Class.forName("org.hsqldb.jdbc.JDBCDriver");
    initDatabase();
  }

  /**
   * Reset the connection to default mode.
   * @throws SQLException  If there is an SQLException
   */
  @After
  public void end() throws SQLException {
    Statement statement = dataSource.getConnection().createStatement();
    statement.execute("DROP TABLE company;");
    statement.execute("DROP TABLE computer;");
    statement.close();
    dataSource.getConnection().commit();
    dataSource.getConnection().close();
  }

  /**
   * Database initialization for testing.
   * @throws SQLException if there is an SQLException
   */
  public void initDatabase() throws SQLException {
    Statement statement = dataSource.getConnection().createStatement();
    statement.execute("create table company ("
        + "id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,"
        + "name VARCHAR(255),"
        + "constraint pk_company primary key (id));"
        );

    statement.execute("create table computer ("
        + "id INT NOT NULL GENERATED BY DEFAULT AS IDENTITY,"
        + "name VARCHAR(255),"
        + "introduced DATE NULL,"
        + "discontinued DATE NULL,"
        + "company_id INT default NULL,"
        + "constraint pk_computer primary key (id));"
        );
    statement.executeUpdate("INSERT INTO company (name) values ('Apple Inc.');");
    statement.executeUpdate("INSERT INTO company (name) values ('HP');");
    statement.executeUpdate("INSERT INTO company (name) values ('Company to delete');");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 1','1983-12-01','1984-04-01',1);");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 2',null,'1984-04-01',1);");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 3','1983-12-01',null,1);");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 4',null,null,1);");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 5',null,null,null);");
    statement.executeUpdate("INSERT INTO computer (name,introduced,discontinued,company_id) values ('Computer 1','1983-12-01','1984-04-01',3);");
    statement.close();
    dataSource.getConnection().commit();
    dataSource.getConnection().close();
  }

  /**
   * Get total records in computer table.
   * @return total number of records. In case of exception 0 is returned
   */
  private int getNumberOfComputers() {
    return computerService.countComputers();
  }

  /**
   * Get total records in company table.
   * @return total number of records. In case of exception 0 is returned
   */
  private int getNumberOfCompanies() {
    return companyService.countCompanies();
  }

  /**
   * Get all computers and checks there is the right number.
   */
  @Test
  public void testListOfComputers() {
    assertEquals(6, getNumberOfComputers());
  }

  /**
   * Get all companies and checks there is the right number.
   */
  @Test
  public void testListOfCompanies() {
    assertEquals(3, getNumberOfCompanies());
  }

  /**
   * Get all companies and checks there is the right number.
   */
  @Test
  public void testGetDetailedComputerWithValidId() {
    int goodId = 1;
    Optional<Computer> computer = computerService.getComputerDetails(goodId);
    assertTrue(computer.isPresent());
    assertEquals(goodId, (int) computer.get().getId());
  }

  /**
   * Get all companies and checks there is the right number.
   */
  @Test
  public void testGetDetailedComputerWithInvalidId() {
    int wrongId = -1;
    Optional<Computer> computer = computerService.getComputerDetails(wrongId);
    assertFalse(computer.isPresent());
  }

  /**
   * Check computer creation with valid.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testCreateComputerWithValidData() throws CDBObjectException {
    Computer goodComputer = new Computer(-1, "New good computer", Date.valueOf("1999-10-10"), Date.valueOf("2000-10-10"), 1, "Apple Inc.");
    computerService.createComputer(goodComputer);
  }

  /**
   * Check computer creation with invalid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test(expected = CDBObjectCompanyIdException.class)
  public void testCreateComputerWithInvalidCompanyId() throws CDBObjectException {
    Computer wrongCompanyIdComputer = new Computer(-1, "New wrong company id computer", Date.valueOf("1999-10-10"), Date.valueOf("2000-10-10"), 99, "Apple Inc.");
    computerService.createComputer(wrongCompanyIdComputer);
  }

  /**
   * Check computer creation with invalid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test(expected = CDBObjectException.class)
  public void testCreateComputerWithInvalidDate() throws CDBObjectException {
    Computer wrongDateComputer = new Computer(-1, "New wrong date computer", Date.valueOf("1998-10-10"), Date.valueOf("1997-10-10"), 1, "Apple Inc.");
    computerService.createComputer(wrongDateComputer);
  }

  /**
   * Check computer update with valid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testUpdateComputerWithValidData() throws CDBObjectException {
    Computer goodComputer = new Computer(1, "Updated good computer", Date.valueOf("1999-10-10"), Date.valueOf("2000-10-10"), 1, "Apple Inc.");
    computerService.updateComputer(goodComputer);
  }

  /**
   * Check computer update with valid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testUpdateComputerWithOnlyName() throws CDBObjectException {
    Computer onlyNameComputer = new Computer(1, "Updated only name computer", null, null, -1, null);
    computerService.updateComputer(onlyNameComputer);
  }

  /**
   * Check computer update with valid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testUpdateComputerWithOnlyNameAndDate() throws CDBObjectException {
    Computer onlyNameAndDateComputer = new Computer(1, "Updated only name and date computer", Date.valueOf("1999-10-10"), Date.valueOf("2000-10-10"), -1, null);
    computerService.updateComputer(onlyNameAndDateComputer);
  }

  /**
   * Check computer update with valid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testUpdateComputerWithOnlyNameAndCompanyId() throws CDBObjectException {
    Computer onlyNameCompanyIdComputer = new Computer(1, "Updated only name and company idcomputer", null, null, 1, null);
    computerService.updateComputer(onlyNameCompanyIdComputer);
  }

  /**
   * Check computer update with invalid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test(expected = CDBObjectCompanyIdException.class)
  public void testUpdateComputerWithWrongCompanyid() throws CDBObjectException {
    Computer wrongCompanyIdComputer = new Computer(1, "Updated wrong company id computer", Date.valueOf("1999-10-10"), Date.valueOf("2000-10-10"), 99, "Apple Inc.");
    computerService.updateComputer(wrongCompanyIdComputer);
  }

  /**
   * Check computer update with invalid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test(expected = CDBObjectException.class)
  public void testUpdateComputerWithWrongDate() throws CDBObjectException {
    Computer wrongDateComputer = new Computer(1, "Updated wrong date computer", Date.valueOf("1998-10-10"), Date.valueOf("1998-10-10"), 1, "Apple Inc.");
    computerService.updateComputer(wrongDateComputer);
  }

  /**
   * Check computer update with valid parameters.
   * @throws CDBObjectException Thrown if there is an error with the validation
   */
  @Test
  public void testDeleteCompany() throws CDBObjectException {
    assertEquals(3, getNumberOfCompanies());
    companyService.deleteCompany(2);
    assertEquals(2, getNumberOfCompanies());
  }

}
