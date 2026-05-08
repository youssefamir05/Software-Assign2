package com.example.masroofy.model;

/**
 * Represents an individual expense item.
 * Contains details such as amount, category, time of expenditure, and associated budget cycle.
 */
public class Expense {
    private int expenseId;
    private float amount;
    private String category;
    private String timestamp;
    private int cycleId;
    private String note;

    /**
     * Constructs a new Expense.
     *
     * @param amount   The cost of the expense.
     * @param category The category of the expense (e.g., Food, Transport).
     * @param cycleId  The ID of the budget cycle this expense belongs to.
     */
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
