package db.dao;

import db.entity.OrderElement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class OrderElementDAO extends GenericDao<OrderElement> {
    public OrderElementDAO() {
        super(OrderElement.class);
    }

    // Метод для получения элементов заказа по orderId
    public List<OrderElement> getByOrderId(int orderId) {
        List<OrderElement> orderElements = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<OrderElement> criteriaQuery = criteriaBuilder.createQuery(entityType);
            Root<OrderElement> root = criteriaQuery.from(entityType);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("userOrder").get("orderId"), orderId));

            orderElements = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return orderElements;
    }


    public boolean saveAll(ArrayList<OrderElement> listOrderElement) {
        try (Session session = sessionFactory.openSession()) {
            Transaction sessionTransaction = session.beginTransaction();

            int batchSize = 50;
            int count = 0;

            for (OrderElement obj : listOrderElement) {
                session.persist(obj);
                count++;
                if (count % batchSize == 0) {
                    session.flush();
                    session.clear();
                }
            }

            sessionTransaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
