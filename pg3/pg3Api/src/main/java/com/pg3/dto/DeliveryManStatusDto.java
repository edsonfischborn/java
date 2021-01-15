package com.pg3.dto;

public class DeliveryManStatusDto {
    private long allDeliveryMan;
    private long activeDeliveryMan;
    private long inactiveDeliveryMan;

    public long getAllDeliveryMan() {
        return allDeliveryMan;
    }

    public void setAllDeliveryMan(long allDeliveryMan) {
        this.allDeliveryMan = allDeliveryMan;
    }

    public long getActiveDeliveryMan() {
        return activeDeliveryMan;
    }

    public void setActiveDeliveryMan(long activeDeliveryMan) {
        this.activeDeliveryMan = activeDeliveryMan;
    }

    public long getInactiveDeliveryMan() {
        return inactiveDeliveryMan;
    }

    public void setInactiveDeliveryMan(long inactiveDeliveryMan) {
        this.inactiveDeliveryMan = inactiveDeliveryMan;
    }
}
