package db.dao;

import db.entity.CartElement;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;


public class CartElementDAO extends GenericDao<CartElement> {
    public CartElementDAO() {
        super(CartElement.class);
    }

    // Метод для получения элементов корзины по accountId
    public List<CartElement> getByAccountId(int accountId) {
        List<CartElement> cartElements = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<CartElement> criteriaQuery = criteriaBuilder.createQuery(entityType);
            Root<CartElement> root = criteriaQuery.from(entityType);

            criteriaQuery.select(root).where(criteriaBuilder.equal(root.get("account").get("accountId"), accountId));

            cartElements = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cartElements;
    }

    public boolean clearByAccountId(int accountId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            // Удаляем все элементы корзины для данного accountId
            int deletedCount = session.createQuery("DELETE FROM CartElement ce WHERE ce.account.accountId = :accountId")
                    .setParameter("accountId", accountId)
                    .executeUpdate();
            transaction.commit();
            return deletedCount > 0; // Возвращаем true, если были удалены элементы
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback(); // Откат транзакции в случае ошибки
            }
            e.printStackTrace();
            return false; // Ошибка при очистке
        }
    }

}

