package com.example.masroofy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.ui.DashboardActivity;
import com.example.masroofy.ui.SetupActivity;
import com.example.masroofy.utils.BudgetCalculator;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DatabaseHelper db = DatabaseHelper.getInstance(this);
        BudgetCycle cycle = db.getActiveCycle();

        if (cycle == null) {
            // No budget yet → go to Setup
            startActivity(new Intent(this, SetupActivity.class));
        } else {
            // Check if new day → do rollover
            SharedPreferences prefs = getPreferences(MODE_PRIVATE);
            String lastOpen = prefs.getString("last_open_date", "");
            String today = new SimpleDateFormat("yyyy-MM-dd",
                    Locale.getDefault()).format(new Date());

            if (!today.equals(lastOpen)) {
                float newBalance = BudgetCalculator.calcRollover(
                        cycle.getRemainingBalance(), cycle.getEndDate());
                db.updateRemainingBalance(cycle.getId(), newBalance);
                prefs.edit().putString("last_open_date", today).apply();
            }

            // Go to Dashboard
            startActivity(new Intent(this, DashboardActivity.class));
        }

        finish();
    }
}