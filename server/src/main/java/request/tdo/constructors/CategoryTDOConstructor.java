package request.tdo.constructors;

import db.entity.Category;
import db.entity.Product;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;

import java.util.List;
import java.util.stream.Collectors;

public class CategoryTDOConstructor {

    // Метод для преобразования сущности Category в DTO
    public CategoryDTO categoryToDTO(Category categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setCategoryId(categoryEntity.getCategoryId());
        categoryDTO.setName(categoryEntity.getName());

        return categoryDTO;
    }

    // Метод для преобразования сущности Product в DTO
    public ProductDTO productToDTO(Product productEntity) {
        if (productEntity == null) {
            return null;
        }
        ProductDTO productDTO = new ProductDTO();
        productDTO.setProductId(productEntity.getProductId());
        productDTO.setName(productEntity.getName());
        productDTO.setModel(productEntity.getModel());
        productDTO.setBrand(productEntity.getBrand());
        productDTO.setPrice(productEntity.getPrice());
        productDTO.setAmount(productEntity.getAmount());
        productDTO.setCategoryId(productEntity.getCategory().getCategoryId()); // Устанавливаем categoryId
        return productDTO;
    }

    // Метод для преобразования DTO в сущность Product
    public Product dtoToProduct(ProductDTO productDTO, Category category) {
        if (productDTO == null) {
            return null;
        }
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setName(productDTO.getName());
        product.setModel(productDTO.getModel());
        product.setBrand(productDTO.getBrand());
        product.setPrice(productDTO.getPrice());
        product.setAmount(productDTO.getAmount());
        product.setCategory(category); // Устанавливаем категорию
        return product;
    }

    // Метод для преобразования DTO в сущность Category
    public Category dtoToCategory(CategoryDTO categoryDTO) {
        if (categoryDTO == null) {
            return null;
        }
        Category category = new Category();
        category.setCategoryId(categoryDTO.getCategoryId());
        category.setName(categoryDTO.getName());

        return category;
    }
}