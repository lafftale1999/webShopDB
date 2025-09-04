package org.example.product;

public class Product {
    private int id;
    private String name;
    private String color;
    private String size;
    private String brand;
    private int quantity;
    private int sold;

    public Product(int id, String name, String color, String size, String brand, int quantity, int sold) {
        setId(id);
        setName(name);
        setColor(color);
        setBrand(brand);
        setQuantity(quantity);
        setSize(size);
        setSold(sold);
    }
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }
}
