package org.excilys.computer_database.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


@Entity
@Table(name = "company")
public class Company {

  @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "id")
  private Integer id;

  @Column(name = "name")
  private String name;

  @OneToMany(mappedBy = "company")
  private Set<Computer> computers;

  /**
   * @param id of the company
   * @param name of the company
   */
  public Company(Integer id, String name) {
    this.id = id;
    this.name = name;
  }
  /**
   * Empty constructor for company.
   */
  public Company() { }

  public Integer getId() {
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

  public Set<Computer> getComputers() {
    return computers;
  }

  public void setComputers(Set<Computer> computers) {
    this.computers = computers;
  }
}
