package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.persistence.JdbcConnection;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet("/add-computer")
public class AddComputerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;
  public static final String COMPUTER_NAME = "computerName";
  public static final String INTRODUCED = "introduced";
  public static final String DISCONTINUED = "discontinued";
  public static final String COMPANY_ID = "companyId";

  public static final String ADD_COMPUTER = "/WEB-INF/views/addComputer.jsp";
  public static final String DASHBOARD = "list-computer";

  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);

  @Autowired
  private CompanyService companyService;
  @Autowired
  private ComputerService computerService;

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ArrayList<Company> companies = companyService.getCompanies();
    request.setAttribute("companies", companies);
    this.getServletContext().getRequestDispatcher(ADD_COMPUTER).forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Computer computer = Util.getComputerFromRequest(request);
    try {
      computerService.createComputer(computer);
      ArrayList<Computer> computers = computerService.getComputers();
      request.setAttribute("computers", computers);
      response.sendRedirect(DASHBOARD);
    } catch (CDBObjectException exception) {
      ArrayList<Company> companies = companyService.getCompanies();
      request.setAttribute("companies", companies);
      request.setAttribute("error", exception.getMessage());
      LOGGER.error("Error when creating a computer - " + exception.getMessage() + " " + computer.toString());
      this.getServletContext().getRequestDispatcher(ADD_COMPUTER).forward(request, response);
    }
  }
}
