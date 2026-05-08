package com.example.masroofy.ui.dashboard;

import com.example.masroofy.data.IDataRepository;
import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;
import com.example.masroofy.utils.BudgetCalculator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardViewModel {

    private final IDataRepository repository;

    private BudgetCycle   activeCycle;
    private List<Expense> expenses;

    public DashboardViewModel(IDataRepository repository) {
        this.repository = repository;
    }

    public void loadData() {
        activeCycle = repository.getActiveCycle();
        if (activeCycle != null) {
            expenses = repository.getAllExpenses(activeCycle.getId());
        }
    }

    public boolean hasCycle() {
        return activeCycle != null;
    }

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

    public String getEndDate() {
        if (activeCycle == null) return "";
        return activeCycle.getEndDate();
    }

    public int getActiveCycleId() {
        if (activeCycle == null) return -1;
        return activeCycle.getId();
    }

    public Map<String, Float> getCategoryTotals() {
        Map<String, Float> totals = new HashMap<>();
        if (expenses == null) return totals;

        for (Expense e : expenses) {
            String cat = e.getCategory();
            totals.put(cat, totals.getOrDefault(cat, 0f) + e.getAmount());
        }
        return totals;
    }

    public float getTotalSpent() {
        if (expenses == null) return 0f;
        float total = 0f;
        for (Expense e : expenses) total += e.getAmount();
        return total;
    }

    public boolean hasNoExpenses() {
        return expenses == null || expenses.isEmpty();
    }

    public boolean isFinalDay() {
        if (activeCycle == null) return false;
        String today = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                .format(new Date());
        return today.equals(activeCycle.getEndDate());
    }
}
