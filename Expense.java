package com.example.masroofy;

import com.example.masroofy.Category;

public class Expense {

    private int expenseId;
    private double amount;
    private Category category;
    private long timestamp;
    private int cycleId;
    private String note;

    public Expense(int expenseId, double amount, Category category, long timestamp, int cycleId, String note) {
        this.expenseId = expenseId;
        this.amount = amount;
        this.category = category;
        this.timestamp = timestamp;
        this.cycleId = cycleId;
        this.note = note;
    }

    public boolean validate() {
        return amount > 0 && category != null;
    }

    public double getAmount() {
        return amount;
    }

    public Category getCategory() {
        return category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public int getCycleId() {
        return cycleId;
    }

    public String getNote() {
        return note;
    }
}