package org.excilys.computer_database.model;

public class Company {

  private int id;
  private String name;

  /**
   * @param id of the company
   * @param name of the company
   */
  public Company(int id, String name) {
    super();
    this.id = id;
    this.name = name;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Company [id=" + id + ", name=" + name + "]";
  }



}
