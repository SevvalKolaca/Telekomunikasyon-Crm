package io.github.ergulberke.event.user;

import java.time.LocalDateTime;

public class UserLoginEvent {
    private String userId;
    private String email;
    private LocalDateTime loginTime;
    private String ipAddress;
    private String deviceInfo;

    public UserLoginEvent() {
    }

    public UserLoginEvent(String userId, String email, LocalDateTime loginTime, String ipAddress, String deviceInfo) {
        this.userId = userId;
        this.email = email;
        this.loginTime = loginTime;
        this.ipAddress = ipAddress;
        this.deviceInfo = deviceInfo;
    }

    // Getters ve Setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(LocalDateTime loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
