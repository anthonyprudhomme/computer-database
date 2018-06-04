package org.excilys.computer_database.util;

import java.sql.Date;

import javax.servlet.http.HttpServletRequest;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;

public class Util {

  public static final String COMPUTER_NAME = "computerName";
  public static final String INTRODUCED = "introduced";
  public static final String DISCONTINUED = "discontinued";
  public static final String COMPANY_ID = "companyId";

  @Autowired
  private static CompanyService companyService;

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
   * Get a computer from the request.
   * @param request Sent from user
   * @return the computer created
   */
  public static Computer getComputerFromRequest(HttpServletRequest request) {
    int id = -1;
    String idAsString = request.getParameter("id");
    if (isInteger(idAsString)) {
      id = Integer.valueOf(idAsString);
    }
    String computerName = request.getParameter(COMPUTER_NAME);
    Date introduced = Util.getDateFromInput(request, INTRODUCED);
    Date discontinued = Util.getDateFromInput(request, DISCONTINUED);

    int companyId = Integer.valueOf(request.getParameter(COMPANY_ID));
    Company company = companyService.getCompany(companyId);
    return new Computer(id, computerName, introduced, discontinued, companyId, company.getName());
  }

}
