package org.excilys.computer_database.dto;

import org.excilys.computer_database.model.Company;

public class CompanyDto {

  private int id;
  private String name;

  /**
   * @param computer Computer you want the DTO from
   */
  public CompanyDto(Company company) {
    this.id = company.getId();
    this.name = company.getName();
  }

  public Company toCompany() {
    Company company = new Company();
    company.setId(this.id);
    company.setName(this.name);
    return company;
  }

  /**
   * Empty constructor.
   */
  public CompanyDto() { }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getShortToString() {
    return this.id + " " + this.name;
  }
  
  @Override
  public String toString() {
    return "ComputerDto [id=" + id + ", name=" + name + "]";
  }

}
