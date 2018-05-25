package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.ComputerService;

@WebServlet("/list-computer")
public class DashboardServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  /**
   * Overload of doGet method.
   */
  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    ArrayList<Computer> computers = ComputerService.getInstance().getComputers();
    request.setAttribute("computers", computers);
    this.getServletContext().getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);

  }
}
