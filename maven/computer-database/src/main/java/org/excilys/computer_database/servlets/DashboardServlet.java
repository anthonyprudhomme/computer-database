package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.model.Page;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.util.Util;

@WebServlet("/list-computer")
public class DashboardServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    int numberOfItemPerPage = 10;
    if (Util.isInteger(request.getParameter("numberOfItemPerPage"))) {
      numberOfItemPerPage = Integer.valueOf(request.getParameter("numberOfItemPerPage"));
    }
    request.setAttribute("numberOfItemPerPage", numberOfItemPerPage);
    Page page = new Page(new ArrayList<Object>(ComputerService.getInstance().getComputers()), numberOfItemPerPage);
    int numberOfComputers = page.getNumberOfItems();
    request.setAttribute("numberOfComputers", numberOfComputers);

    int numberOfPages = page.getNumberOfPages();
    request.setAttribute("numberOfPages", numberOfPages);

    int currentPage = 1;
    if (Util.isInteger(request.getParameter("page"))) {
      currentPage = Integer.valueOf(request.getParameter("page"));
    }
    request.setAttribute("currentPage", currentPage);

    ArrayList<Computer> computersOfPage = page.getComputersForPage(currentPage - 1);
    request.setAttribute("computers", computersOfPage);
    this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

  }
}
