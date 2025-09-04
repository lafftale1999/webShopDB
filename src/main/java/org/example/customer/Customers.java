package org.example.customer;

import java.util.ArrayList;

public class Customers {
    private ArrayList<Customer> customers = new ArrayList<>();

    public Customers() {

    }

    public ArrayList<Customer> getCustomers() {
        return customers;
    }

    public Customer findCustomerByID(int id) {
        for(Customer customer : customers) {
            if (customer.getID() == id) {
                return customer;
            }
        }

        return null;
    }

    public void addCustomer(Customer customer) {
        if(customer != null) {
            customers.add(customer);
        }
    }
}
