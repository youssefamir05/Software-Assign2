package com.example.masroofy.ui;

import com.example.masroofy.data.IDataRepository;
import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;
import com.example.masroofy.utils.BudgetCalculator;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ViewModel for the Dashboard screen.
 * Handles data logic, calculations, and provides state for the UI.
 */
public class DashboardViewModel {

    private final IDataRepository repository;

    private BudgetCycle   activeCycle;
    private List<Expense> expenses;

    public DashboardViewModel(IDataRepository repository) {
        this.repository = repository;
    }

    /**
     * Loads the current budget cycle and its expenses from the repository.
     */
    public void loadData() {
        activeCycle = repository.getActiveCycle();
        if (activeCycle != null) {
            expenses = repository.getAllExpenses(activeCycle.getId());
        }
    }

    /**
     * Checks if there is an active budget cycle.
     * @return True if a cycle exists.
     */
    public boolean hasCycle() {
        return activeCycle != null;
    }

    /**
     * Calculates the daily spending limit for the current cycle.
     * @return Safe daily spending limit.
     */
    public float getDailyLimit() {
        if (activeCycle == null) return 0f;
        return BudgetCalculator.calcDailyLimit(
                activeCycle.getRemainingBalance(),
                activeCycle.getEndDate()
        );
    }

    public float getRemainingBalance() {
        if (activeCycle == null) return 0f;
        return activeCycle.getRemainingBalance();
    }

    public float getTotalAllowance() {
        if (activeCycle == null) return 0f;
        return activeCycle.getAllowance();
    }

    public String getStartDate() {
        if (activeCycle == null) return "";
        return activeCycle.getStartDate();
    }

    public String getEndDate() {
        if (activeCycle == null) return "";
        return activeCycle.getEndDate();
    }

    public int getActiveCycleId() {
        if (activeCycle == null) return -1;
        return activeCycle.getId();
    }

    /**
     * Aggregates total spending for each category.
     * @return A map where keys are categories and values are total spent.
     */
    public Map<String, Float> getCategoryTotals() {
        Map<String, Float> totals = new HashMap<>();
        if (expenses == null) return totals;

        for (Expense e : expenses) {
            String cat = e.getCategory();
            totals.put(cat, totals.getOrDefault(cat, 0f) + e.getAmount());
        }
        return totals;
    }

    public boolean hasNoExpenses() {
        return expenses == null || expenses.isEmpty();
    }
}
