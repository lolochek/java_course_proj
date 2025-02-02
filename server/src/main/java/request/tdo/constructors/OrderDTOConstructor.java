package request.tdo.constructors;

import db.entity.*;
import request.tdo.CartElementDTO;
import request.tdo.OrderElementDTO;
import request.tdo.ProductDTO;
import request.tdo.UserOrderDTO;

import java.util.List;

public class OrderDTOConstructor {

    // Метод для преобразования сущности UserOrder в DTO
    public UserOrderDTO userOrderToDTO(UserOrder userOrderEntity) {
        if (userOrderEntity == null) {
            return null;
        }
        UserOrderDTO userOrderDTO = new UserOrderDTO();
        userOrderDTO.setOrderId(userOrderEntity.getOrderId());
        userOrderDTO.setAccountId(userOrderEntity.getAccount().getAccountId());
        userOrderDTO.setOrderCreated(userOrderEntity.getOrderCreated());
        userOrderDTO.setTotalCost(userOrderEntity.getTotalCost());
        userOrderDTO.setStatus(userOrderEntity.getStatus());
        userOrderDTO.setStatusLastUpdatedDate(userOrderEntity.getStatusLastUpdatedDate());

        return userOrderDTO;
    }

    // Метод для преобразования сущности OrderElement в DTO
    public OrderElementDTO orderElementToDTO(OrderElement orderElementEntity) {
        if (orderElementEntity == null) {
            return null;
        }
        OrderElementDTO orderElementDTO = new OrderElementDTO();
        orderElementDTO.setOrderElementId(orderElementEntity.getOrderElementId());
        orderElementDTO.setOrderId(orderElementEntity.getUserOrder().getOrderId());
        orderElementDTO.setProductId(orderElementEntity.getProduct().getProductId());
        orderElementDTO.setQuantity(orderElementEntity.getQuantity());
        orderElementDTO.setSubtotalCost(orderElementEntity.getSubtotalCost());

        return orderElementDTO;
    }

    // Метод для преобразования сущности CartElement в DTO
    public CartElementDTO cartElementToDTO(CartElement cartElementEntity) {
        if (cartElementEntity == null) {
            return null;
        }
        CartElementDTO cartElementDTO = new CartElementDTO();
        cartElementDTO.setCartElementId(cartElementEntity.getCartElementId());
        cartElementDTO.setAccountId(cartElementEntity.getAccount().getAccountId());
        cartElementDTO.setProductId(cartElementEntity.getProduct().getProductId());
        cartElementDTO.setQuantity(cartElementEntity.getQuantity());
        cartElementDTO.setSubtotalCost(cartElementEntity.getSubtotalCost());

        return cartElementDTO;
    }

    // Метод для преобразования DTO в сущность UserOrder
    public UserOrder dtoToUserOrder(UserOrderDTO userOrderDTO, Account account) {
        if (userOrderDTO == null) {
            return null;
        }
        UserOrder userOrder = new UserOrder();
        userOrder.setOrderId(userOrderDTO.getOrderId());
        userOrder.setOrderCreated(userOrderDTO.getOrderCreated());
        userOrder.setTotalCost(userOrderDTO.getTotalCost());
        userOrder.setStatus(userOrderDTO.getStatus());
        userOrder.setStatusLastUpdatedDate(userOrderDTO.getStatusLastUpdatedDate());
        userOrder.setAccount(account);
        return userOrder;
    }

    // Метод для преобразования DTO в сущность OrderElement
    public OrderElement dtoToOrderElement(OrderElementDTO orderElementDTO, UserOrder userOrder, Product product) {
        if (orderElementDTO == null) {
            return null;
        }
        OrderElement orderElement = new OrderElement();
        orderElement.setOrderElementId(orderElementDTO.getOrderElementId());
        orderElement.setUserOrder(userOrder);
        orderElement.setQuantity(orderElementDTO.getQuantity());
        orderElement.setSubtotalCost(orderElementDTO.getSubtotalCost());
        orderElement.setProduct(product);
        return orderElement;
    }

    // Метод для преобразования DTO в сущность CartElement
    public CartElement dtoToCartElement(CartElementDTO cartElementDTO, Account account, Product product) {
        if (cartElementDTO == null) {
            return null;
        }
        CartElement cartElement = new CartElement();
        cartElement.setCartElementId(cartElementDTO.getCartElementId());
        cartElement.setQuantity(cartElementDTO.getQuantity());
        cartElement.setSubtotalCost(cartElementDTO.getSubtotalCost());
        cartElement.setAccount(account);
        cartElement.setProduct(product);
        return cartElement;
    }
}
