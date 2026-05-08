package com.example.masroofy.ui;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masroofy.R;
import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ExpenseEntryActivity extends AppCompatActivity {

    private EditText amountInput;
    private Spinner categorySpinner;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        amountInput = findViewById(R.id.amountInput);
        categorySpinner = findViewById(R.id.categorySpinner);
        Button saveBtn = findViewById(R.id.saveBtn);

        dbHelper = DatabaseHelper.getInstance(this);

        String[] categories = {"Food", "Transport", "Shopping", "Entertainment", "Other"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        saveBtn.setOnClickListener(v -> saveExpense());
    }

    private void saveExpense() {
        String amountStr = amountInput.getText().toString().trim();
        if (amountStr.isEmpty()) {
            amountInput.setError("Enter amount");
            return;
        }

        float amount = Float.parseFloat(amountStr);
        String category = categorySpinner.getSelectedItem().toString();

        BudgetCycle cycle = dbHelper.getActiveCycle();
        if (cycle == null) {
            Toast.makeText(this, "No active budget cycle found", Toast.LENGTH_SHORT).show();
            return;
        }

        if (amount > cycle.getRemainingBalance()) {
            amountInput.setError("Amount exceeds remaining budget!");
            Toast.makeText(this, "Insufficient balance!", Toast.LENGTH_LONG).show();
            return;
        }

        // Create expense
        Expense expense = new Expense(amount, category, cycle.getId());
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        expense.setTimestamp(timestamp);

        // Save expense
        dbHelper.insertExpense(expense);

        // Update balance
        float newBalance = cycle.getRemainingBalance() - amount;
        dbHelper.updateRemainingBalance(cycle.getId(), newBalance);

        Toast.makeText(this, "Expense Saved", Toast.LENGTH_SHORT).show();
        finish();
    }
}
