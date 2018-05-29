package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.validation.ComputerValidationStatus;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

@WebServlet("/add-computer")
public class AddComputerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  public static final String COMPUTER_NAME = "computerName";
  public static final String INTRODUCED = "introduced";
  public static final String DISCONTINUED = "discontinued";
  public static final String COMPANY_ID = "companyId";

  public static final String ADD_COMPUTER = "/WEB-INF/views/addComputer.jsp";
  //public static final String DASHBOARD = "/WEB-INF/views/dashboard.jsp";
  public static final String DASHBOARD = "list-computer";

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ArrayList<Company> companies = CompanyService.getInstance().getCompanies();
    request.setAttribute("companies", companies);
    this.getServletContext().getRequestDispatcher(ADD_COMPUTER).forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String computerName = request.getParameter(COMPUTER_NAME);
    Date introduced = getDateFromInput(request, INTRODUCED);
    Date discontinued = getDateFromInput(request, DISCONTINUED);

    int companyId = Integer.valueOf(request.getParameter(COMPANY_ID));
    Company company = CompanyService.getInstance().getCompany(companyId);

    Computer computer = new Computer(-1, computerName, introduced, discontinued, companyId, company.getName());
    Pair<ComputerValidationStatus, String> result = ComputerService.getInstance().createComputer(computer);

    if (result.left == ComputerValidationStatus.OK) {
      ArrayList<Computer> computers = ComputerService.getInstance().getComputers();
      request.setAttribute("computers", computers);
      response.sendRedirect(DASHBOARD);

      //this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);
    } else {
      ArrayList<Company> companies = CompanyService.getInstance().getCompanies();
      request.setAttribute("companies", companies);
      request.setAttribute("error", result.right);
      this.getServletContext().getRequestDispatcher(ADD_COMPUTER).forward(request, response);
    }
  }

  /**
   * Return the date from the user input. Returns null if nothing was entered.
   * @param request HttpServletRequest to get the param
   * @param paramName name of the param you want to check in the request
   * @return the date.
   */
  public Date getDateFromInput(HttpServletRequest request, String paramName) {
    Date date = null;
    String userInput = request.getParameter(paramName);
    if (!userInput.isEmpty()) {
      date = Date.valueOf(request.getParameter(paramName));
    }
    return date;
  }
}
