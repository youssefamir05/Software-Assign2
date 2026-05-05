package com.example.masroofy.model;
public class Expense {
    private int expenseId;
    private float amount;
    private String category;
    private String timestamp;
    private int cycleId;
    private String note;

    public Expense(float amount, String category, int cycleId) {
        this.amount = amount;
        this.category = category;
        this.cycleId = cycleId;
    }

    public int getExpenseId()            { return expenseId; }
    public void setExpenseId(int id)     { this.expenseId = id; }
    public float getAmount()             { return amount; }
    public String getCategory()          { return category; }
    public int getCycleId()              { return cycleId; }
    public String getTimestamp()         { return timestamp; }
    public void setTimestamp(String t)   { this.timestamp = t; }
    public String getNote()              { return note; }
    public void setNote(String note)     { this.note = note; }
}
