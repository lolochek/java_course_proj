package db.dao;

import db.entity.UserInfo;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class UserInfoDAO extends GenericDao<UserInfo> {
    public UserInfoDAO() {
        super(UserInfo.class);
    }

    public UserInfo findByAccountId(int accountId) {
        UserInfo userInfo = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<UserInfo> criteriaQuery = cBuilder.createQuery(entityType);
            Root<UserInfo> root = criteriaQuery.from(entityType);

            criteriaQuery.select(root).where(cBuilder.equal(root.get("account").get("accountId"), accountId));

            userInfo = session.createQuery(criteriaQuery).uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userInfo;
    }
}

