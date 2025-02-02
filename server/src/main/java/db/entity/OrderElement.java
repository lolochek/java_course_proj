package db.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "order_element")
public class OrderElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_element_id")
    private int orderElementId;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    private UserOrder userOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "subtotal_cost", nullable = false)
    private BigDecimal subtotalCost;

    // Getters and setters

    public int getOrderElementId() {
        return orderElementId;
    }

    public void setOrderElementId(int orderElementId) {
        this.orderElementId = orderElementId;
    }

    public UserOrder getUserOrder() {
        return userOrder;
    }

    public void setUserOrder(UserOrder userOrder) {
        this.userOrder = userOrder;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
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
        return "OrderElement{" +
                "orderElementId=" + orderElementId +
                ", userOrder=" + (userOrder != null ? userOrder.getOrderId() : "null") +
                ", product=" + (product != null ? product.getProductId() : "null") +
                ", quantity=" + quantity +
                ", subtotalCost=" + subtotalCost +
                '}';
    }
}

