package org.example.databaseConnection;

import org.example.customer.Customer;
import org.example.customer.Customers;
import org.example.order.Order;
import org.example.order.Orders;
import org.example.product.Categories;
import org.example.product.Category;
import org.example.product.Product;
import org.example.product.Products;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Properties;

public class DBConnection {

    private static Properties properties;
    private static boolean propertiesCreated = false;

    private static synchronized void createPropertiesOnce() {

        if (!propertiesCreated) {
            properties = new Properties();
            try {
                properties.load(new FileInputStream("src/main/Resources/config.properties"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            propertiesCreated = true;
        }
    }

    private static <T> T sendQuery(String sql, ArrayList<Object> params, ResultSetMapper<T> mapper) {

        // CHECK FOR VALID STATE
        if (sql == null
            || sql.isBlank()) {
            throw new IllegalArgumentException("Missing SQL string");
        }

        // CREATE PROPERTIES ONCE
        createPropertiesOnce();

        // START CONNECTION
        try (Connection connection = DriverManager.getConnection(
                properties.getProperty("db.url"),
                properties.getProperty("db.username"),
                properties.getProperty("db.password")
        )) {

            // CREATE QUERY FROM STRING ARGUMENT
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            if (params != null) {
                // SET PARAMETERS OF THE QUERY
                for (int i = 0; i < params.size(); i++) {
                    Object value = params.get(i);

                    if (value instanceof Integer) {
                        preparedStatement.setInt(i + 1, (Integer)value);
                    } else if (value instanceof Float) {
                        preparedStatement.setFloat(i + 1, (Float)value);
                    } else if (value instanceof String) {
                        preparedStatement.setString(i + 1, (String)value);
                    } else if (value instanceof Boolean) {
                        preparedStatement.setBoolean(i + 1, (Boolean)value);
                    } else if (value instanceof Character) {
                        preparedStatement.setString(i + 1, (String)value);
                    }
                }
            }

            try(ResultSet resultSet = preparedStatement.executeQuery()) {
                return mapper.map(resultSet);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Orders orderQuery(Integer orderId, Integer customerId, String category, String size, String color) {

        StringBuilder sql = new StringBuilder("" +
                "SELECT DISTINCT order_information.id AS id, CONCAT(customer.firstName, ' ', customer.lastName) AS name,  order_information.status AS status, order_information.total_gross AS total FROM order_information\n" +
                "INNER JOIN customer ON order_information.customer_id = customer.id\n" +
                "INNER JOIN order_product_map ON order_information.id = order_product_map.order_id\n" +
                "INNER JOIN stock_unit ON order_product_map.stock_id = stock_unit.id\n" +
                "INNER JOIN product ON stock_unit.product_id = product.id\n" +
                "INNER JOIN product_category_map ON product.id = product_category_map.product_id\n" +
                "INNER JOIN category ON product_category_map.category_id = category.id\n" +
                "INNER JOIN size ON stock_unit.size_id = size.id\n" +
                "INNER JOIN color ON stock_unit.color_id = color.id\n" +
                "WHERE 1=1");

        ArrayList<Object> params = new ArrayList<>();

        if (orderId != null) {
            sql.append(" AND order_information.id = ?");
            params.add(orderId);
        }
        if (customerId != null) {
            sql.append(" AND customer.id = ?");
            params.add(customerId);
        }
        if (category != null && !category.isBlank()) {
            sql.append(" AND category.name = ?");
            params.add(category);
        }
        if (size != null && !size.isBlank()) {
            sql.append(" AND size.name = ?");
            params.add(size);
        }
        if (color != null && !color.isBlank()) {
            sql.append(" AND color.name = ?");
            params.add(color);
        }

        sql.append(" ORDER BY order_information.id;");

        return sendQuery(sql.toString(), params, rs -> {
            Orders orders = new Orders();
            while(rs.next()) {
                orders.addOrder(new Order(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("status"),
                        rs.getFloat("total")
                ));
            }

            return orders;
        });
    }

    public static Products getProductsSortedOnId() {

        StringBuilder sql = new StringBuilder(
                "WITH sold_per_stock AS (\n" +
                "\tSELECT order_product_map.stock_id, SUM(order_product_map.quantity) AS sold_qty\n" +
                "    FROM order_product_map\n" +
                "    JOIN order_information ON order_information.id = order_product_map.order_id\n" +
                "    GROUP BY order_product_map.stock_id\n" +
                ")\n" +
                "SELECT\n" +
                "\tstock_unit.id AS id,\n" +
                "    product.name AS name,\n" +
                "    color.name AS color,\n" +
                "    size.name AS size,\n" +
                "    brand.name AS brand,\n" +
                "    stock_unit.quantity AS stock,\n" +
                "    COALESCE(sold_per_stock.sold_qty, 0) AS sold\n" +
                "FROM product\n" +
                "INNER JOIN stock_unit ON product.id = stock_unit.product_id\n" +
                "INNER JOIN color ON stock_unit.color_id = color.id\n" +
                "INNER JOIN size ON stock_unit.size_id = size.id\n" +
                "INNER JOIN brand ON product.brand_id = brand.id\n" +
                "LEFT JOIN sold_per_stock ON sold_per_stock.stock_id = stock_unit.id\n" +
                "ORDER BY id;");

        return sendQuery(sql.toString(), null, rs -> {
            Products products = new Products();

            while(rs.next()) {
                products.addProduct(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("size"),
                        rs.getString("brand"),
                        rs.getInt("stock"),
                        rs.getInt("sold")
                ));
            }

            return products;
        });
    }

    public static Products getProductsSortedOnSold() {

        StringBuilder sql = new StringBuilder(
                "WITH sold_per_stock AS (\n" +
                        "\tSELECT order_product_map.stock_id, SUM(order_product_map.quantity) AS sold_qty\n" +
                        "    FROM order_product_map\n" +
                        "    JOIN order_information ON order_information.id = order_product_map.order_id\n" +
                        "    GROUP BY order_product_map.stock_id\n" +
                        ")\n" +
                        "SELECT\n" +
                        "\tstock_unit.id AS id,\n" +
                        "    product.name AS name,\n" +
                        "    color.name AS color,\n" +
                        "    size.name AS size,\n" +
                        "    brand.name AS brand,\n" +
                        "    stock_unit.quantity AS stock,\n" +
                        "    COALESCE(sold_per_stock.sold_qty, 0) AS sold\n" +
                        "FROM product\n" +
                        "INNER JOIN stock_unit ON product.id = stock_unit.product_id\n" +
                        "INNER JOIN color ON stock_unit.color_id = color.id\n" +
                        "INNER JOIN size ON stock_unit.size_id = size.id\n" +
                        "INNER JOIN brand ON product.brand_id = brand.id\n" +
                        "LEFT JOIN sold_per_stock ON sold_per_stock.stock_id = stock_unit.id\n" +
                        "ORDER BY sold DESC;");

        return sendQuery(sql.toString(), null, rs -> {
            Products products = new Products();

            while(rs.next()) {
                products.addProduct(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("color"),
                        rs.getString("size"),
                        rs.getString("brand"),
                        rs.getInt("stock"),
                        rs.getInt("sold")
                ));
            }

            return products;
        });
    }

    public static Categories getCategories() {
        StringBuilder sql = new StringBuilder("" +
                "SELECT category.name AS name, COALESCE(COUNT(product_category_map.category_id), 0) AS product\n" +
                "FROM category\n" +
                "LEFT JOIN product_category_map ON product_category_map.category_id = category.id\n" +
                "GROUP BY category.id\n" +
                "ORDER BY product DESC;");

        return sendQuery(sql.toString(), null, rs -> {
            Categories categories = new Categories();
            while(rs.next()) {
                categories.addCategory(new Category(
                        rs.getString("name"),
                        rs.getInt("product")
                ));
            }

            return categories;
        });
    }

    public static Customers getCustomersSortedOnId() {
        StringBuilder sql = new StringBuilder("" +
                "WITH customer_stats AS (\n" +
                "\tSELECT \n" +
                "\t\torder_information.customer_id,\n" +
                "        COUNT(order_information.customer_id) AS orders,\n" +
                "        SUM(order_information.total_gross) AS total\n" +
                "\tFROM order_information\n" +
                "    GROUP BY order_information.customer_id\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "\tcustomer.id AS id,\n" +
                "    CONCAT(customer.firstName, ' ', customer.lastName) AS name,\n" +
                "    customer.email AS email,\n" +
                "    customer.phone AS phone,\n" +
                "    COALESCE(customer_stats.orders,0) AS orders,\n" +
                "    COALESCE(customer_stats.total,0) AS total\n" +
                "FROM customer\n" +
                "LEFT JOIN customer_stats ON customer_stats.customer_id = customer.id\n" +
                "ORDER BY customer.id ASC;");

        return sendQuery(sql.toString(), null, rs -> {
            Customers customers = new Customers();
            while(rs.next()) {
                customers.addCustomer(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("orders"),
                        rs.getFloat("total")
                ));
            }

            return customers;
        });
    }

    public static Customers getCustomersSortedOnNewest() {
        StringBuilder sql = new StringBuilder("" +
                "WITH customer_stats AS (\n" +
                "\tSELECT \n" +
                "\t\torder_information.customer_id,\n" +
                "        COUNT(order_information.customer_id) AS orders,\n" +
                "        SUM(order_information.total_gross) AS total\n" +
                "\tFROM order_information\n" +
                "    GROUP BY order_information.customer_id\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "\tcustomer.id AS id,\n" +
                "    CONCAT(customer.firstName, ' ', customer.lastName) AS name,\n" +
                "    customer.email AS email,\n" +
                "    customer.phone AS phone,\n" +
                "    COALESCE(customer_stats.orders,0) AS orders,\n" +
                "    COALESCE(customer_stats.total,0) AS total\n" +
                "FROM customer\n" +
                "LEFT JOIN customer_stats ON customer_stats.customer_id = customer.id\n" +
                "ORDER BY customer.id DESC;");

        return sendQuery(sql.toString(), null, rs -> {
            Customers customers = new Customers();
            while(rs.next()) {
                customers.addCustomer(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("orders"),
                        rs.getFloat("total")
                ));
            }

            return customers;
        });
    }

    public static Customers getCustomersSortedOnOrders() {
        StringBuilder sql = new StringBuilder("" +
                "WITH customer_stats AS (\n" +
                "\tSELECT \n" +
                "\t\torder_information.customer_id,\n" +
                "        COUNT(order_information.customer_id) AS orders,\n" +
                "        SUM(order_information.total_gross) AS total\n" +
                "\tFROM order_information\n" +
                "    GROUP BY order_information.customer_id\n" +
                ")\n" +
                "\n" +
                "SELECT\n" +
                "\tcustomer.id AS id,\n" +
                "    CONCAT(customer.firstName, ' ', customer.lastName) AS name,\n" +
                "    customer.email AS email,\n" +
                "    customer.phone AS phone,\n" +
                "    COALESCE(customer_stats.orders,0) AS orders,\n" +
                "    COALESCE(customer_stats.total,0) AS total\n" +
                "FROM customer\n" +
                "LEFT JOIN customer_stats ON customer_stats.customer_id = customer.id\n" +
                "ORDER BY orders DESC;");

        return sendQuery(sql.toString(), null, rs -> {
            Customers customers = new Customers();
            while(rs.next()) {
                customers.addCustomer(new Customer(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getInt("orders"),
                        rs.getFloat("total")
                ));
            }

            return customers;
        });
    }
}
