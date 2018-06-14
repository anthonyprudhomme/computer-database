package org.excilys.computer_database.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.excilys.computer_database.model.Computer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ComputerDaoImpl implements ComputerDao {

  @Autowired
  private SessionFactory sessionFactory;

  @Override
  public ArrayList<Computer> getComputers() {
    Transaction transaction = null;
    ArrayList<Computer> computers = new ArrayList<>();
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Computer> criteria = builder.createQuery(Computer.class);
      Root<Computer> variableRoot = criteria.from(Computer.class);
      variableRoot.fetch("company", JoinType.LEFT);
      criteria.select(variableRoot);
      Query<Computer> query = session.createQuery(criteria);
      computers = (ArrayList<Computer>) query.getResultList();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return computers;
  }

  @Override
  public Optional<Computer> getComputerDetails(int id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Computer> criteria = builder.createQuery(Computer.class);
      Root<Computer> root = criteria.from(Computer.class);
      root.fetch("company", JoinType.LEFT);
      criteria.select(root).where(builder.equal(root.get("id"), id));
      Query<Computer> query = session.createQuery(criteria);
      List<Computer> results = query.getResultList();
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
  public void createComputer(Computer computer) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      if (computer.getCompany() == null || computer.getCompany().getId() == -1) {
        computer.setCompany(null);
      }
      session.save(computer);
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
  public void updateComputer(Computer computer) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaUpdate<Computer> criteria = builder.createCriteriaUpdate(Computer.class);
      criteria.from(Computer.class);
      Root<Computer> root = criteria.from(Computer.class);
      criteria.where(builder.equal(root.get("id"), computer.getId()));
      criteria.set("name", computer.getName());
      criteria.set("introduced", computer.getIntroduced());
      criteria.set("discontinued", computer.getDiscontinued());
      if (computer.getCompany() != null && computer.getCompany().getId() != -1) {
        criteria.set("company", computer.getCompany());
      }
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
  public void deleteComputer(int id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaDelete<Computer> criteria = builder.createCriteriaDelete(Computer.class);
      Root<Computer> root = criteria.from(Computer.class);
      criteria.where(builder.equal(root.get("id"), id));
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
  public int countComputers() {
    int result = -1;
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
      Root<Computer> variableRoot = criteria.from(Computer.class);
      criteria.select(builder.count(variableRoot));
      Query<Long> query = session.createQuery(criteria);
      result = Math.toIntExact(query.getSingleResult());
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return result;
  }

  @Override
  public int countComputers(String keyword) {
    if (keyword == null || keyword.isEmpty()) {
      return countComputers();
    }
    int result = -1;
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
      Root<Computer> variableRoot = criteria.from(Computer.class);
      criteria.select(builder.count(variableRoot));
      criteria.where(builder.like(variableRoot.get("computer.name"), "%" + keyword + "%"));
      criteria.where(builder.like(variableRoot.get("company.name"), "%" + keyword + "%"));
      Query<Long> query = session.createQuery(criteria);
      result = Math.toIntExact(query.getSingleResult());
      transaction.commit();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return result;
  }

  @Override
  public ArrayList<Computer> getComputersWithParams(int numberOfItemPerPage, int page, String keyword,
      OrderByParams orderByParams) {
    Transaction transaction = null;
    ArrayList<Computer> computers = new ArrayList<>();
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();

      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Computer> criteria = builder.createQuery(Computer.class);
      Root<Computer> variableRoot = criteria.from(Computer.class);
      variableRoot.fetch("company", JoinType.LEFT);
      criteria.select(variableRoot);
      prepareCriteriaWithOrderByParams(criteria, builder, variableRoot, orderByParams);
      prepareCriteriaWithKeyword(criteria, builder, variableRoot, keyword);
      Query<Computer> query = session.createQuery(criteria);
      int offset = numberOfItemPerPage * (page - 1);
      prepareCriteriaWithPage(query, numberOfItemPerPage, offset);
      computers = (ArrayList<Computer>) query.getResultList();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return computers;
  }
  /**
   * Prepare the criteria depending on the Keyword.
   * @param criteria Criteria object
   * @param builder Builder object
   * @param variableRoot Root Object
   * @param keyword the keyword you are looking for
   */
  private void prepareCriteriaWithKeyword(CriteriaQuery<Computer> criteria, CriteriaBuilder builder,
      Root<Computer> variableRoot, String keyword) {
    if (keyword != null && !keyword.isEmpty()) {
      criteria.where(builder.like(variableRoot.get("computer.name"), "%" + keyword + "%"));
      criteria.where(builder.like(variableRoot.get("company.name"), "%" + keyword + "%"));
    }
  }
  /**
   * Prepare the criteria depending on the page params.
   * @param query The Query object
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param offset offset
   */
  private void prepareCriteriaWithPage(Query<Computer> query, int numberOfItemPerPage, int offset) {
    query.setMaxResults(numberOfItemPerPage);
    query.setFirstResult(offset);
  }

  /**
   * Prepare the criteria depending on the orderByparams.
   * @param criteria Criteria object
   * @param builder Builder object
   * @param variableRoot Root Object
   * @param orderByParams OrderByParams object
   */
  private void prepareCriteriaWithOrderByParams(CriteriaQuery<Computer> criteria, CriteriaBuilder builder,
      Root<Computer> variableRoot, OrderByParams orderByParams) {
    if (orderByParams != null) {
      if (orderByParams.getAscOrDesc().equalsIgnoreCase("asc")) {
        criteria.orderBy(builder.asc(variableRoot.get(orderByParams.getColumnToOrder())));
      } else {
        criteria.orderBy(builder.desc(variableRoot.get(orderByParams.getColumnToOrder())));
      }
    }

  }

  @Override
  public void deleteComputers(int[] ids) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaDelete<Computer> criteria = builder.createCriteriaDelete(Computer.class);
      Root<Computer> root = criteria.from(Computer.class);
      for (int i = 0; i < ids.length; i++) {
        System.out.println(ids[i]);
      }
      List<Integer> integerIds = Arrays.stream(ids).boxed().collect(Collectors.toList());
      criteria.where(root.get("id").in(integerIds));
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
