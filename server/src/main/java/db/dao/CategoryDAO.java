package db.dao;

import db.entity.Category;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class CategoryDAO extends GenericDao<Category> {
    public CategoryDAO() {
        super(Category.class);
    }


    // Метод для поиска категории по имени
    public Category getByName(String name) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Category> criteriaQuery = criteriaBuilder.createQuery(Category.class);
            Root<Category> root = criteriaQuery.from(Category.class);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.equal(root.get("name"), name));

            List<Category> results = session.createQuery(criteriaQuery).getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

