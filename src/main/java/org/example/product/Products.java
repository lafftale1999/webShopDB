package org.example.product;

import org.example.customer.Customer;

import java.util.ArrayList;

public class Products {
    private ArrayList<Product> products = new ArrayList<>();

    public Products() {
        // Clothing Products
//        products.add(new Product(1, "Classic T-Shirt", "White", "M", "Clothing", "Nike", 150, 89));
//        products.add(new Product(2, "Denim Jeans", "Blue", "L", "Clothing", "Levi's", 75, 45));
//        products.add(new Product(3, "Hoodie", "Black", "XL", "Clothing", "Adidas", 120, 67));
//        products.add(new Product(4, "Polo Shirt", "Red", "S", "Clothing", "Ralph Lauren", 90, 34));
//        products.add(new Product(5, "Dress Shirt", "Navy", "M", "Clothing", "Hugo Boss", 60, 28));
//
//        // Footwear Products
//        products.add(new Product(6, "Running Shoes", "Black", "9", "Footwear", "Nike", 80, 52));
//        products.add(new Product(7, "Canvas Sneakers", "White", "8", "Footwear", "Converse", 95, 71));
//        products.add(new Product(8, "Leather Boots", "Brown", "10", "Footwear", "Dr. Martens", 45, 23));
//        products.add(new Product(9, "High Heels", "Red", "7", "Footwear", "Jimmy Choo", 30, 18));
//        products.add(new Product(10, "Sandals", "Tan", "8", "Footwear", "Birkenstock", 70, 41));
//
//        // Electronics Products
//        products.add(new Product(11, "Wireless Headphones", "Black", "Standard", "Electronics", "Sony", 200, 156));
//        products.add(new Product(12, "Smartphone", "Silver", "Standard", "Electronics", "Apple", 50, 38));
//        products.add(new Product(13, "Laptop", "Gray", "15-inch", "Electronics", "Dell", 25, 14));
//        products.add(new Product(14, "Tablet", "White", "10-inch", "Electronics", "Samsung", 40, 27));
//        products.add(new Product(15, "Smart Watch", "Black", "42mm", "Electronics", "Apple", 85, 62));
//
//        // Home & Garden Products
//        products.add(new Product(16, "Coffee Mug", "Blue", "Standard", "Home & Garden", "IKEA", 300, 187));
//        products.add(new Product(17, "Throw Pillow", "Green", "18x18", "Home & Garden", "West Elm", 120, 78));
//        products.add(new Product(18, "Table Lamp", "White", "Medium", "Home & Garden", "Target", 55, 31));
//        products.add(new Product(19, "Wall Art", "Multi", "24x36", "Home & Garden", "CB2", 35, 19));
//        products.add(new Product(20, "Plant Pot", "Terracotta", "Large", "Home & Garden", "Pottery Barn", 90, 53));
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        products.add(product);
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
