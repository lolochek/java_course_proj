package controllers;

import request.commands.ConfirmCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.CartElementDTO;

import java.util.List;
import java.util.Objects;

public class CartController {

    public static ConfirmCommands addToCart(CartElementDTO cartElement) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.ADD_CART_ELEMENT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                // Отправляем объект CartElementDTO на сервер
                BaseRequestController.getObjectOutputStream().writeObject(cartElement);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands updateCartElement(CartElementDTO cartElement) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.CART_ELEMENT_CHANGE_AMOUNT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                BaseRequestController.getObjectOutputStream().writeObject(cartElement);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands removeCartElement(CartElementDTO cartElement) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.REMOVE_CART_ELEMENT);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                BaseRequestController.getObjectOutputStream().writeObject(cartElement);
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands clearCart(AccountDTO accountDTO) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.CLEAR_CART);
        ConfirmCommands command;
        try {
            command = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(command) == ConfirmCommands.SUCCESSFULLY) {
                BaseRequestController.getObjectOutputStream().writeObject(accountDTO.getAccountId());
                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
