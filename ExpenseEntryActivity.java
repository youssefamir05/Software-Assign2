package com.example.masroofy;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExpenseEntryActivity extends AppCompatActivity {

    EditText amountInput;
    Button saveBtn;
    Spinner categorySpinner;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_entry);

        amountInput = findViewById(R.id.amountInput);
        saveBtn = findViewById(R.id.saveBtn);
        categorySpinner = findViewById(R.id.categorySpinner);

        dbHelper = new DatabaseHelper(this);

        // Categories
        String[] categories = {"Food", "Transport", "Shopping"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String amountStr = amountInput.getText().toString();
                String category = categorySpinner.getSelectedItem().toString();

                // Validation
                if (amountStr.isEmpty()) {
                    amountInput.setError("Enter amount");
                    return;
                }

                double amount = Double.parseDouble(amountStr);

                if (amount <= 0) {
                    amountInput.setError("Amount must be greater than 0");
                    return;
                }

                // Save to DB
                boolean inserted = dbHelper.insertExpense(amount, category);

                if (inserted) {
                    Toast.makeText(ExpenseEntryActivity.this,
                            "Expense Saved", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ExpenseEntryActivity.this,
                            "Error saving expense", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}