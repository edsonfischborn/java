package com.pg3.dto;

import java.util.ArrayList;

public class DeliveryStatusDto {
    private long allDeliveries;
    private long pendingDeliveries;
    private long deliveredDeliveries;
    private long problemsDeliveries;

    private ArrayList<String[]> lastDaysDeliveredDeliveries = new ArrayList<>();
    private ArrayList<String[]> lastDaysProblemsDeliveries = new ArrayList<>();

    public long getAllDeliveries() {
        return allDeliveries;
    }

    public void setAllDeliveries(long allDeliveries) {
        this.allDeliveries = allDeliveries;
    }

    public long getPendingDeliveries() {
        return pendingDeliveries;
    }

    public void setPendingDeliveries(long pendingDeliveries) {
        this.pendingDeliveries = pendingDeliveries;
    }

    public long getDeliveredDeliveries() {
        return deliveredDeliveries;
    }

    public void setDeliveredDeliveries(long deliveredDeliveries) {
        this.deliveredDeliveries = deliveredDeliveries;
    }

    public long getProblemsDeliveries() {
        return problemsDeliveries;
    }

    public void setProblemsDeliveries(long problemsDeliveries) {
        this.problemsDeliveries = problemsDeliveries;
    }

    public ArrayList<String[]> getLastDaysDeliveredDeliveries() {
        return lastDaysDeliveredDeliveries;
    }

    public void setLastDaysDeliveredDeliveries(ArrayList<String[]> lastDaysDeliveredDeliveries) {
        this.lastDaysDeliveredDeliveries = lastDaysDeliveredDeliveries;
    }

    public ArrayList<String[]> getLastDaysProblemsDeliveries() {
        return lastDaysProblemsDeliveries;
    }

    public void setLastDaysProblemsDeliveries(ArrayList<String[]> lastDaysProblemsDeliveries) {
        this.lastDaysProblemsDeliveries = lastDaysProblemsDeliveries;
    }
}
