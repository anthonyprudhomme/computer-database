package org.excilys.computer_database.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;

import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.validation.DatabaseTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Selenium {

  private static WebDriver driver;
  private String baseUrl = "http://localhost:8081/computer-database/list-computer";

  /**
   * Starts the Firefox driver.
   * @throws ClassNotFoundException If the class is not found
   * @throws SQLException If there is an error with the database
   */
  @BeforeClass
  public static void initDriver() throws ClassNotFoundException, SQLException {
    JdbcConnection.testMode = true;
    Class.forName("org.hsqldb.jdbc.JDBCDriver");
    DatabaseTest.initDatabase();
    //System.setProperty("webdriver.gecko.driver", "geckodriver");
    driver = new FirefoxDriver();
  }

  /**
   * Stops the Firefox driver.
   */
  @AfterClass
  public static void closeDriver() {
    JdbcConnection.testMode = false;
    driver.close();
  }

  /**
   * Checks the title of the page is the right one.
   */
  @Test
  public void checkTitle() {
    String expectedTitle = "Computer Database";
    String actualTitle = "";
    driver.get(baseUrl);
    actualTitle = driver.getTitle();
    assertTrue(actualTitle.equals(expectedTitle));
  }
  /**
   * Check if the number of item per pages is the right one.
   */
  @Test
  public void checkNumberOfItemPerPage() {
    String query = "?page=1&numberOfItemPerPage=10";
    driver.get(baseUrl + query);
    int numberOfItems = driver.findElement(By.id("results")).findElements(By.tagName("tr")).size();
    assertEquals(10, numberOfItems);
    query = "?page=1&numberOfItemPerPage=100";
    driver.get(baseUrl + query);
    numberOfItems = driver.findElement(By.id("results")).findElements(By.tagName("tr")).size();
    assertEquals(100, numberOfItems);
  }

  /**
   * Check jQuery validation.
   */
  @Test
  public void checkJqueryValidationOnAddComputer() {
    driver.get(baseUrl);
    driver.findElement(By.id("addComputer")).click();

    String elementToTest = driver.findElement(By.id("computerName")).findElement(By.xpath("..")).getAttribute("class");
    assertTrue(elementToTest.contains("has-error"));

    driver.findElement(By.id("computerName")).sendKeys("Selenium tested computer");
    elementToTest = driver.findElement(By.id("computerName")).findElement(By.xpath("..")).getAttribute("class");
    assertTrue(elementToTest.contains("has-success"));

    driver.findElement(By.id("introduced")).sendKeys("10-10-10");
    elementToTest = driver.findElement(By.id("introduced")).findElement(By.xpath("..")).getAttribute("class");
    assertTrue(elementToTest.contains("has-success"));

    driver.findElement(By.id("discontinued")).sendKeys("11-10-10");
    elementToTest = driver.findElement(By.id("discontinued")).findElement(By.xpath("..")).getAttribute("class");
    assertTrue(elementToTest.contains("has-success"));
  }


  /**
   * Check if the computer is properly added.
   */
  @Test
  public void checkAddComputerWithValidData() {
    driver.get(baseUrl);
    int numberOfComputer = Integer.valueOf(driver.findElement(By.id("homeTitle")).getText().replaceAll("Computers found", "").trim());
    driver.findElement(By.id("addComputer")).click();
    driver.findElement(By.id("computerName")).sendKeys("Selenium tested computer");
    driver.findElement(By.id("introduced")).sendKeys("2000-10-10");
    driver.findElement(By.id("discontinued")).sendKeys("2001-10-10");

    driver.findElement(By.id("submitButton")).click();
    driver.get(baseUrl);
    int newNumberOfComputer = Integer.valueOf(driver.findElement(By.id("homeTitle")).getText().replaceAll("Computers found", "").trim());
    assertEquals(numberOfComputer + 1, newNumberOfComputer);
  }

  /**
   * Check if the computer is not added with invalid data.
   */
  @Test
  public void checkAddComputerWithInvalidData() {
    driver.get(baseUrl);
    int numberOfComputer = Integer.valueOf(driver.findElement(By.id("homeTitle")).getText().replaceAll("Computers found", "").trim());
    driver.findElement(By.id("addComputer")).click();
    driver.findElement(By.id("computerName")).sendKeys("Selenium tested computer");
    driver.findElement(By.id("introduced")).sendKeys("2001-10-10");
    driver.findElement(By.id("discontinued")).sendKeys("2000-10-10");

    driver.findElement(By.id("submitButton")).click();
    driver.get(baseUrl);
    int newNumberOfComputer = Integer.valueOf(driver.findElement(By.id("homeTitle")).getText().replaceAll("Computers found", "").trim());
    assertEquals(numberOfComputer, newNumberOfComputer);
  }

}