package org.excilys.computer_database.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "user")
public class User {

  @Id 
  @Column(name = "username")
  private String username;

  @Column(name = "password")
  private String password;
  
  @ManyToMany(fetch=FetchType.EAGER)
  @JoinTable(
      name = "user_role", 
      joinColumns = { @JoinColumn(name = "user_username") }, 
      inverseJoinColumns = { @JoinColumn(name = "role_role") }
  )
  private Collection<Role> roles;

  /**
   * @param id of the user
   * @param username of the user
   * @param password of the user
   */
  public User(String username, String password) {
    this.username = username;
    this.password = password;
    this.roles = new ArrayList<Role>();
  }
  /**
   * Empty constructor for User.
   */
  public User() { 
    this.roles = new ArrayList<Role>();
  }
  
  public String getUsername() {
    return username;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }
  
  public void setPassword(String password) {
    this.password = password;
  }
  
  public Collection<Role> getRoles() {
    return roles;
  }

  public void setRoles(ArrayList<Role> roles) {
    this.roles = roles;
  }
  
  public void addRole(Role role) {
    this.roles.add(role);
  }
  
  @Override
  public String toString() {
    return "User [username=" + username + ", password=" + password + "]";
  }
  
}

