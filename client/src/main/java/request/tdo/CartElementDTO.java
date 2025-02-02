package request.tdo;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartElementDTO implements Serializable {
    private int cartElementId;
    private int accountId;
    private int productId;
    private int quantity;
    private BigDecimal subtotalCost;

    // Getters and setters
    public int getCartElementId() {
        return cartElementId;
    }

    public void setCartElementId(int cartElementId) {
        this.cartElementId = cartElementId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
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
        return "CartElementDTO{" +
                "cartElementId=" + cartElementId +
                ", accountId=" + accountId +
                ", productId=" + productId +
                ", quantity=" + quantity +
                ", subtotalCost=" + subtotalCost +
                '}';
    }
}
