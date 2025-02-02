package controllers;

import request.commands.AdminCommands;
import request.commands.ConfirmCommands;
import request.commands.UserCommands;
import request.controller.BaseRequestController;
import request.tdo.CartElementDTO;
import request.tdo.OrderElementDTO;
import request.tdo.UserOrderDTO;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class OrderController {


    public static ConfirmCommands addOrder(List<CartElementDTO> orderElements)
    {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.CREATE_ORDER);

        ConfirmCommands confirm = null;
        try {
            confirm = BaseRequestController.getCommand(ConfirmCommands.class);
        } catch (Exception e) {
            return ConfirmCommands.FAILED;
        }
        switch (confirm) {
            case SUCCESSFULLY: {
                try{
                    BaseRequestController.getObjectOutputStream().writeObject(orderElements);
                    confirm = BaseRequestController.getCommand(ConfirmCommands.class);
                    return confirm;

                }catch (Exception e) {
                    return ConfirmCommands.FAILED;
                }
            }
        }
        return confirm;
    }

    public static ConfirmCommands makeOrder(UserOrderDTO userOrderDTO, List<OrderElementDTO> orderElements) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.CREATE_ORDER);
        ConfirmCommands command;
        try {
            BaseRequestController.getObjectOutputStream().writeObject(userOrderDTO);
            BaseRequestController.getObjectOutputStream().writeObject(orderElements);

            command = BaseRequestController.getCommand(ConfirmCommands.class);
            return command;
        } catch (Exception e) {
            e.printStackTrace();
            return ConfirmCommands.FAILED;
        }
    }

    public static List<OrderElementDTO> showOrderDetails(UserOrderDTO userOrderDTO) {
        BaseRequestController.sendRequest(UserCommands.class, UserCommands.GET_ORDER_ELEMENTS);
        ConfirmCommands confirmCommand;
        try {
            confirmCommand = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(confirmCommand) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(userOrderDTO);
                return (List<OrderElementDTO>) BaseRequestController.getObjectInputStream().readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static List<OrderElementDTO> showUserOrderDetails(UserOrderDTO userOrderDTO) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.GET_ORDER_ELEMENTS);
        ConfirmCommands confirmCommand;
        try {
            confirmCommand = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(confirmCommand) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(userOrderDTO);
                return (List<OrderElementDTO>) BaseRequestController.getObjectInputStream().readObject();
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ConfirmCommands changeOrderStatus(UserOrderDTO userOrderDTO) {
        BaseRequestController.sendRequest(AdminCommands.class, AdminCommands.CHANGE_ORDER_STATUS);
        ConfirmCommands confirmCommand;
        try {
            confirmCommand = BaseRequestController.getCommand(ConfirmCommands.class);
            if (Objects.requireNonNull(confirmCommand) == ConfirmCommands.SUCCESSFULLY) {

                BaseRequestController.getObjectOutputStream().writeObject(userOrderDTO);

                return BaseRequestController.getCommand(ConfirmCommands.class);
            }
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

