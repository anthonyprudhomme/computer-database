package org.excilys.computer_database.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;

import org.excilys.computer_database.dto.UserDto;
import org.excilys.computer_database.exceptions.RegistrationFailedException;
import org.excilys.computer_database.model.Role;
import org.excilys.computer_database.model.User;
import org.excilys.computer_database.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/users")
public class LoginController {

  private final Logger logger = LoggerFactory.getLogger(LoginController.class);
  private UserService userService;

  /**
   * Constructor for LoginController.
   * @param userService the userService
   */
  public LoginController(UserService userService) {
    this.userService = userService;
  }

  /**
   * Delete user.
   * @param id to delete
   * @return the status of the request
   */
  @PreAuthorize("hasRole('ADMIN')")
  @DeleteMapping(path = "/{username}")
  public ResponseEntity<Void> deleteUser(@PathVariable("username") String username) {
    logger.debug("Delete User: " + username);
    userService.deleteUser(username);
    return new ResponseEntity<Void>(HttpStatus.OK);
  }

  /**
   * Validate form and save the user.
   * @param userDto that contains data to update
   * @return the status of the request
   */
  @PostMapping
  public ResponseEntity<String> createUser(@RequestBody UserDto userDto) {
    logger.debug("Create User " + userDto.toString());
    User user = userDto.toUser();
    try {
      userService.createUser(user.getUsername(), user.getPassword());
      return new ResponseEntity<>(HttpStatus.CREATED);
    } catch (RegistrationFailedException e) {
      e.printStackTrace();
      return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
    }

  }

  /**
   * Update the user.
   * @param id of the user to update
   * @param userDto that contains data to update
   * @return the status of the request
   */
  @PreAuthorize("hasRole('USER')")
  @PutMapping(path = "/{username}")
  public ResponseEntity<?> updateUser(@PathVariable("username") String username, @RequestBody UserDto userDto) {
    User currentUser = userDto.toUser();
    UserDetails user = userService.loadUserByUsername(username);
    currentUser.setPassword(user.getPassword());
    ArrayList<Role> roles = new ArrayList<>();
    user.getAuthorities().forEach(authority -> roles.add(new Role(authority.getAuthority())));
    currentUser.setRoles(roles);
    userService.updateUser(currentUser);
    return new ResponseEntity<>(HttpStatus.OK);
  }

}