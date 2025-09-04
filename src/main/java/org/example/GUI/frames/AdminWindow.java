package org.example.GUI.frames;

import org.example.GUI.buttons.AdminButton;
import org.example.GUI.panels.AdminPanel;
import org.example.GUI.panels.AdminPanelFactory;
import org.example.GUI.textArea.AdminTextArea;
import org.example.GUI.textArea.AdminTextField;
import org.example.databaseConnection.DBConnection;
import org.example.helpers.helper;
import org.example.order.Orders;
import org.example.product.Products;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminWindow extends JFrame {
    AdminPanel tabPanel;
    AdminPanel optionsPanel;
    AdminPanel infoPanel;
    AdminTextArea infoArea = new AdminTextArea();

    ButtonGroup tabButtons = new ButtonGroup();

    public static AdminWindow createFrame() {
        return new AdminWindow();
    }

    public AdminWindow() {
        Products products = new Products();

        this.setTitle("AdminView");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setSize(1240, 800);
        this.setVisible(true);
        this.setLayout(null);

        tabPanel = AdminPanelFactory.createPanel(AdminPanelFactory.PanelType.TAB_PANEL,
                this,
                1, 3);

        optionsPanel = AdminPanelFactory.createPanel(AdminPanelFactory.PanelType.OPTIONS_PANEL,
                this,
                2, 6);

        infoPanel = AdminPanelFactory.createPanel(AdminPanelFactory.PanelType.INFORMATION_PANEL,
                this,
                1, 1);

        AdminButton orderTabButton = new AdminButton("Orders", Color.WHITE, true);
        orderTabButton.addActionListener(e -> {
            showOrderFrame();
        });

        AdminButton stockTabButton = new AdminButton("Stock", Color.WHITE, true);
        stockTabButton.addActionListener(e -> {
            showStock();
        });

        AdminButton customersTabButton = new AdminButton("Customers", Color.WHITE, true);
        customersTabButton.addActionListener(e -> {
            showCustomers();
        });

        tabButtons.add(orderTabButton);
        tabButtons.add(stockTabButton);
        tabButtons.add(customersTabButton);

        tabPanel.add(orderTabButton);
        tabPanel.add(stockTabButton);
        tabPanel.add(customersTabButton);

        this.add(tabPanel);
        this.add(optionsPanel);
        this.add(infoPanel);
        showOrderFrame();
    }

    public void showOrderFrame() {

        optionsPanel.removeAll();
        optionsPanel.setLayout(new GridLayout(2, 6));

        AdminTextField orderIdField = new AdminTextField(Color.lightGray);
        AdminTextField customerIdField = new AdminTextField(Color.lightGray);
        AdminTextField categoryField = new AdminTextField(Color.lightGray);
        AdminTextField sizeField = new AdminTextField(Color.lightGray);
        AdminTextField colorField = new AdminTextField(Color.lightGray);
        AdminButton searchButton = new AdminButton("Search", Color.WHITE, true);
        AdminButton resetButton = new AdminButton("Reset", Color.WHITE, true);

        JLabel orderLabel = new JLabel("Order Id");
        JLabel customerLabel = new JLabel("Customer Id");
        JLabel categoryLabel = new JLabel("Category");
        JLabel sizeLabel = new JLabel("Size");
        JLabel colorLabel = new JLabel("Color");

        optionsPanel.add(orderLabel);
        optionsPanel.add(customerLabel);
        optionsPanel.add(categoryLabel);
        optionsPanel.add(sizeLabel);
        optionsPanel.add(colorLabel);
        optionsPanel.add(resetButton);

        optionsPanel.add(orderIdField);
        optionsPanel.add(customerIdField);
        optionsPanel.add(categoryField);
        optionsPanel.add(sizeField);
        optionsPanel.add(colorField);
        optionsPanel.add(searchButton);

        infoPanel.add(infoArea);

        infoArea.displayOrders(DBConnection.orderQuery(null, null, null, null, null));

        searchButton.addActionListener(e -> {
            try {

                infoArea.displayOrders(parseOrderFields(
                        orderIdField.getTextContent(),
                        customerIdField.getTextContent(),
                        categoryField.getTextContent(),
                        sizeField.getTextContent(),
                        colorField.getTextContent()
                ));

            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        resetButton.addActionListener(e -> {
            infoArea.displayOrders(DBConnection.orderQuery(null, null, null, null, null));
            orderIdField.setText("");
            customerIdField.setText("");
            categoryField.setText("");
            sizeField.setText("");
            colorField.setText("");
        });

        this.revalidate();
        this.repaint();
    }

    private Orders parseOrderFields(String orderId, String customerId, String category, String size, String color) {

        try {
            Integer orderId_i = helper.parseInt(orderId);
            Integer customerId_i = helper.parseInt(customerId);
            String category_s = (category == null || category.isBlank()) ? null : category.trim();
            String size_s = (size == null || size.isBlank()) ? null : size.trim();
            String color_s = (color == null || size.isBlank()) ? null : color.trim();

            return DBConnection.orderQuery(orderId_i, customerId_i, category_s, size_s, color_s);

        } catch (NumberFormatException nfe) {
            JOptionPane.showMessageDialog(this,"Customer ID and Order ID must be integers", "Wrong format", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Magic error", "You're a magician!", JOptionPane.ERROR_MESSAGE);
        }
        return null;
    }

    private void showStock() {
        optionsPanel.removeAll();

        optionsPanel.setLayout(new GridLayout(1,3));

        AdminButton sortButton = new AdminButton("Best Selling", Color.WHITE, true);
        sortButton.addActionListener(e -> {
            infoArea.displayProducts(DBConnection.getProductsSortedOnSold());
        });

        AdminButton categoryButton = new AdminButton("Categories", Color.WHITE, true);
        categoryButton.addActionListener(e -> {
            infoArea.displayCategories(DBConnection.getCategories());
        });

        AdminButton resetButton = new AdminButton("Reset", Color.WHITE, true);
        resetButton.addActionListener(e -> {
            infoArea.displayProducts(DBConnection.getProductsSortedOnId());
        });

        optionsPanel.add(sortButton);
        optionsPanel.add(categoryButton);
        optionsPanel.add(resetButton);

        infoArea.displayProducts(DBConnection.getProductsSortedOnId());

        this.revalidate();
        this.repaint();
    }

    private void showCustomers() {
        optionsPanel.removeAll();
        optionsPanel.setLayout(new GridLayout(1,3));

        AdminButton sortButton = new AdminButton("Frequent Buyers", Color.WHITE, true);
        sortButton.addActionListener(e -> {
            infoArea.displayCustomers(DBConnection.getCustomersSortedOnOrders());
        });

        AdminButton categoryButton = new AdminButton("Sort Newest", Color.WHITE, true);
        categoryButton.addActionListener(e -> {
            infoArea.displayCustomers(DBConnection.getCustomersSortedOnNewest());
        });

        AdminButton resetButton = new AdminButton("Reset", Color.WHITE, true);
        resetButton.addActionListener(e -> {
            infoArea.displayCustomers(DBConnection.getCustomersSortedOnId());
        });

        optionsPanel.add(sortButton);
        optionsPanel.add(categoryButton);
        optionsPanel.add(resetButton);

        infoArea.displayCustomers(DBConnection.getCustomersSortedOnId());

        this.revalidate();
        this.repaint();
    }
}
