package com.example.masroofy.model;

/**
 * Represents a budget cycle with a defined allowance and duration.
 * Tracks the starting amount, the date range, and the current remaining balance.
 */
public class BudgetCycle {
    private int id;
    private float allowance;
    private String startDate;
    private String endDate;
    private float remainingBalance;

    /**
     * Constructs a new BudgetCycle.
     *
     * @param allowance The total amount allocated for this cycle.
     * @param startDate The start date of the cycle (ISO format recommended).
     * @param endDate   The end date of the cycle (ISO format recommended).
     */
    public BudgetCycle(float allowance, String startDate, String endDate) {
        this.allowance = allowance;
        this.startDate = startDate;
        this.endDate = endDate;
        this.remainingBalance = allowance;
    }

    public int getId()                             { return id; }
    public void setId(int id)                      { this.id = id; }
    public float getAllowance()                     { return allowance; }
    public String getStartDate()                   { return startDate; }
    public String getEndDate()                     { return endDate; }
    public float getRemainingBalance()             { return remainingBalance; }
    public void setRemainingBalance(float balance) { this.remainingBalance = balance; }
}
