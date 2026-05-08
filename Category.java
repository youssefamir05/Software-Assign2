package com.example.masroofy;

public class Category {

    private int categoryId;
    private String name;
    private int iconResId;
    private boolean isDefault;

    public Category(int categoryId, String name, int iconResId, boolean isDefault) {
        this.categoryId = categoryId;
        this.name = name;
        this.iconResId = iconResId;
        this.isDefault = isDefault;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getName() {
        return name;
    }

    public int getIconResId() {
        return iconResId;
    }

    public boolean isDefault() {
        return isDefault;
    }
}