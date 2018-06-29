package org.excilys.computer_database.dao;

public class OrderByParams {

  private String columnToOrder;
  private String ascOrDesc;
  /**
   * Object containing the orderby params.
   * @param columnToOrder Name of the column
   * @param ascOrDesc asc or desc
   */
  public OrderByParams(String columnToOrder, String ascOrDesc) {
    this.setColumnToOrder(columnToOrder);
    this.setAscOrDesc(ascOrDesc);
  }
  public String getColumnToOrder() {
    return columnToOrder;
  }
  public String getAscOrDesc() {
    return ascOrDesc;
  }

  /**
   * Set the column to order.
   * @param columnToOrder The value of the column to order
   */
  public void setColumnToOrder(String columnToOrder) {
    switch (columnToOrder) {
    case "computerName":
      this.columnToOrder = "name";
      break;
    case "introduced":
      this.columnToOrder = "introduced";
      break;
    case "discontinued":
      this.columnToOrder = "discontinued";
      break;
    case "companyName":
      this.columnToOrder = "company";
      break;
    default:
      this.columnToOrder = null;
      break;

    }
  }
  public void setAscOrDesc(String ascOrDesc) {
    this.ascOrDesc = ascOrDesc.toUpperCase();
  }

}
