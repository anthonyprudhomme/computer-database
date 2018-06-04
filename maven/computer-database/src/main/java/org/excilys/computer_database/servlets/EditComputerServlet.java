package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.exceptions.CDBObjectException;
import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.CompanyService;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/edit-computer")
public class EditComputerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static final String EDIT_COMPUTER = "/WEB-INF/views/editComputer.jsp";
  public static final String DASHBOARD = "list-computer";

  private static final Logger LOGGER = LoggerFactory.getLogger(EditComputerServlet.class);

  @Autowired
  private ComputerService computerService;
  @Autowired
  private CompanyService companyService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        config.getServletContext());
  }

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int id = Integer.valueOf(request.getParameter("id"));
    Computer computer = computerService.getComputerDetails(id);
    request.setAttribute("computer", computer);
    ArrayList<Company> companies = companyService.getCompanies();
    request.setAttribute("companies", companies);
    this.getServletContext().getRequestDispatcher(EDIT_COMPUTER).forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Computer computer = Util.getComputerFromRequest(request, companyService);

    try {
      computerService.updateComputer(computer);
      ArrayList<Computer> computers = computerService.getComputers();
      request.setAttribute("computers", computers);
      response.sendRedirect(DASHBOARD);
    } catch (CDBObjectException exception) {
      ArrayList<Company> companies = companyService.getCompanies();
      request.setAttribute("companies", companies);
      request.setAttribute("error", exception.getMessage());
      LOGGER.error("Error when editing a computer - " + exception.getMessage() + " " + computer.toString());
      this.getServletContext().getRequestDispatcher(EDIT_COMPUTER).forward(request, response);
    }
  }
}
