package org.excilys.computer_database.util;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Enumeration;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;

public class Util {

  public static final String COMPUTER_NAME = "computerName";
  public static final String INTRODUCED = "introduced";
  public static final String DISCONTINUED = "discontinued";
  public static final String COMPANY_ID = "companyId";

  private static final String DATE_FORMAT = "yyyy-MM-dd";

  /**
   * Tells if the String is possible to cast into an integer or not.
   * @param s the String to check
   * @return Whether the string is possible to cast or not
   */
  public static boolean isInteger(String s) {
    return isInteger(s, 10);
  }

  /**
   * Tells if the String is possible to cast into an integer or not.
   * @param s the String to check
   * @param radix The range of numbers
   * @return Whether the string is possible to cast or not
   */
  public static boolean isInteger(String s, int radix) {
    if (s == null || s.isEmpty()) {
      return false;
    }
    for (int i = 0; i < s.length(); i++) {
      if (i == 0 && s.charAt(i) == '-') {
        if (s.length() == 1) {
          return false;
        } else {
          continue;
        }
      }
      if (Character.digit(s.charAt(i), radix) < 0) {
        return false;
      }
    }
    return true;
  }

  /**
   * Return the date from the user input. Returns null if nothing was entered.
   * @param request HttpServletRequest to get the param
   * @param paramName name of the param you want to check in the request
   * @return the date.
   */
  public static Date getDateFromInput(HttpServletRequest request, String paramName) {
    Date date = null;
    String userInput = request.getParameter(paramName);
    if (userInput != null && !userInput.isEmpty()) {
      date = Date.valueOf(request.getParameter(paramName));
    }
    return date;
  }

  /**
   * Return the date from String. Returns null if nothing was entered.
   * @param dateString The date as String
   * @return the date
   */
  public static Date getDateFromString(String dateString) {
    Date date = null;
    if (dateString != null && !dateString.isEmpty()) {
      date = Date.valueOf(dateString);
    }
    return date;
  }

  /**
   * Get a computer from the request.
   * @param request Sent from user
   * @param companyService company service object to query the database
   * @return the computer created
   */
  public static Computer getComputerFromRequest(HttpServletRequest request, CompanyService companyService) {
    int id = -1;
    String idAsString = request.getParameter("id");
    if (isInteger(idAsString)) {
      id = Integer.valueOf(idAsString);
    }
    String computerName = request.getParameter(COMPUTER_NAME);
    Date introduced = Util.getDateFromInput(request, INTRODUCED);
    Date discontinued = Util.getDateFromInput(request, DISCONTINUED);

    int companyId = Integer.valueOf(request.getParameter(COMPANY_ID));
    Company company = companyService.getCompany(companyId).get();
    return new Computer(id, computerName, introduced, discontinued, companyId, company.getName());
  }

  /**
   * Convert ResourceBundle into a Properties object.
   * @param resource a resource bundle to convert.
   * @return Properties a properties version of the resource bundle.
   */
  public static Properties convertResourceBundleToProperties(ResourceBundle resource) {
    Properties properties = new Properties();
    Enumeration<String> keys = resource.getKeys();
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      properties.put(key, resource.getString(key));
    }
    return properties;
  }

  /**
   * Check whether the date is valid or not.
   * @param date the date you want to check
   * @return whether the date is valid or not
   */
  public static boolean isDateValid(String date) {
    try {
      DateFormat df = new SimpleDateFormat(DATE_FORMAT);
      df.setLenient(false);
      df.parse(date);
      return true;
    } catch (ParseException e) {
      return false;
    }
  }

}
