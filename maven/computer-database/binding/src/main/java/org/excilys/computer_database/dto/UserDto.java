package org.excilys.computer_database.dto;

import org.excilys.computer_database.model.User;

public class UserDto {

  private String username;
  private String password;

  /**
   * @param computer Computer you want the DTO from
   */
  public UserDto(User user) {
    this.username = user.getUsername();
    this.password = user.getPassword();
  }

  public User toUser() {
    User user = new User();
    user.setUsername(this.username);
    user.setPassword(this.password);
    return user;
  }

  /**
   * Empty constructor.
   */
  public UserDto() { }

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

  @Override
  public String toString() {
    return "UserDto [username=" + username + ", password=" + password + "]";
  }

}


