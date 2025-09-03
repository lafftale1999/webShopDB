package org.example.customer;

public class Customer {
    private int ID;
    private String name;
    private String email;
    private String phone;
    private int orderQuantity;
    private float orderTotal;

    public Customer(int id, String name, String email, String phone, int orderQuantity, float orderTotal) {
        setID(id);
        setName(name);
        setEmail(email);
        setPhone(phone);
        setOrderQuantity(orderQuantity);
        setOrderTotal(orderTotal);
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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

    public int getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(int orderQuantity) {
        this.orderQuantity = orderQuantity;
    }

    public float getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(float orderTotal) {
        this.orderTotal = orderTotal;
    }
}
