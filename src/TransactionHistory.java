package com.example.masroofy.ui.history;

import com.example.masroofy.data.IDataRepository;
import com.example.masroofy.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class TransactionHistory {

    private final IDataRepository repository;
    private List<Expense> allExpenses;

    public TransactionHistory(IDataRepository repository) {
        this.repository = repository;
        this.allExpenses = new ArrayList<>();
    }

    public List<Expense> loadAll(int cycleId) {
        allExpenses = repository.getAllExpenses(cycleId);
        return new ArrayList<>(allExpenses);
    }

    public List<Expense> filterByCategory(String category) {
        if (category == null || category.isEmpty()) return new ArrayList<>(allExpenses);
        List<Expense> result = new ArrayList<>();
        for (Expense e : allExpenses) {
            if (e.getCategory().equalsIgnoreCase(category)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Expense> filterByDate(String datePrefix) {
        if (datePrefix == null || datePrefix.isEmpty()) return new ArrayList<>(allExpenses);
        List<Expense> result = new ArrayList<>();
        for (Expense e : allExpenses) {
            if (e.getTimestamp() != null && e.getTimestamp().startsWith(datePrefix)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Expense> applyFilter(FilterCriteria criteria) {
        List<Expense> result = new ArrayList<>();
        for (Expense e : allExpenses) {
            if (criteria.matches(e)) {
                result.add(e);
            }
        }
        return result;
    }

    public List<Expense> getAllLoaded() {
        return new ArrayList<>(allExpenses);
    }

    public boolean isEmpty() {
        return allExpenses == null || allExpenses.isEmpty();
    }
}
