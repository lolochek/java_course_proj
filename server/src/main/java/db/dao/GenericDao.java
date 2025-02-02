package db.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import db.connection.DBConnection;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class GenericDao<T> implements IDao<T>{
    protected SessionFactory sessionFactory;
    protected final Class<T> entityType;
    protected CriteriaBuilder cBuilder;
    protected CriteriaQuery<T> criteriaQuery;
    protected Root<T> root;

    public GenericDao(Class<T> entityType){
        sessionFactory = DBConnection.GetSessionFactory();
        this.entityType = entityType;
    }

    @Override
    public List<T> getAll() {
        List<T> entityList= null;
        try (Session session = sessionFactory.openSession();) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);
            criteriaQuery.select(root);
            entityList = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return entityList;
        } 
        return entityList;
    }

    @Override
    public boolean update(T obj) {
        try (Session session = sessionFactory.openSession();) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);

            Transaction sessionTransaction = session.beginTransaction();
            session.merge(obj);
            sessionTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean save(T obj) {
        try (Session session = sessionFactory.openSession();) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);

            Transaction sessionTransaction = session.beginTransaction();
            session.persist(obj);
            sessionTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(T obj) {
        try (Session session = sessionFactory.openSession();) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);

            Transaction sessionTransaction = session.beginTransaction();
            session.remove(obj);
            sessionTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public T getById(int id) {
        try (Session session = sessionFactory.openSession();) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);
            T obj = session.get(entityType, id);
            return obj;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String getPassword(int id) {
        try (Session session = sessionFactory.openSession();) {
            T obj = session.get(entityType, id);
            if (obj != null) {
                return (String) obj.getClass().getMethod("getPassword").invoke(obj);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
