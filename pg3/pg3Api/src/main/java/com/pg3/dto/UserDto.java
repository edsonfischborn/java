package com.pg3.dto;

import com.pg3.models.User;

import java.util.Date;

public class UserDto {
    private long id;
    private String name;
    private String email;
    private String phone;
    private String cpf;
    private int active;
    private int admin;

    // Date
    private Date createdAt;
    private Date updatedAt;

    public UserDto(User user) {
        setId(user.getId());
        setName(user.getName());
        setEmail(user.getEmail());
        setPhone(user.getPhone());
        setCpf(user.getCpf());
        setActive(user.getActive());
        setAdmin(user.getAdmin());

        // Date
        setUpdatedAt(user.getUpdatedAt());
        setCreatedAt(user.getCreatedAt());
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getActive() {
        return active;
    }

    public void setActive(int active) {
        this.active = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getAdmin() {
        return admin;
    }

    public void setAdmin(int admin) {
        this.admin = admin;
    }
}
