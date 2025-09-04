package org.example.product;

import java.util.ArrayList;

public class Categories {
    ArrayList<Category> categories = new ArrayList<>();

    public Categories() {

    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public ArrayList<Category> getCategories() {
        return categories;
    }
}
