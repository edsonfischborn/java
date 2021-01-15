package com.pg3.dto;

public class UserStatusDto {
    private long allUsers;
    private long activeUsers;
    private long inactiveUsers;

    public long getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(long allUsers) {
        this.allUsers = allUsers;
    }

    public long getActiveUsers() {
        return activeUsers;
    }

    public void setActiveUsers(long activeUsers) {
        this.activeUsers = activeUsers;
    }

    public long getInactiveUsers() {
        return inactiveUsers;
    }

    public void setInactiveUsers(long inactiveUsers) {
        this.inactiveUsers = inactiveUsers;
    }
}
