package org.excilys.computer_database.model;

import java.sql.Date;

public class Computer {

  private int id;
  private String name;
  private Date introduced;
  private Date discontinued;
  private Company company;

  /**
   * @param id id of the computer
   * @param name name of the computer
   * @param introduced date when the computer was introduced
   * @param discontinued date when the computer was discontinued
   * @param companyId id of the company
   * @param companyName name of the company
   */
  public Computer(int id, String name, Date introduced, Date discontinued, int companyId, String companyName) {
    super();
    this.id = id;
    this.name = name;
    this.introduced = introduced;
    this.discontinued = discontinued;
    this.company = new Company(companyId, companyName);
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Date getIntroduced() {
    return introduced;
  }

  public Date getDiscontinued() {
    return discontinued;
  }

  public Company getCompany() {
    return this.company;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setIntroduced(Date introduced) {
    this.introduced = introduced;
  }

  public void setDiscontinued(Date discontinued) {
    this.discontinued = discontinued;
  }

  public void setCompany(Company company) {
    this.company = company;
  }

  @Override
  public String toString() {
    return "Computer [id=" + id + ", name=" + name + ", introduced=" + introduced + ", discontinued=" + discontinued
        + ", company=" + company.toString() + "]";
  }

  public String getShortToString() {
    return this.id + " " + this.name;
  }




}
