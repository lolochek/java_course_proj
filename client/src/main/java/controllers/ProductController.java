package controllers;

import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProductController {

    public static List<ProductDTO> getProducts() {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_PRODUCT_LIST);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                return (List<ProductDTO>) BaseRequestController.getObjectInputStream().readObject();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<CategoryDTO> getCategories() {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_CATEGORY_LIST);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                return (List<CategoryDTO>) BaseRequestController.getObjectInputStream().readObject();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands add_product(ProductDTO product, CategoryDTO category) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.ADD_PRODUCT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(product);
                BaseRequestController.getObjectOutputStream().writeObject(category);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands add_category(String name) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.ADD_CATEGORY);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(name);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static ConfirmCommands delete_category(String name) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.DELETE_CATEGORY);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(name);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands deleteProduct(ProductDTO product) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.DELETE_PRODUCT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                // Отправляем productId для удаления
                BaseRequestController.getObjectOutputStream().writeObject(product.getProductId());
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands update_product(ProductDTO product) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.UPDATE_PRODUCT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                BaseRequestController.getObjectOutputStream().writeObject(product);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
