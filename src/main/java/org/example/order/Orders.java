package org.example.order;

import java.util.ArrayList;

public class Orders {
    private ArrayList<Order> orders = new ArrayList<>();

    public Orders() {

    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public ArrayList<Order> getOrders() {
        return orders;
    }
}
