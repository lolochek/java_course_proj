package db.entity;

import jakarta.persistence.*;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "cart_element")
public class CartElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_element_id")
    private int cartElementId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "subtotal_cost", nullable = false)
    private BigDecimal subtotalCost;

    // Getters and setters

    public int getCartElementId() {
        return cartElementId;
    }

    public void setCartElementId(int cartElementId) {
        this.cartElementId = cartElementId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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
        return "CartElement{" +
                "cartElementId=" + cartElementId +
                ", account=" + (account != null ? account.getAccountId() : "null") +
                ", product=" + (product != null ? product.getProductId() : "null") +
                ", quantity=" + quantity +
                ", subtotalCost=" + subtotalCost +
                '}';
    }
}

