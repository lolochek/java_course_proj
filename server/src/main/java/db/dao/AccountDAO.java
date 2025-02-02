package db.dao;

import db.entity.Account;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class AccountDAO extends GenericDao<Account> {
    public AccountDAO() {
        super(Account.class);
    }

    public boolean checkLogin(String username, String password) {
        Account account = findByUsername(username);
        return account != null && account.getPasswordHash().equals(password);
    }

    // метод для поиска аккаунта по username
    public Account findByUsername(String username) {
        Account account = null;
        try (Session session = sessionFactory.openSession()) {
            cBuilder = session.getCriteriaBuilder();
            criteriaQuery = cBuilder.createQuery(entityType);
            root = criteriaQuery.from(entityType);
            criteriaQuery.select(root).where(cBuilder.equal(root.get("username"), username));
            account = session.createQuery(criteriaQuery).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return account;
    }
}
