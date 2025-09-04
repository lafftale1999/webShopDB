package org.example.order;

import org.example.customer.Customer;

public class Order {

    private int ID;
    private String customerName;
    private String status;
    private float orderValue;

    public Order(int ID, String customerName, String status, float orderValue) {
        setID(ID);
        setCustomerName(customerName);
        setStatus(status);
        setOrderValue(orderValue);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public float getOrderValue() {
        return orderValue;
    }

    public void setOrderValue(float orderValue) {
        this.orderValue = orderValue;
    }
}
