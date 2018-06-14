package org.excilys.computer_database.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.excilys.computer_database.model.Company;
import org.excilys.computer_database.model.Computer;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class CompanyDaoImpl implements CompanyDao {

  @Autowired
  private SessionFactory sessionFactory;


  @Override
  public ArrayList<Company> getCompanies() {
    Transaction transaction = null;
    ArrayList<Company> companies = new ArrayList<>();
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
      Root<Company> variableRoot = criteria.from(Company.class);
      criteria.select(variableRoot);
      Query<Company> query = session.createQuery(criteria);
      companies = (ArrayList<Company>) query.getResultList();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return companies;
  }

  @Override
  public Optional<Company> getCompany(int id) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
      Root<Company> root = criteria.from(Company.class);
      criteria.select(root).where(builder.equal(root.get("id"), id));
      Query<Company> query = session.createQuery(criteria);
      List<Company> results = query.getResultList();
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
  public int countCompanies() {
    int result = -1;
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Long> criteria = builder.createQuery(Long.class);
      Root<Company> variableRoot = criteria.from(Company.class);
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
  public ArrayList<Company> getCompaniesAtPage(int numberOfItemPerPage, int page) {
    Transaction transaction = null;
    ArrayList<Company> companies = new ArrayList<>();
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();

      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
      Root<Company> variableRoot = criteria.from(Company.class);
      criteria.select(variableRoot);
      Query<Company> query = session.createQuery(criteria);
      int offset = numberOfItemPerPage * (page - 1);
      query.setMaxResults(numberOfItemPerPage);
      query.setFirstResult(offset);
      companies = (ArrayList<Company>) query.getResultList();
      session.close();
    } catch (Exception e) {
      e.printStackTrace();
      if (transaction != null) {
        transaction.rollback();
      }
    }
    return companies;
  }

  @Override
  @Transactional
  public void deleteCompany(int companyId) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder computerCriteriaBuilder = session.getCriteriaBuilder();
      CriteriaDelete<Computer> computerCriteria = computerCriteriaBuilder.createCriteriaDelete(Computer.class);
      Root<Computer> computerRoot = computerCriteria.from(Computer.class);
      computerCriteria.where(computerCriteriaBuilder.equal(computerRoot.get("company"), companyId));
      session.createQuery(computerCriteria).executeUpdate();

      CriteriaBuilder companyCriteriabuilder = session.getCriteriaBuilder();
      CriteriaDelete<Company> companyCriteria = companyCriteriabuilder.createCriteriaDelete(Company.class);
      Root<Company> companyRoot = companyCriteria.from(Company.class);
      companyCriteria.where(companyCriteriabuilder.equal(companyRoot.get("id"), companyId));
      session.createQuery(companyCriteria).executeUpdate();
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
