package com.example.masroofy.ui.history;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masroofy.R;
import com.example.masroofy.adapter.ExpenseAdapter;
import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;
import com.example.masroofy.ui.expense.ExpenseEntryActivity;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity
        implements ExpenseAdapter.OnExpenseActionListener {

    private RecyclerView  rvExpenses;
    private TextView      tvEmptyState;
    private Button        btnLogFirst;
    private Button        btnFilter;

    private ExpenseAdapter     adapter;
    private TransactionHistory transactionHistory;
    private DatabaseHelper     db;
    private int                activeCycleId = -1;
    private List<Expense>      currentList   = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        db = DatabaseHelper.getInstance(this);
        transactionHistory = new TransactionHistory(db);

        bindViews();
        setupRecyclerView();
        setupSwipeToDelete();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadHistory(new FilterCriteria());
    }

    private void bindViews() {
        rvExpenses   = findViewById(R.id.rvExpenses);
        tvEmptyState = findViewById(R.id.tvEmptyState);
        btnLogFirst  = findViewById(R.id.btnLogFirst);
        btnFilter    = findViewById(R.id.btnFilter);
    }

    private void setupRecyclerView() {
        adapter = new ExpenseAdapter(currentList, this);
        rvExpenses.setLayoutManager(new LinearLayoutManager(this));
        rvExpenses.setAdapter(adapter);
    }

    private void setupSwipeToDelete() {
        ItemTouchHelper.SimpleCallback swipeCallback =
                new ItemTouchHelper.SimpleCallback(0,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(@androidx.annotation.NonNull RecyclerView rv,
                                  @androidx.annotation.NonNull RecyclerView.ViewHolder vh,
                                  @androidx.annotation.NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@androidx.annotation.NonNull RecyclerView.ViewHolder vh,
                                 int direction) {
                int position = vh.getAdapterPosition();
                Expense swiped = currentList.get(position);
                showDeleteConfirmation(swiped, position);
                adapter.notifyItemChanged(position);
            }
        };
        new ItemTouchHelper(swipeCallback).attachToRecyclerView(rvExpenses);
    }

    private void setupClickListeners() {
        btnLogFirst.setOnClickListener(v ->
                startActivity(new Intent(this, ExpenseEntryActivity.class))
        );

        btnFilter.setOnClickListener(v -> {
            FilterDialogFragment dialog = new FilterDialogFragment();
            dialog.setOnFilterAppliedListener(criteria -> loadHistory(criteria));
            dialog.show(getSupportFragmentManager(), "FilterDialog");
        });
    }

    private void loadHistory(FilterCriteria criteria) {
        BudgetCycle cycle = db.getActiveCycle();
        if (cycle == null) { showEmpty(); return; }

        activeCycleId = cycle.getId();
        transactionHistory.loadAll(activeCycleId);

        if (criteria != null && !criteria.isEmpty()) {
            currentList = transactionHistory.applyFilter(criteria);
        } else {
            currentList = transactionHistory.getAllLoaded();
        }

        adapter.updateList(currentList);

        if (currentList.isEmpty()) showEmpty(); else showList();
    }

    private void showList() {
        rvExpenses.setVisibility(View.VISIBLE);
        tvEmptyState.setVisibility(View.GONE);
        btnLogFirst.setVisibility(View.GONE);
    }

    private void showEmpty() {
        rvExpenses.setVisibility(View.GONE);
        tvEmptyState.setVisibility(View.VISIBLE);
        btnLogFirst.setVisibility(View.VISIBLE);
    }

    @Override
    public void onEditClick(Expense expense, int position) {
        Intent intent = new Intent(this, ExpenseEntryActivity.class);
        intent.putExtra("expense_id",  expense.getExpenseId());
        intent.putExtra("amount",      expense.getAmount());
        intent.putExtra("category",    expense.getCategory());
        intent.putExtra("note",        expense.getNote());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(Expense expense, int position) {
        showDeleteConfirmation(expense, position);
    }

    private void showDeleteConfirmation(Expense expense, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Expense")
                .setMessage("Are you sure you want to delete this? This will update your daily limit.")
                .setPositiveButton("Delete", (dialog, which) -> {
                    db.deleteExpense(expense.getExpenseId());
                    BudgetCycle cycle = db.getActiveCycle();
                    if (cycle != null) {
                        float newBalance = cycle.getRemainingBalance() + expense.getAmount();
                        db.updateRemainingBalance(cycle.getId(), newBalance);
                    }
                    adapter.removeItem(position);
                    currentList.remove(position);
                    if (currentList.isEmpty()) showEmpty();
                    Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}
