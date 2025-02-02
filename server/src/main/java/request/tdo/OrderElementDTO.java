package request.tdo;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderElementDTO implements Serializable {
    private int orderElementId;
    private int orderId;
    private int productId;
    private int quantity;
    private BigDecimal subtotalCost;

    // Getters and setters
    public int getOrderElementId() {
        return orderElementId;
    }

    public void setOrderElementId(int orderElementId) {
        this.orderElementId = orderElementId;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotalCost() {
        return subtotalCost;
    }

    public void setSubtotalCost(BigDecimal subtotalCost) {
        this.subtotalCost = subtotalCost;
    }

    @Override
    public String toString() {
        return "OrderElementDTO{" +
                "orderElementId=" + orderElementId +
                ", orderId=" + orderId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", subtotalCost=" + subtotalCost +
                '}';
    }
}


