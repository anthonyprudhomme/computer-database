package org.excilys.computer_database.dao;

import java.util.Optional;

import org.excilys.computer_database.model.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {
  
  /**
   * Return the user from the username.
   * @param username the username
   * @return user object
   */
  Optional<User> getUser(String username);


  Optional<User> createUser(String username, String password);


  void deleteUser(String username);


  void updateUser(User currentUser);
}
