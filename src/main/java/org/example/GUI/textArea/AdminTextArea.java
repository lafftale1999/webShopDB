package org.example.GUI.textArea;

import org.example.customer.Customer;
import org.example.customer.Customers;
import org.example.order.Order;
import org.example.order.Orders;
import org.example.product.Categories;
import org.example.product.Category;
import org.example.product.Product;
import org.example.product.Products;

import javax.swing.*;
import java.awt.*;

public class AdminTextArea extends JTextArea {

    public AdminTextArea() {
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setEditable(false);
        this.setMargin(new Insets(5,10,5,10));
        JScrollPane scrollPane = new JScrollPane(this);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    }

    public void displayCustomers(Customers customers) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-4.4s │ %-40.40s │ %-40.40s │ %-20.20s │ %-6s | %-10s%n",
                                "ID", "Name", "Email", "Phone", "Qty", "Total"));

        for(Customer c : customers.getCustomers()) {
            sb.append(String.format("%-4.4s │ %-40.40s │ %-40.40s │ %-20.20s │ %-6d │ $%-9.2f%n",
                c.getID(),
                c.getName(),
                c.getEmail(),
                c.getPhone(),
                c.getOrderQuantity(),
                c.getOrderTotal()));
        }

        this.setText(sb.toString());
    }

    public void displayOrders(Orders orders) {
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        final String fmt = "%-4.4s │ %-40.40s │ %-18.18s │ %-10s%n";

        StringBuilder sb = new StringBuilder();
        String header = String.format(fmt, "ID", "Name", "Status", "Total");

        sb.append(header);
        sb.append("-".repeat(header.stripTrailing().length())).append("\n");

        for (Order o : orders.getOrders()) {
            sb.append(String.format(fmt,
                    o.getID(),
                    o.getCustomerName(),
                    o.getStatus(),
                    o.getOrderValue()));
        }

        this.setText(sb.toString());
    }

    public void displayProducts(Products products) {
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        final String fmt = "%-4.4s │ %-40.40s │ %-18.18s │ %-18.18s │ %-36.36s | %6s │ %4s%n";

        StringBuilder sb = new StringBuilder();
        String header = String.format(fmt, "ID", "Name", "Color", "Size", "Brand", "Qty", "Sold");

        sb.append(header);
        sb.append("-".repeat(header.stripTrailing().length())).append("\n");

        for (Product p : products.getProducts()) {
            sb.append(String.format(fmt,
                    p.getId(),
                    p.getName(),
                    p.getColor(),
                    p.getSize(),
                    p.getBrand(),
                    p.getQuantity(),
                    p.getSold()));
        }

        this.setText(sb.toString());
    }

    public void displayCategories(Categories categories) {
        this.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        final String fmt = "%-40.40s | %6s%n";

        StringBuilder sb = new StringBuilder();
        String header = String.format(fmt, "Category", "Products");

        sb.append(header);
        sb.append("-".repeat(header.stripTrailing().length())).append("\n");

        for (Category c : categories.getCategories()) {
            sb.append(String.format(fmt,
                    c.getName(),
                    c.getQuantity()));
        }

        this.setText(sb.toString());
    }
}
