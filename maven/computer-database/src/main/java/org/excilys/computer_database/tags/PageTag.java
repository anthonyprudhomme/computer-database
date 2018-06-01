package org.excilys.computer_database.tags;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class PageTag extends SimpleTagSupport {

  private String uri;
  private int currentPage;
  private int numberOfPages;
  private int maxLinks = 10;
  private int numberOfItemPerPage = 10;
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
    JspWriter out = getWriter();

    boolean lastPage = currentPage == numberOfPages;
    int pgStart = Math.max(currentPage - maxLinks / 2, 1);
    int pgEnd = pgStart + maxLinks;
    if (pgEnd > numberOfPages + 1) {
      int diff = pgEnd - numberOfPages;
      pgStart -= diff - 1;
      if (pgStart < 1) {
        pgStart = 1;
      }
      pgEnd = numberOfPages + 1;
    }

    try {
      out.write("<ul class=\"pagination\">");

      if (currentPage > 1) {
        out.write(constructLink(currentPage - 1, "Previous", "paginatorPrev"));
      }

      for (int i = pgStart; i < pgEnd; i++) {
        if (i == currentPage) {
          out.write("<li class=\"active" + (lastPage && i == numberOfPages ? " paginatorLast" : "")  + "\"><a href=\"#\">" + currentPage + "</li>");
        } else {
          out.write(constructLink(i));
        }
      }

      if (!lastPage) {
        out.write(constructLink(currentPage + 1, "Next", "paginatorNext paginatorLast"));
      }

      out.write("</ul>");

    } catch (java.io.IOException ex) {
      throw new JspException("Error in Paginator tag", ex);
    }
  }

  /**
   * Construct the link.
   * @param page wanted
   * @return the link
   */
  private String constructLink(int page) {
    return constructLink(page, String.valueOf(page), null);
  }

  /**
   * Construct the link.
   * @param page wanted
   * @param text param
   * @param className name of the class.
   * @return the link
   */
  private String constructLink(int page, String text, String className) {
    StringBuilder link = new StringBuilder("<li");
    if (className != null) {
      link.append(" class=\"");
      link.append(className);
      link.append("\"");
    }
    link.append(">")
    .append("<a href=\"")
    .append(uri)
    .append("?page=" + String.valueOf(page))
    .append("&numberOfItemPerPage=" + String.valueOf(numberOfItemPerPage))
    .append("&search=" + search)
    .append("&orderBy=" + orderBy)
    .append("&ascOrDesc=" + ascOrDesc)
    .append("\">")
    .append(text)
    .append("</a></li>");
    return link.toString();
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

  public void setNumberOfPages(int totalPages) {
    this.numberOfPages = totalPages;
  }

  public void setMaxLinks(int maxLinks) {
    this.maxLinks = maxLinks;
  }

  public void setNumberOfItemPerPage(int numberOfItemPerPage) {
    this.numberOfItemPerPage = numberOfItemPerPage;
  }

}