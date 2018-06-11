package org.excilys.computer_database.selenium;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import org.excilys.computer_database.AppTestConfig;
//import org.excilys.computer_database.persistence.JdbcConnection;
//import org.excilys.computer_database.validation.DatabaseTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppTestConfig.class)
public class Selenium {

  private static WebDriver driver;
  private static final String BASE_URL = "http://localhost:8080/computer-database/computers";

  /**
   * Starts the Firefox driver.
   * @throws ClassNotFoundException If the class is not found
   * @throws SQLException If there is an error with the database
   */
  @BeforeClass
  public static void initDriver() throws ClassNotFoundException, SQLException {
    driver = new FirefoxDriver();
  }

  /**
   * Stops the Firefox driver.
   */
  @AfterClass
  public static void closeDriver() {
    driver.close();
  }

  /**
   * Checks the title of the page is the right one.
   */
  @Test
  public void checkTitle() {
    String expectedTitle = "Computer Database";
    String actualTitle = "";
    driver.get(BASE_URL);
    actualTitle = driver.getTitle();
    assertTrue(actualTitle.equals(expectedTitle));
  }
  /**
   * Check if the number of item per pages is the right one.
   */
  @Test
  public void checkNumberOfItemPerPage() {
    String query = "?page=1&numberOfItemPerPage=10";
    driver.get(BASE_URL + query);
    int numberOfItems = driver.findElement(By.id("results")).findElements(By.tagName("tr")).size();
    assertEquals(10, numberOfItems);
    query = "?page=1&numberOfItemPerPage=100";
    driver.get(BASE_URL + query);
    numberOfItems = driver.findElement(By.id("results")).findElements(By.tagName("tr")).size();
    assertEquals(100, numberOfItems);
  }

  /**
   * Check jQuery validation.
   */
  @Test
  public void checkJqueryValidationOnAddComputer() {
    driver.get(BASE_URL);
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
    driver.get(BASE_URL);
    int numberOfComputer = getTotalNumberOfComputers();
    goToAddPage();
    String name = "Selenium tested computer";
    String introducedDate = "2000-10-10";
    String discontinuedDate = "2001-10-10";
    fillFormWithDataAndSubmit(name, introducedDate, discontinuedDate);
    int newNumberOfComputer = getTotalNumberOfComputers();
    assertEquals(numberOfComputer + 1, newNumberOfComputer);
  }

  /**
   * Check if the computer is not added with invalid data.
   */
  @Test
  public void checkAddComputerWithInvalidData() {
    driver.get(BASE_URL);
    int numberOfComputer = getTotalNumberOfComputers();
    goToAddPage();
    String name = "Selenium tested computer";
    String introducedDate = "2003-10-10";
    String discontinuedDate = "2002-10-10";
    fillFormWithDataAndSubmit(name, introducedDate, discontinuedDate);
    int newNumberOfComputer = getTotalNumberOfComputers();
    assertEquals(numberOfComputer, newNumberOfComputer);
  }

  /**
   * Check if the update is successful with valid data.
   */
  @Test
  public void checkUpdateComputerWithValidData() {
    driver.get(BASE_URL);
    goToEditPage();
    String introducedDateToUpdate = "2003-10-10";
    fillFormWithDataAndSubmit(null, introducedDateToUpdate, null);
    assertTrue(checkIntroducedInList(introducedDateToUpdate));
  }

  /**
   * Check if the update is not successful with invalid data.
   */
  @Test
  public void checkUpdateComputerWithInvalidData() {
    driver.get(BASE_URL);
    goToEditPage();
    String introducedDateToUpdate = "2003-10-10";
    String discontinuedDateToUpdate = "2002-10-10";
    fillFormWithDataAndSubmit(null, introducedDateToUpdate, discontinuedDateToUpdate);
    boolean introducedIsTheSame = checkIntroducedInList(introducedDateToUpdate);
    boolean discontinuedIsTheSame = checkDiscontinuedInList(discontinuedDateToUpdate);
    assertFalse(introducedIsTheSame && discontinuedIsTheSame);
  }

