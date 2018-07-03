package org.excilys.computer_database.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
public class Role {

  @Id
  @Column(name = "role")
  private String role;

  /**
   * Empty constructor for Role.
   */
  public Role(String role) {
    this.role = role;
  }
  
  public Role() {}
  
  public String getRole() {
    return role;
  }
  
  public void setRole(String role) {
    this.role = role;
  }
  
  @Override
  public String toString() {
    return "Role [ role=" + role + "]";
  }
  
}


