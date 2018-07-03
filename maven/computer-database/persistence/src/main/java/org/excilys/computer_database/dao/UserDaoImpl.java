package org.excilys.computer_database.dao;

import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;

import org.excilys.computer_database.model.Role;
import org.excilys.computer_database.model.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserDaoImpl implements UserDao {
  
  @Autowired
  private SessionFactory sessionFactory;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Optional<User> getUser(String username) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<User> criteria = builder.createQuery(User.class);
      Root<User> root = criteria.from(User.class);
      criteria.select(root).where(builder.equal(root.get("username"), username));
      Query<User> query = session.createQuery(criteria);
      List<User> results = query.getResultList();
      transaction.commit();
      session.close();
      if (results.isEmpty()) {
        return Optional.empty();
      } else {
        return Optional.of(results.get(0));
      }
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
      return Optional.empty();
    }
  }

  @Override
  public Optional<User> createUser(String username, String password) {
    String encryptedPassword = passwordEncoder.encode(password); 
    User user = new User(username, encryptedPassword);
    
    user.addRole(new Role("ROLE_USER"));
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      session.save(user);
      transaction.commit();
      session.close();
      return Optional.of(user);
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
      return Optional.empty();
    }
    
  }

  @Override
  public void deleteUser(String username) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaDelete<User> criteria = builder.createCriteriaDelete(User.class);
      Root<User> root = criteria.from(User.class);
      criteria.where(builder.equal(root.get("username"), username));
      session.createQuery(criteria).executeUpdate();
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

  @Override
  public void updateUser(User user) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaUpdate<User> criteria = builder.createCriteriaUpdate(User.class);
      criteria.from(User.class);
      Root<User> root = criteria.from(User.class);
      criteria.where(builder.equal(root.get("username"), user.getUsername()));
      criteria.set("password", user.getPassword());
      session.createQuery(criteria).executeUpdate();
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
  }

}
