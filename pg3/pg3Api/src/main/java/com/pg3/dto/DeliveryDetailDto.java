package com.pg3.dto;

import com.pg3.models.Delivery;
import com.pg3.models.DeliveryMan;
import com.pg3.models.DeliveryState;

public class DeliveryDetailDto extends DeliveryDto {
    private String receiverSignature;
    private UserDto user;
    private DeliveryMan deliveryMan;

    public DeliveryDetailDto(Delivery delivery, DeliveryState deliveryState, UserDto user, DeliveryMan deliveryMan) {
        super(delivery, deliveryState);
        setReceiverSignature(delivery.getReceiverSignature());
        setUser(user);
        setDeliveryMan(deliveryMan);
    }

    public String getReceiverSignature() {
        return receiverSignature;
    }

    public void setReceiverSignature(String receiverSignature) {
        this.receiverSignature = receiverSignature;
    }

    public UserDto getUser() {
        return user;
    }

    public void setUser(UserDto user) {
        this.user = user;
    }

    public DeliveryMan getDeliveryMan() {
        return deliveryMan;
    }

    public void setDeliveryMan(DeliveryMan deliveryMan) {
        this.deliveryMan = deliveryMan;
    }
}
