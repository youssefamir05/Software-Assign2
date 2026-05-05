package com.example.masroofy.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.masroofy.R;
import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.model.BudgetCycle;

import java.util.Calendar;
import java.util.Locale;

public class SetupActivity extends AppCompatActivity {

    private String startDate, endDate;
    private Button btnStartDate, btnEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        EditText etAllowance = findViewById(R.id.etAllowance);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate   = findViewById(R.id.btnEndDate);
        Button btnSave = findViewById(R.id.btnSave);

        btnStartDate.setOnClickListener(v -> showDatePicker(true));
        btnEndDate.setOnClickListener(v -> showDatePicker(false));

        btnSave.setOnClickListener(v -> {
            String amountStr = etAllowance.getText().toString().trim();

            if (amountStr.isEmpty()) {
                Toast.makeText(this, "Enter your allowance", Toast.LENGTH_SHORT).show();
                return;
            }
            if (startDate == null) {
                Toast.makeText(this, "Pick a start date", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endDate == null) {
                Toast.makeText(this, "Pick an end date", Toast.LENGTH_SHORT).show();
                return;
            }

            float allowance = Float.parseFloat(amountStr);

            if (allowance <= 0) {
                Toast.makeText(this, "Allowance must be greater than 0", Toast.LENGTH_SHORT).show();
                return;
            }
            if (endDate.compareTo(startDate) <= 0) {
                Toast.makeText(this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                return;
            }

            BudgetCycle cycle = new BudgetCycle(allowance, startDate, endDate);
            DatabaseHelper.getInstance(this).saveBudgetCycle(cycle);

            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        });
    }

    private void showDatePicker(boolean isStart) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(this, (view, year, month, day) -> {
            String date = String.format(Locale.getDefault(),
                    "%04d-%02d-%02d", year, month + 1, day);
            if (isStart) {
                startDate = date;
                btnStartDate.setText(date);
            } else {
                endDate = date;
                btnEndDate.setText(date);
            }
        },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)).show();
    }
}
