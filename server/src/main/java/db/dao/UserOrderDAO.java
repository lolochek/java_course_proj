package db.dao;

import db.entity.UserOrder;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class UserOrderDAO extends GenericDao<UserOrder> {
    public UserOrderDAO() {
        super(UserOrder.class);
    }

    // Метод для получения заказов по accountId
    public List<UserOrder> getByAccountId(int accountId) {
        List<UserOrder> userOrders = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserOrder> criteriaQuery = criteriaBuilder.createQuery(entityType);
            Root<UserOrder> root = criteriaQuery.from(entityType);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("account").get("accountId"), accountId));

            userOrders = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userOrders;
    }

    // Метод для получения заказа по его ID
    public UserOrder getById(int orderId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(UserOrder.class, orderId);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
