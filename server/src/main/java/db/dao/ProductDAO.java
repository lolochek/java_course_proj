package db.dao;

import db.entity.Product;
import org.hibernate.Session;
import org.hibernate.Transaction;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class ProductDAO extends GenericDao<Product> {
    public ProductDAO() {
        super(Product.class);
    }

    // Метод для поиска продуктов по категории
    public List<Product> findByCategoryId(int categoryId) {
        List<Product> products = null;
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder cBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = cBuilder.createQuery(entityType);
            Root<Product> root = criteriaQuery.from(entityType);

            criteriaQuery.select(root).where(cBuilder.equal(root.get("category").get("categoryId"), categoryId));

            products = session.createQuery(criteriaQuery).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return products;
    }

    // Метод для поиска продукта по имени, модели и бренду
    public Product getByNameAndModelAndBrand(String name, String model, String brand) {
        try (Session session = sessionFactory.openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Product> criteriaQuery = criteriaBuilder.createQuery(Product.class);
            Root<Product> root = criteriaQuery.from(Product.class);

            criteriaQuery.select(root)
                    .where(criteriaBuilder.and(
                            criteriaBuilder.equal(root.get("name"), name),
                            criteriaBuilder.equal(root.get("model"), model),
                            criteriaBuilder.equal(root.get("brand"), brand)
                    ));

            List<Product> results = session.createQuery(criteriaQuery).getResultList();
            return results.isEmpty() ? null : results.get(0); // Возвращаем первый найденный товар или null
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
