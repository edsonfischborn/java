package com.pg3.models;

import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Min(value = 1, message = "Id must have one number")
    private long userId;

    @Min(value = 1, message = "Delivery man id must have one number")
    private long deliveryManId;

    @Min(value = 1, message = "Delivery state must have one number")
    private long deliveryState = 1;

    @Size(min = 10, max = 200, message = "Description must have interval 10 to 200 characters")
    private String description;

    // Address
    @Size(min = 3, max = 50, message = "Street must have interval 10 to 50 characters")
    private String street;

    @Size(min = 1, max = 10, message = "Number must have interval 10 to 10 characters")
    private String number;

    @Size(max = 150, message = "Street must have maximum 150 characters")
    private String complement = null;

    @Size(min = 5, max = 20, message="Zipcode must have interval 5 to 20 characters")
    private String zipCode;

    @Size(min = 3, max = 80, message="City must have interval 3 to 80 characters" )
    private String city;

    @Size(min = 3, max = 80, message="District must have interval 3 to 80 characters")
    private String district;

    @Size(min = 2, max = 2, message="State must have 2 characters")
    private String state;

    // Receiver
    @Size(min = 7, max = 50, message="Name must have interval 7 to 50 characters")
    @Pattern(regexp = "(?=^.{2,60}$)^[A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ | a-zàáâãèéêìíóôõùúç][a-zàáâãèéêìíóôõùúç | A-ZÀÁÂĖÈÉÊÌÍÒÓÔÕÙÚÛÇ]+(?:[ ](?:das?|dos?|de|e|[A-Z][a-z]+))*$", message="Receiver name does not must have invalid characters")
    private String receiverName;

    @CPF
    private String receiverCpf;

    @Size(min = 15, max = 150, message="Receiver contact must have interval 15 to 150 characters")
    private String receiverContact;

    private String receiverSignature = null;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getDeliveryManId() {
        return deliveryManId;
    }

    public void setDeliveryManId(long deliveryManId) {
        this.deliveryManId = deliveryManId;
    }

    public long getDeliveryState() {
        return deliveryState;
    }

    public void setDeliveryState(long deliveryState) {
        this.deliveryState = deliveryState;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state.toUpperCase();
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverCpf() {
        return receiverCpf;
    }

    public void setReceiverCpf(String receiverCpf) {
        this.receiverCpf = receiverCpf;
    }

    public String getReceiverContact() {
        return receiverContact;
    }

    public void setReceiverContact(String receiverContact) {
        this.receiverContact = receiverContact;
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

    public String getReceiverSignature() {
        return receiverSignature;
    }

    public void setReceiverSignature(String receiverSignature) {
        this.receiverSignature = receiverSignature;
    }
}
