package com.example.masroofy.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masroofy.R;
import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.model.BudgetCycle;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private DashboardViewModel viewModel;
    private TextView tvRemainingBalance, tvDailyLimit, tvDateRange, tvEmptyState, tvTotalAllowance;
    private PieChartView pieChartView;
    private ExpenseAdapter expenseAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvRemainingBalance = findViewById(R.id.tvRemainingBalance);
        tvTotalAllowance = findViewById(R.id.tvTotalAllowance);
        tvDailyLimit = findViewById(R.id.tvDailyLimit);
        tvDateRange = findViewById(R.id.tvDateRange);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        pieChartView = findViewById(R.id.pieChartView);
        ExtendedFloatingActionButton btnAddExpense = findViewById(R.id.btnAddExpense);
        View btnResetBudget = findViewById(R.id.btnResetBudget);

        // Setup RecyclerView
        RecyclerView rvExpenses = findViewById(R.id.rvExpenses);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        expenseAdapter = new ExpenseAdapter();
        rvExpenses.setAdapter(expenseAdapter);

        expenseAdapter.setOnExpenseLongClickListener(expense -> {
            new android.app.AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this expense?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    DatabaseHelper db = DatabaseHelper.getInstance(this);
                    // Update remaining balance before deleting
                    BudgetCycle cycle = db.getActiveCycle();
                    if (cycle != null) {
                        db.updateRemainingBalance(cycle.getId(), cycle.getRemainingBalance() + expense.getAmount());
                    }
                    db.deleteExpense(expense.getExpenseId());
                    refreshData();
                })
                .setNegativeButton("Cancel", null)
                .show();
        });

        viewModel = new DashboardViewModel(DatabaseHelper.getInstance(this));

        btnAddExpense.setOnClickListener(v -> {
            startActivity(new Intent(this, ExpenseEntryActivity.class));
        });

        btnResetBudget.setOnClickListener(v -> {
            DatabaseHelper.getInstance(this).clearAllData();
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        viewModel.loadData();

        if (viewModel.hasCycle()) {
            tvRemainingBalance.setText(String.format(Locale.getDefault(), "%.2f EGP", viewModel.getRemainingBalance()));
            tvTotalAllowance.setText(String.format(Locale.getDefault(), "%.2f EGP", viewModel.getTotalAllowance()));
            tvDailyLimit.setText(String.format(Locale.getDefault(), "%.2f EGP", viewModel.getDailyLimit()));
            tvDateRange.setText(String.format("Ends %s", viewModel.getEndDate()));
            
            pieChartView.setData(viewModel.getCategoryTotals());
            
            if (viewModel.hasNoExpenses()) {
                tvEmptyState.setVisibility(View.VISIBLE);
                expenseAdapter.setExpenses(null);
            } else {
                tvEmptyState.setVisibility(View.GONE);
                expenseAdapter.setExpenses(DatabaseHelper.getInstance(this).getAllExpenses(viewModel.getActiveCycleId()));
            }
        }
    }
}
