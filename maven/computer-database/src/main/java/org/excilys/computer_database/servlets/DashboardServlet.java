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
import org.excilys.computer_database.util.Util;

@WebServlet("/list-computer")
public class DashboardServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String PAGE = "page";
  private static final String NUMBER_OF_PAGES = "numberOfPages";
  private static final String NUMBER_OF_COMPUTERS = "numberOfComputers";
  private static final String NUMBER_OF_ITEM_PER_PAGE = "numberOfItemPerPage";
  private static final String KEYWORD = "search";
  private static final String DASHBOARD = "/WEB-INF/views/dashboard.jsp";

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    setComputers(request);
    this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);

  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String[] idsToDelete = request.getParameter("selection").split(",");
    for (int i = 0; i < idsToDelete.length; i++) {
      ComputerService.getInstance().deleteComputer(Integer.valueOf(idsToDelete[i]));
    }
    setComputers(request);
    this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);
  }

  /**
   * Put appropriate computers in the params.
   * @param request Request object to get the different parameters
   */
  private void setComputers(HttpServletRequest request) {
    int numberOfItemPerPage = 10;
    if (Util.isInteger(request.getParameter(NUMBER_OF_ITEM_PER_PAGE))) {
      numberOfItemPerPage = Integer.valueOf(request.getParameter(NUMBER_OF_ITEM_PER_PAGE));
    }
    request.setAttribute(NUMBER_OF_ITEM_PER_PAGE, numberOfItemPerPage);
    String keyword = request.getParameter(KEYWORD);
    System.out.println(keyword);
    int numberOfComputers = ComputerService.getInstance().countComputers(keyword);
    request.setAttribute(NUMBER_OF_COMPUTERS, numberOfComputers);

    int numberOfPages = (int) numberOfComputers / numberOfItemPerPage + 1;
    if (numberOfComputers % numberOfItemPerPage == 0) {
      numberOfPages = numberOfComputers / numberOfItemPerPage;
    }
    request.setAttribute(NUMBER_OF_PAGES, numberOfPages);

    int currentPage = 1;
    if (Util.isInteger(request.getParameter(PAGE))) {
      currentPage = Integer.valueOf(request.getParameter(PAGE));
    }
    request.setAttribute("currentPage", currentPage);
    request.setAttribute(KEYWORD, keyword);


    ArrayList<Computer> computersOfPage = ComputerService.getInstance().getComputersWithPageAndSearch(numberOfItemPerPage, currentPage, keyword);
    request.setAttribute("computers", computersOfPage);
  }
}
