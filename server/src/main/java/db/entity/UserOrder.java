package db.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Entity
@Table(name = "user_order")
public class UserOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "order_created", nullable = false, updatable = false)
    private Date orderCreated;

    @Column(name = "total_cost", nullable = false)
    private BigDecimal totalCost;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "status_last_updated_date", nullable = false)
    private LocalDateTime statusLastUpdatedDate;

    @OneToMany(mappedBy = "userOrder", cascade = CascadeType.ALL)
    private List<OrderElement> orderElements;

    // Getters and setters

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
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

    public List<OrderElement> getOrderElements() {
        return orderElements;
    }

    public void setOrderElements(List<OrderElement> orderElements) {
        this.orderElements = orderElements;
    }

    @Override
    public String toString() {
        return "UserOrder{" +
                "orderId=" + orderId +
                ", account=" + (account != null ? account.getAccountId() : "null") +
                ", orderCreated=" + orderCreated +
                ", totalCost=" + totalCost +
                ", status='" + status + '\'' +
                ", statusLastUpdatedDate=" + statusLastUpdatedDate +
                '}';
    }
}
