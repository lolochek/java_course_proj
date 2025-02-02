package request.tdo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class AccountDTO implements Serializable {
    private int accountId;
    private String username;
    private String passwordHash;
    private int accessLevel;
    private LocalDateTime createdAt;

    // Getters and setters
    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public int getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(int accessLevel) {
        this.accessLevel = accessLevel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "AccountDTO{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", accessLevel=" + accessLevel +
                ", createdAt=" + createdAt +
                '}';
    }
}

