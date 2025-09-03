package org.example.customer;

import java.util.ArrayList;

public class Customers {
    ArrayList<Customer> customers = new ArrayList<>();

    public Customers() {

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
