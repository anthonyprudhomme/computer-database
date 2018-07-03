package org.excilys.computer_database.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.excilys.computer_database.dao.UserDao;
import org.excilys.computer_database.exceptions.RegistrationFailedException;
import org.excilys.computer_database.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  private UserDao userDao;
   
  public User createUser(String username, String password) throws RegistrationFailedException {
    Optional<User> user = userDao.createUser(username, password);
    if(user.isPresent()) {
      return user.get();
    } else {
      throw new RegistrationFailedException("An error occured during registration.");
    }
  }

  public Optional<User> getUser(String username) {
    return userDao.getUser(username);
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<User> user = userDao.getUser(username);
    if(user.isPresent()) {
      Set<GrantedAuthority> grantedAuthorities = user.get().getRoles().stream()
          .map(role -> new SimpleGrantedAuthority(role.getRole())).collect(Collectors.toSet());
      System.out.println(grantedAuthorities);
      return new org.springframework.security.core.userdetails.User(user.get().getUsername(), user.get().getPassword(), grantedAuthorities);
    } else {
      throw new UsernameNotFoundException("Username: " + username + " not found.");
    }
  }

  public void deleteUser(String username) {
    userDao.deleteUser(username);
  }

  public void updateUser(User currentUser) {
    userDao.updateUser(currentUser);
  }

}
