package com.example.masroofy.data;

import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;
import java.util.List;

public interface IDataRepository {
    void saveBudgetCycle(BudgetCycle cycle);
    BudgetCycle getActiveCycle();
    void insertExpense(Expense expense);
    List<Expense> getAllExpenses(int cycleId);
    void deleteExpense(int expenseId);
    void updateRemainingBalance(int cycleId, float newBalance);
}