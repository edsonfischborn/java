package com.pg3.models;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.web.util.HtmlUtils;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class DeliveryMan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min=7, max=50, message="Name must have interval 7 to 50 characters")
    @Pattern(regexp = "(?=^.{2,60}$)^[A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ | a-zàáâãèéêìíóôõùúç][a-zàáâãèéêìíóôõùúç | A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ]+(?:[ ](?:das?|dos?|de|e|[A-Z][a-z]+))*$", message="Name does not must have invalid characters")
    private String name;

    @Email
    private String email;

    @Pattern(regexp = "(\\d){11}", message="Phone does not must have invalid characters")
    private String phone;

    @CPF
    private String cpf;

    @Min(0) @Max(1)
    private int active = 1;

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

    public void setName(String name) { this.name = name; }

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
