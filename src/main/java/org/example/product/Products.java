package org.example.product;

import org.example.customer.Customer;

import java.util.ArrayList;

public class Products {
    ArrayList<Product> products = new ArrayList<>();

    public Products() {

    }

    public Product findProductByID(int id) {
        for(Product p : products) {
            if(p.getId() == id) {
                return p;
            }
        }

        return null;
    }
}
