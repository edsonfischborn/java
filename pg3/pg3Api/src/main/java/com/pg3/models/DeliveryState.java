package com.pg3.models;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class DeliveryState {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Size(min = 3, max=20, message = "State color must have interval 3 to 20 characters")
    private String stateColor;

    @Size(min = 5, max=20, message = "State name must have interval 5 to 20 characters")
    private String stateName;

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

    public String getStateColor() {
        return stateColor;
    }

    public void setStateColor(String stateColor) {
        this.stateColor = stateColor;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
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
}
