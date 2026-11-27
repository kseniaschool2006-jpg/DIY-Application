package com.example.diyproject.model;

public class MaterialItem {
    private  String name;
    private boolean inStock;

    public MaterialItem(String name, boolean inStock) {
        this.name = name;
        this.inStock = inStock;
    }

    public String getName() {
        return name;
    }

    public boolean isInStock() {
        return inStock;
    }

    public void setInStock(boolean inStock) {
        this.inStock = inStock;
    }
}
