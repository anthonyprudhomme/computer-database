package org.excilys.computer_database.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
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

  @Override
  public ArrayList<Company> getCompaniesWithParams(Integer page, Integer numberOfItemPerPage, String keyword,
      String orderBy, String ascOrDesc) {
    Transaction transaction = null;
    ArrayList<Company> companies = new ArrayList<>();
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();

      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaQuery<Company> criteria = builder.createQuery(Company.class);
      Root<Company> companyRoot = criteria.from(Company.class);
      criteria.select(companyRoot);
      prepareCriteriaWithOrderByParams(criteria, builder, companyRoot, orderBy, ascOrDesc);
      prepareCriteriaWithKeyword(criteria, builder, companyRoot, keyword);
      Query<Company> query = session.createQuery(criteria);
      int offset = numberOfItemPerPage * (page - 1);
      prepareCriteriaWithPage(query, numberOfItemPerPage, offset);
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
  /**
   * Prepare the criteria depending on the Keyword.
   * @param criteria Criteria object
   * @param builder Builder object
   * @param variableRoot Root Object
   * @param keyword the keyword you are looking for
   */
  private void prepareCriteriaWithKeyword(CriteriaQuery<Company> criteria, CriteriaBuilder builder,
      Root<Company> companyRoot, String keyword) {
    if (keyword != null && !keyword.isEmpty()) {
      criteria.where(builder.or(builder.like(companyRoot.get("name"), "%" + keyword + "%")));
    }
  }
  /**
   * Prepare the criteria depending on the page params.
   * @param query The Query object
   * @param numberOfItemPerPage numberOfItemPerPage
   * @param offset offset
   */
  private void prepareCriteriaWithPage(Query<Company> query, int numberOfItemPerPage, int offset) {
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
  private void prepareCriteriaWithOrderByParams(CriteriaQuery<Company> criteria, CriteriaBuilder builder,
      Root<Company> variableRoot, String orderBy, String ascOrdDesc) {
    if (orderBy != null) {
      if (ascOrdDesc.equalsIgnoreCase("asc")) {
        if(orderBy.equalsIgnoreCase("company")) {
          criteria.orderBy(builder.asc(variableRoot.get("name")));
        }else {
          criteria.orderBy(builder.asc(variableRoot.get(orderBy)));
        }
      } else {
        if(orderBy.equalsIgnoreCase("company")) {
          criteria.orderBy(builder.desc(variableRoot.get("name")));
        }else {
          criteria.orderBy(builder.desc(variableRoot.get(orderBy)));
        }
      }
    }

  }

  @Override
  public void createCompany(Company company) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      session.save(company);
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
  public void updateCompany(Company company) {
    Transaction transaction = null;
    try (Session session = sessionFactory.getCurrentSession()) {
      transaction = session.beginTransaction();
      CriteriaBuilder builder = session.getCriteriaBuilder();
      CriteriaUpdate<Company> criteria = builder.createCriteriaUpdate(Company.class);
      criteria.from(Company.class);
      Root<Company> root = criteria.from(Company.class);
      criteria.where(builder.equal(root.get("id"), company.getId()));
      criteria.set("name", company.getName());
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
