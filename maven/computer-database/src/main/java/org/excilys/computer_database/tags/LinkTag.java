package org.excilys.computer_database.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.excilys.computer_database.persistence.JdbcConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinkTag extends SimpleTagSupport {

  private static final Logger LOGGER = LoggerFactory.getLogger(JdbcConnection.class);
  private String uri;
  private int currentPage;
  private int numberOfItemPerPage;
  private String search;
  private String orderBy;
  private String ascOrDesc;

  /**
   * Accessor for writer.
   * @return the writer
   */
  private JspWriter getWriter() {
    JspWriter out = getJspContext().getOut();
    return out;
  }

  @Override
  public void doTag() throws JspException {
    JspWriter writer = getWriter();
    StringBuilder link = new StringBuilder("");
    try {
      link.append(uri)
      .append("?page=" + String.valueOf(currentPage))
      .append("&numberOfItemPerPage=" + String.valueOf(numberOfItemPerPage))
      .append("&search=" + search)
      .append("&orderBy=" + orderBy)
      .append("&ascOrDesc=" + ascOrDesc);
      writer.print(link);
    } catch (IOException e) {
        LOGGER.debug("Link Tag:" + e.getMessage());
    }

  }

  public void setUri(String uri) {
    this.uri = uri;
  }

  public void setSearch(String search) {
    this.search = search;
  }

  public void setOrderBy(String orderBy) {
    this.orderBy = orderBy;
  }

  public void setAscOrDesc(String ascOrDesc) {
    this.ascOrDesc = ascOrDesc;
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
  }

  public void setNumberOfItemPerPage(int numberOfItemPerPage) {
    this.numberOfItemPerPage = numberOfItemPerPage;
  }

}