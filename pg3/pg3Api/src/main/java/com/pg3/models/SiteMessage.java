package com.pg3.models;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Date;

@Entity
public class SiteMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 7, max=50, message = "Name must have interval 7 to 50 characters")
    @Pattern(regexp = "(?=^.{2,60}$)^[A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ | a-zàáâãèéêìíóôõùúç][a-zàáâãèéêìíóôõùúç | A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ]+(?:[ ](?:das?|dos?|de|e|[A-Z][a-z]+))*$", message="Name does not must have invalid characters")
    private String name;

    @Email
    private String email;

    @Min(0) @Max(1)
    @Column(name = "readed")
    private int read = 0;

    @Size(min = 10, max=200, message = "Message must have interval 10 to 200 characters")
    private String message;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
