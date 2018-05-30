package org.excilys.computer_database.servlets;

import java.io.IOException;
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
import org.excilys.computer_database.util.Util;
import org.excilys.computer_database.validation.ComputerValidationStatus;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;

@WebServlet("/edit-computer")
public class EditComputerServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static final String EDIT_COMPUTER = "/WEB-INF/views/editComputer.jsp";
  public static final String DASHBOARD = "list-computer";

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int id = Integer.valueOf(request.getParameter("id"));
    Computer computer = ComputerService.getInstance().getComputerDetails(id);
    request.setAttribute("computer", computer);
    ArrayList<Company> companies = CompanyService.getInstance().getCompanies();
    request.setAttribute("companies", companies);
    this.getServletContext().getRequestDispatcher(EDIT_COMPUTER).forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Computer computer = Util.getComputerFromRequest(request);
    Pair<ComputerValidationStatus, String> result = ComputerService.getInstance().updateComputer(computer);

    if (result.left == ComputerValidationStatus.OK) {
      ArrayList<Computer> computers = ComputerService.getInstance().getComputers();
      request.setAttribute("computers", computers);
      response.sendRedirect(DASHBOARD);

      //this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);
    } else {
      ArrayList<Company> companies = CompanyService.getInstance().getCompanies();
      request.setAttribute("companies", companies);
      request.setAttribute("error", result.right);
      this.getServletContext().getRequestDispatcher(EDIT_COMPUTER).forward(request, response);
    }
  }
}
