package org.excilys.computer_database.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.excilys.computer_database.dao.OrderByParams;
import org.excilys.computer_database.model.Computer;
import org.excilys.computer_database.service.ComputerService;
import org.excilys.computer_database.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet("/list-computer")
public class DashboardServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private static final String PAGE = "page";
  private static final String NUMBER_OF_PAGES = "numberOfPages";
  private static final String NUMBER_OF_COMPUTERS = "numberOfComputers";
  private static final String NUMBER_OF_ITEM_PER_PAGE = "numberOfItemPerPage";
  private static final String KEYWORD = "search";
  private static final String ORDER_BY = "orderBy";
  private static final String ASC_OR_DESC = "ascOrDesc";
  private static final String DASHBOARD = "/WEB-INF/views/dashboard.jsp";

  @Autowired
  private ComputerService computerService;

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this,
        config.getServletContext());
  }

  @Override
  public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    setComputers(request);
    this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);
  }

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    deleteComputersIfRequired(request);
    setComputers(request);
    this.getServletContext().getRequestDispatcher(DASHBOARD).forward(request, response);
  }

  /**
   * Delete the computers defined by the user.
   * @param request Request containing the ids to delete
   */
  private void deleteComputersIfRequired(HttpServletRequest request) {
    String[] idsToDelete = request.getParameter("selection").split(",");
    for (int i = 0; i < idsToDelete.length; i++) {
      computerService.deleteComputer(Integer.valueOf(idsToDelete[i]));
    }
  }

  /**
   * Put appropriate computers in the params.
   * @param request Request object to get the different parameters
   */
  private void setComputers(HttpServletRequest request) {
    int numberOfItemPerPage = getNumberOfItemPerPage(request);
    String keyword = getKeyword(request);
    int numberOfComputers = getNumberOfComputers(request, keyword);
    setNumberOfPages(request, numberOfComputers, numberOfItemPerPage);
    int currentPage = getCurrentPage(request);
    OrderByParams orderByParams = getOrderByparams(request);
    ArrayList<Computer> computersOfPage = computerService.getComputersWithParams(numberOfItemPerPage, currentPage, keyword, orderByParams);
    request.setAttribute("computers", computersOfPage);
  }

  /**
   * Get the currentPage from the request.
   * @param request to get the params from
   * @return the currentPage from the request
   */
  private int getCurrentPage(HttpServletRequest request) {
    int currentPage = 1;
    if (Util.isInteger(request.getParameter(PAGE))) {
      currentPage = Integer.valueOf(request.getParameter(PAGE));
    }
    request.setAttribute("currentPage", currentPage);
    return currentPage;
  }

  /**
   * Get the numberOfPages from the request.
   * @param request to get the params from
   * @param numberOfComputers number of computers
   * @param numberOfItemPerPage number of item per page
   */
  private void setNumberOfPages(HttpServletRequest request, int numberOfComputers, int numberOfItemPerPage) {
    int numberOfPages = (int) numberOfComputers / numberOfItemPerPage + 1;
    if (numberOfComputers % numberOfItemPerPage == 0) {
      numberOfPages = numberOfComputers / numberOfItemPerPage;
    }
    request.setAttribute(NUMBER_OF_PAGES, numberOfPages);
  }

  /**
   * Get the numberOfComputers from the request.
   * @param request to get the params from
   * @param keyword the keyword to look for
   * @return the numberOfComputers from the request
   */
  private int getNumberOfComputers(HttpServletRequest request, String keyword) {
    int numberOfComputers = computerService.countComputers(keyword);
    request.setAttribute(NUMBER_OF_COMPUTERS, numberOfComputers);
    return numberOfComputers;
  }

  /**
   * Get the numberOfItemPerPage from the request.
   * @param request to get the params from
   * @return the numberOfItemPerPage from the request
   */
  private String getKeyword(HttpServletRequest request) {
    String keyword = request.getParameter(KEYWORD);
    request.setAttribute(KEYWORD, keyword);
    return keyword;
  }

  /**
   * Get the numberOfItemPerPage from the request.
   * @param request to get the params from
   * @return the numberOfItemPerPage from the request
   */
  private int getNumberOfItemPerPage(HttpServletRequest request) {
    int numberOfItemPerPage = 10;
    if (Util.isInteger(request.getParameter(NUMBER_OF_ITEM_PER_PAGE))) {
      numberOfItemPerPage = Integer.valueOf(request.getParameter(NUMBER_OF_ITEM_PER_PAGE));
    }
    request.setAttribute(NUMBER_OF_ITEM_PER_PAGE, numberOfItemPerPage);
    return numberOfItemPerPage;
  }

  /**
   * Get the OderByParams object from the request.
   * @param request to get the params from
   * @return the OderByParams object from the request
   */
  private OrderByParams getOrderByparams(HttpServletRequest request) {
    String orderBy = request.getParameter(ORDER_BY);
    if (orderBy == null || orderBy.isEmpty()) {
      return null;
    }
    String ascOrDesc = request.getParameter(ASC_OR_DESC);
    if (ascOrDesc == null || ascOrDesc.isEmpty()) {
      return null;
    }
    request.setAttribute(ORDER_BY, orderBy);
    request.setAttribute(ASC_OR_DESC, ascOrDesc);
    return new OrderByParams(orderBy, ascOrDesc);
  }
}
