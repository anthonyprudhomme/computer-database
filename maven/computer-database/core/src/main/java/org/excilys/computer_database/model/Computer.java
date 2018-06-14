package org.excilys.computer_database.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "computer")
public class Computer {

  @Id @GeneratedValue(strategy = javax.persistence.GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  //@Temporal(TemporalType.DATE)
  @Column(name = "introduced")
  private Date introduced;

  //@Temporal(TemporalType.DATE)
  @Column(name = "discontinued")
  private Date discontinued;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "company_id")
  private Company company;

  /**
   * @param id id of the computer
   * @param name name of the computer
   * @param introduced date when the computer was introduced
   * @param discontinued date when the computer was discontinued
   * @param companyId id of the company
   * @param companyName name of the company
   */
  public Computer(int id, String name, Date introduced, Date discontinued, Integer companyId, String companyName) {
    this.id = id;
    this.name = name;
    this.introduced = introduced;
    this.discontinued = discontinued;
    this.company = new Company(companyId, companyName);
  }

  /**
   * Empty constructor for computer.
   */
  public Computer() { }

  public Integer getId() {
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
        + ", company=" + company + "]";
  }

  public String getShortToString() {
    return this.id + " " + this.name;
  }
}
