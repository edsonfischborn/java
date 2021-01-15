package com.pg3.models;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=7, max=50)
    @Pattern(regexp = "(?=^.{2,60}$)^[A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ | a-zàáâãèéêìíóôõùúç][a-zàáâãèéêìíóôõùúç | A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ]+(?:[ ](?:das?|dos?|de|e|[A-Z][a-z]+))*$", message="Name does not must have invalid characters")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "(\\d){11}", message="Phone does not must have invalid characters")
    private String phone;

    @Size(min = 6, max=60, message = "Password must have interval 6 to 60 characters")
    private String password;

    @CPF
    private String cpf;

    @Min(0) @Max(1)
    private int active = 1;

    @Min(0) @Max(1)
    private int admin = 0;

    // Date
    private Date createdAt;
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
