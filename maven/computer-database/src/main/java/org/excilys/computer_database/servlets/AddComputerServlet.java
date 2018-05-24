package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.service.CompanyService;

@WebServlet("/add-computer")
public class AddComputerServlet extends HttpServlet {

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ArrayList<Company> companies = CompanyService.getInstance().getCompanies();
    request.setAttribute("companies", companies);
    this.getServletContext().getRequestDispatcher("/WEB-INF/views/addComputer.jsp").forward(request, response);

  }
}
