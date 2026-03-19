package com.football.models;

import java.time.LocalDateTime;

public class User {

    public enum UserType { CUSTOMER, ADMIN }
    public enum Status { ACTIVE, BLOCKED }

    private int userId;
    private String username;
    private String email;
    private String passwordHash;
    private UserType userType;
    private Status status;
    private LocalDateTime createdAt;
    private LocalDateTime lastLogin;

    public User() {}

    public User(int userId, String username, String email, String passwordHash,
                UserType userType, Status status, LocalDateTime createdAt, LocalDateTime lastLogin) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.userType = userType;
        this.status = status;
        this.createdAt = createdAt;
        this.lastLogin = lastLogin;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserType getUserType() { return userType; }
    public void setUserType(UserType userType) { this.userType = userType; }

    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", username='" + username + "', userType=" + userType + "}";
    }
}