  /**
   * Check if the computer is properly deleted.
   */
  @Test
  public void checkDeleteComputer() {
    driver.get(BASE_URL);
    int numberOfComputer = getTotalNumberOfComputers();
    goToEditMode();
    selectComputerToDelete();
    clickDeleteIcon();
    driver.switchTo().alert().accept();
    driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
    driver.get(BASE_URL);
    driver.manage().timeouts().implicitlyWait(1000, TimeUnit.MILLISECONDS);
    int newNumberOfComputer = getTotalNumberOfComputers();
    assertEquals(numberOfComputer, newNumberOfComputer + 1);
  }

  /**
   * Go to the edit mode.
   */
  private void goToEditMode() {
    driver.findElement(By.id("editComputer")).click();
  }

  /**
   * Check the box of the computer to delete.
   */
  private void selectComputerToDelete() {
    ArrayList<WebElement> rowsOfTable = new ArrayList<>(driver.findElement(By.id("results")).findElements(By.tagName("tr")));
    ArrayList<WebElement> colsOfFirstRow = new ArrayList<>(rowsOfTable.get(0).findElements(By.tagName("td")));
    colsOfFirstRow.get(0).findElement(By.tagName("input")).click();
  }

  /**
   * Click on the delete icon.
   */
  private void clickDeleteIcon() {
    WebElement element = driver.findElement(By.tagName("thead"));
    element.findElement(By.tagName("a")).click();
  }


  /**
   * Go to the add page.
   */
  private void goToAddPage() {
    driver.get(BASE_URL);
    driver.findElement(By.id("addComputer")).click();
  }

  /**
   * Go to the edit page.
   */
  private void goToEditPage() {
    driver.get(BASE_URL);
    ArrayList<WebElement> rowsOfTable = new ArrayList<>(driver.findElement(By.id("results")).findElements(By.tagName("tr")));
    ArrayList<WebElement> colsOfFirstRow = new ArrayList<>(rowsOfTable.get(0).findElements(By.tagName("td")));
    colsOfFirstRow.get(1).findElement(By.tagName("a")).click();
  }

  /**
   * Get the number of computers.
   * @return the number of computers
   */
  private int getTotalNumberOfComputers() {
    return Integer.valueOf(driver.findElement(By.id("homeTitle")).getText().replaceAll("Computers found", "").trim());
  }

  /**
   * Fill the edit or add form with following parameters.
   * @param name Name of the computer
   * @param introduced Date when introduced
   * @param discontinued Date when discontinued
   */
  private void fillFormWithDataAndSubmit(String name, String introduced, String discontinued) {
    if (name != null) {
      WebElement nameElement = driver.findElement(By.id("computerName"));
      nameElement.clear();
      nameElement.sendKeys(name);
    }
    if (introduced != null) {
      WebElement introducedElement = driver.findElement(By.id("introduced"));
      introducedElement.clear();
      introducedElement.sendKeys(introduced);
    }
    if (discontinued != null) {
      WebElement discontinuedElement = driver.findElement(By.id("discontinued"));
      discontinuedElement.clear();
      discontinuedElement.sendKeys(discontinued);
    }
    driver.findElement(By.id("submitButton")).click();
    driver.get(BASE_URL);
  }

  /**
   * Check that the introduced date is the same as the one filled in the form.
   * @param introduced Date you want to check
   * @return whether the date is corresponding or not
   */
  private boolean checkIntroducedInList(String introduced) {
    return introduced.equals(getElementAtIndex(2));
  }

  /**
   * Check that the discontinued date is the same as the one filled in the form.
   * @param discontinued Date you want to check
   * @return whether the date is corresponding or not
   */
  private boolean checkDiscontinuedInList(String discontinued) {
    return discontinued.equals(getElementAtIndex(3));
  }

  /**
   * Return the content of the cell at index.
   * @param index index you want to get the content of
   * @return the content of the cell
   */
  private String getElementAtIndex(int index) {
    ArrayList<WebElement> rowsOfTable = new ArrayList<>(driver.findElement(By.id("results")).findElements(By.tagName("tr")));
    ArrayList<WebElement> colsOfFirstRow = new ArrayList<>(rowsOfTable.get(0).findElements(By.tagName("td")));
    return colsOfFirstRow.get(index).getText();
  }

}