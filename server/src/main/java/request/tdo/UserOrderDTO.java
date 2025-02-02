package request.tdo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDateTime;

public class UserOrderDTO implements Serializable {
    private int orderId;
    private int accountId;
    private Date orderCreated;
    private BigDecimal totalCost;
    private String status;
    private LocalDateTime statusLastUpdatedDate;

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public Date getOrderCreated() {
        return orderCreated;
    }

    public void setOrderCreated(Date orderCreated) {
        this.orderCreated = orderCreated;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost = totalCost;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getStatusLastUpdatedDate() {
        return statusLastUpdatedDate;
    }

    public void setStatusLastUpdatedDate(LocalDateTime statusLastUpdatedDate) {
        this.statusLastUpdatedDate = statusLastUpdatedDate;
    }

    @Override
    public String toString() {
        return "UserOrderDTO{" +
                "orderId=" + orderId +
                ", accountId=" + accountId +
                ", orderCreated=" + orderCreated +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                ", statusLastUpdatedDate=" + statusLastUpdatedDate +
                '}';
    }
}

