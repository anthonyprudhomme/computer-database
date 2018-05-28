package org.excilys.computer_database.selenium;

import static org.junit.Assert.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
//comment the above line and uncomment below line to use Chrome
//import org.openqa.selenium.chrome.ChromeDriver;
public class Selenium {

  private WebDriver driver;

  /**
   * Starts the Firefox driver.
   */
  @BeforeClass
  public void initDriver() {
    //System.setProperty("webdriver.gecko.driver", "geckodriver");
    driver = new FirefoxDriver();
  }

  /**
   * Stops the Firefow driver.
   */
  @AfterClass
  public void closeDriver() {
    driver.close();
  }

  /**
   * Checks the title of the page is the right one.
   */
  @Test
  public void checkTitle() {
    String baseUrl = "http://localhost:8080/computer-database/list-computer";
    String expectedTitle = "Computer Database";
    String actualTitle = "";
    driver.get(baseUrl);
    actualTitle = driver.getTitle();
    assertTrue(actualTitle.equals(expectedTitle));
  }

}