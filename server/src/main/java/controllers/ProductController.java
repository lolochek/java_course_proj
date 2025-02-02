package controllers;

import db.dao.CategoryDAO;
import db.dao.GenericDao;
import db.dao.ProductDAO;
import db.entity.Category;
import db.entity.Product;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;
import request.tdo.constructors.CategoryTDOConstructor;

import java.util.ArrayList;

public class ProductController extends BaseRequestController {

    public ProductController(BaseRequestController contr) {
        super(contr);
    }

    public void sendProductList() {
        System.out.println("get_product_list");
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var productDAO = new GenericDao<>(Product.class);
            var productList = productDAO.getAll();
            var categoryTDOConstructor = new CategoryTDOConstructor();

            var listTDO = new ArrayList<ProductDTO>();
            for (var product : productList) {
                listTDO.add(categoryTDOConstructor.productToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendCategoryList() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var categoryDAO = new GenericDao<>(Category.class);
            var categoryList = categoryDAO.getAll();
            var categoryTDOConstructor = new CategoryTDOConstructor();

            var listTDO = new ArrayList<CategoryDTO>();
            for (var product : categoryList) {
                listTDO.add(categoryTDOConstructor.categoryToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add_product() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            ProductDTO newProduct = (ProductDTO) inputObjectStream.readObject();
            CategoryDTO categoryDTO = (CategoryDTO) inputObjectStream.readObject();
            System.out.println(newProduct);
            System.out.println(categoryDTO);

            CategoryTDOConstructor categoryTDOConstructor = new CategoryTDOConstructor();
            Category category = categoryTDOConstructor.dtoToCategory(categoryDTO);
            Product product = categoryTDOConstructor.dtoToProduct(newProduct, category);

            var productDAO = new ProductDAO();
            Product existingProduct = productDAO.getByNameAndModelAndBrand(newProduct.getName(), newProduct.getModel(), newProduct.getBrand());
            if (existingProduct != null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            boolean isSaved = productDAO.save(product);
            if (isSaved) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void add_category() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            String categoryName = (String) inputObjectStream.readObject();

            CategoryDAO categoryDAO = new CategoryDAO();
            Category existingCategory = categoryDAO.getByName(categoryName);

            if (existingCategory != null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            Category category = new Category();
            category.setName(categoryName);

            boolean isSaved = categoryDAO.save(category);
            if (isSaved) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void delete_category() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            String categoryName = (String) inputObjectStream.readObject();

            CategoryDAO categoryDAO = new CategoryDAO();
            Category existingCategory = categoryDAO.getByName(categoryName);

            if (existingCategory == null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            boolean isDeleted = categoryDAO.remove(existingCategory);
            if (isDeleted) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void del_product() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            int productId = (int) inputObjectStream.readObject();

            ProductDAO productDAO = new ProductDAO();
            Product existingProduct = productDAO.getById(productId);

            if (existingProduct == null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }
            boolean isDeleted = productDAO.remove(existingProduct);
            if (isDeleted) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

    public void update_product() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            ProductDTO updatedProduct = (ProductDTO) inputObjectStream.readObject();



            CategoryDAO categoryDAO = new CategoryDAO();
            Category existingCategory = categoryDAO.getById(updatedProduct.getCategoryId());

            ProductDAO productDAO = new ProductDAO();
            Product existingProduct = productDAO.getById(updatedProduct.getProductId());

            if (existingProduct == null) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            existingProduct.setName(updatedProduct.getName());
            existingProduct.setModel(updatedProduct.getModel());
            existingProduct.setBrand(updatedProduct.getBrand());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setAmount(updatedProduct.getAmount());
            existingProduct.setCategory(existingCategory);

            boolean isUpdated = productDAO.update(existingProduct);
            if (isUpdated) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            } else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }


}
