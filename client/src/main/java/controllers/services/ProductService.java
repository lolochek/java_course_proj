package controllers.services;

import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.CartElementDTO;
import request.tdo.CategoryDTO;
import request.tdo.ProductDTO;
import request.tdo.UserOrderDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class ProductService {

    // admin

    public static List<ProductDTO> getProducts() {
        return getData(AdminCommands.class, AdminCommands.GET_PRODUCT_LIST);
    }

    public static List<CategoryDTO> getCategories() {
        return getData(AdminCommands.class, AdminCommands.GET_CATEGORY_LIST);
    }

    public static List<UserOrderDTO> getOrders() {
        return getData(AdminCommands.class, AdminCommands.GET_ORDER_LIST);
    }


    // user

    public static List<ProductDTO> getUserProducts() {
        return getData(UserCommands.class, UserCommands.GET_PRODUCT_LIST);
    }

    public static List<CategoryDTO> getUserCategories() {
        return getData(UserCommands.class, UserCommands.GET_CATEGORY_LIST);
    }

    public static List<CartElementDTO> getCartElements() {
        return getData(UserCommands.class, UserCommands.GET_CART);
    }

    public static List<UserOrderDTO> getUserOrders() {
        return getData(UserCommands.class, UserCommands.GET_USER_ORDER_LIST);
    }

    @SuppressWarnings("unchecked")
    private static <T, C> List<T> getData(Class<C> commandClass, C command) {
        BaseRequestController.sendRequest(commandClass, command);
        ConfirmCommands confirmCommand;
        try {
            confirmCommand = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(confirmCommand) == ConfirmCommands.SUCCESSFULLY) {
                return (List<T>) BaseRequestController.getObjectInputStream().readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ProductDTO getProductById(int productId) {
        List<ProductDTO> products = getUserProducts();
        if (products != null) {
            for (ProductDTO product : products) {
                if (product.getProductId() == productId) {
                    return product;
                }
            }
        }
        return null;
    }

}

