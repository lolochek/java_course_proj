package controllers;

import db.dao.*;
import db.entity.*;
import request.commands.ConfirmCommands;
import request.controller.BaseRequestController;
import request.tdo.AccountDTO;
import request.tdo.CartElementDTO;
import request.tdo.OrderElementDTO;
import request.tdo.UserOrderDTO;
import request.tdo.constructors.AccountTDOConstructor;
import request.tdo.constructors.OrderDTOConstructor;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class UserOrderController extends BaseRequestController {

    public UserOrderController(BaseRequestController contr) {
        super(contr);
    }

    public void sendOrders() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var orderDAO = new UserOrderDAO();
            var productList = orderDAO.getAll();
            var orderTDOConstructor = new OrderDTOConstructor();

            var listTDO = new ArrayList<UserOrderDTO>();
            for (var product : productList) {
                listTDO.add(orderTDOConstructor.userOrderToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendUserOrders(AccountDTO account) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var orderDAO = new UserOrderDAO();
            var productList = orderDAO.getByAccountId(account.getAccountId());
            var orderTDOConstructor = new OrderDTOConstructor();

            var listTDO = new ArrayList<UserOrderDTO>();
            for (var product : productList) {
                listTDO.add(orderTDOConstructor.userOrderToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendOrderElements() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            UserOrderDTO order = (UserOrderDTO) inputObjectStream.readObject();

            var orderElementDAO = new OrderElementDAO();
            var orderElements = orderElementDAO.getByOrderId(order.getOrderId());

            var orderTDOConstructor = new OrderDTOConstructor();

            var listTDO = new ArrayList<OrderElementDTO>();
            for (var product : orderElements) {
                listTDO.add(orderTDOConstructor.orderElementToDTO(product));
            }
            System.out.println(listTDO);
            outputObjectStream.writeObject(listTDO);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addOrder(AccountDTO accountTDO) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            var listStorageElementTDO = (List<CartElementDTO>) inputObjectStream.readObject();

            BigDecimal totalCost = BigDecimal.valueOf(0.00);
            for (CartElementDTO elementTDO : listStorageElementTDO) {
                totalCost = totalCost.add(elementTDO.getSubtotalCost());
            }
            UserOrderDTO orderTDO = new UserOrderDTO();

            orderTDO.setAccountId(accountTDO.getAccountId());
            orderTDO.setTotalCost(totalCost);
            orderTDO.setOrderCreated(new Date(System.currentTimeMillis()));
            orderTDO.setStatus("Создан");
            orderTDO.setStatusLastUpdatedDate(LocalDateTime.now());

            OrderDTOConstructor orderTDOConstructor = new OrderDTOConstructor();
            AccountTDOConstructor accountTDOConstructor = new AccountTDOConstructor();

            Account account = accountTDOConstructor.dtoToAccount(accountTDO);

            UserOrder newOrder = orderTDOConstructor.dtoToUserOrder(orderTDO, account);
            System.out.println(newOrder);

            UserOrderDAO orderDao = new UserOrderDAO();

            if (orderDao.save(newOrder))
            {
                var listOrder = orderDao.getAll();

                UserOrder trueOrder = new UserOrder();
                trueOrder = listOrder.get(listOrder.size() - 1);

                var listOrderElement = new ArrayList<OrderElement>();

                var storageDao = new GenericDao<>(CartElement.class);
                var storageTDOConstructor = new OrderDTOConstructor();

                for (CartElementDTO storageElementTDO : listStorageElementTDO) {
                    OrderElementDTO orderElementTDO = new OrderElementDTO();

                    var productDAO = new GenericDao<>(Product.class);

                    Product trueFurn = productDAO.getById(storageElementTDO.getProductId());


                    orderElementTDO.setSubtotalCost(storageElementTDO.getSubtotalCost());
                    orderElementTDO.setQuantity(storageElementTDO.getQuantity());
                    orderElementTDO.setProductId(storageElementTDO.getProductId());
                    orderElementTDO.setOrderId(trueOrder.getOrderId());

                    storageDao.remove(storageTDOConstructor.dtoToCartElement(storageElementTDO, account, trueFurn));

                    listOrderElement.add(orderTDOConstructor.dtoToOrderElement(orderElementTDO, trueOrder, trueFurn));
                }

                OrderElementDAO orderElementDao = new OrderElementDAO();

                if (orderElementDao.saveAll(listOrderElement)) {
                    sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
                }
                else {
                    sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
                }
            }
            else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void changeOrderStatus() {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {
            UserOrderDTO order = (UserOrderDTO) inputObjectStream.readObject();
            UserOrderDAO orderDao = new UserOrderDAO();

            UserOrder lastOrder = orderDao.getById(order.getOrderId());
            lastOrder.setStatus(order.getStatus());
            lastOrder.setStatusLastUpdatedDate(LocalDateTime.now());
            if (orderDao.update(lastOrder)) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
            }
            else {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }


    public void makeOrder(AccountDTO account) {
        sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        try {/*
            AccountDAO accountDAO = new AccountDAO();
            ProductDAO productDAO = new ProductDAO();

            List<OrderElementDTO> orderElementsDTO = (List<OrderElementDTO>) inputObjectStream.readObject();

            UserOrderDAO userOrderDAO = new UserOrderDAO();
            OrderElementDAO orderElementDAO = new OrderElementDAO();

            // Создание UserOrder
            UserOrder userOrder = new UserOrder();
            userOrder.setAccount(accountDAO.getById(account.getAccountId()));
            userOrder.setTotalCost(userOrderDTO.getTotalCost());
            userOrder.setOrderCreated(LocalDateTime.now());
            userOrder.setStatus(userOrderDTO.getStatus());
            userOrder.setStatusLastUpdatedDate(userOrderDTO.getStatusLastUpdatedDate());

            // Сохранение заказа
            boolean isOrderSaved = userOrderDAO.save(userOrder);
            if (!isOrderSaved) {
                sendResponse(ConfirmCommands.class, ConfirmCommands.FAILED);
                return;
            }

            // Сохранение элементов заказа
            for (OrderElementDTO orderElementDTO : orderElementsDTO) {
                OrderElement orderElement = new OrderElement();
                orderElement.setUserOrder(userOrder);
                orderElement.setProduct(productDAO.getById(orderElementDTO.getProductId()));
                orderElement.setQuantity(orderElementDTO.getQuantity());
                orderElement.setSubtotalCost(orderElementDTO.getSubtotalCost());

                orderElementDAO.save(orderElement);
            }*/

            sendResponse(ConfirmCommands.class, ConfirmCommands.SUCCESSFULLY);
        } catch (Exception e) {
            e.printStackTrace();
            sendResponse(ConfirmCommands.class, ConfirmCommands.SOMETHINGWRONG);
        }
    }

}
