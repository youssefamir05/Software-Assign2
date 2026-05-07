package com.example.masroofy.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.masroofy.R;
import com.example.masroofy.data.DatabaseHelper;
import com.example.masroofy.ui.history.HistoryActivity;

import java.util.Locale;

public class DashboardActivity extends AppCompatActivity {

    private TextView      tvDailyLimit;
    private TextView      tvRemainingBalance;
    private TextView      tvAllowanceSummary;
    private TextView      tvFinalDayBadge;
    private TextView      tvEmptyState;
    private PieChartView  pieChartView;
    private Button        btnLogExpense;
    private Button        btnHistory;

    private DashboardViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        bindViews();
        initViewModel();
        setupClickListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAndRender();
    }

    private void bindViews() {
        tvDailyLimit        = findViewById(R.id.tvDailyLimit);
        tvRemainingBalance  = findViewById(R.id.tvRemainingBalance);
        tvAllowanceSummary  = findViewById(R.id.tvAllowanceSummary);
        tvFinalDayBadge     = findViewById(R.id.tvFinalDayBadge);
        tvEmptyState        = findViewById(R.id.tvEmptyState);
        pieChartView        = findViewById(R.id.pieChartView);
        btnLogExpense       = findViewById(R.id.btnLogExpense);
        btnHistory          = findViewById(R.id.btnHistory);
    }

    private void initViewModel() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        viewModel = new DashboardViewModel(db);
    }

    private void setupClickListeners() {
        btnLogExpense.setOnClickListener(v ->
                startActivity(new Intent(this,
                        com.example.masroofy.ui.expense.ExpenseEntryActivity.class))
        );

        btnHistory.setOnClickListener(v ->
                startActivity(new Intent(this, HistoryActivity.class))
        );
    }

    private void loadAndRender() {
        viewModel.loadData();

        if (!viewModel.hasCycle()) {
            startActivity(new Intent(this,
                    com.example.masroofy.ui.setup.SetupActivity.class));
            finish();
            return;
        }

        renderDailyLimit();
        renderBalanceSummary();
        renderFinalDayBadge();
        renderPieChart();
    }

    private void renderDailyLimit() {
        float limit = viewModel.getDailyLimit();
        tvDailyLimit.setText(String.format(Locale.getDefault(), "%.0f EGP", limit));
    }

    private void renderBalanceSummary() {
        float remaining = viewModel.getRemainingBalance();
        float allowance = viewModel.getTotalAllowance();
        float spent     = allowance - remaining;

        tvRemainingBalance.setText(
                String.format(Locale.getDefault(), "Remaining: %.0f EGP", remaining));
        tvAllowanceSummary.setText(
                String.format(Locale.getDefault(), "Spent %.0f of %.0f EGP", spent, allowance));
    }

    private void renderFinalDayBadge() {
        tvFinalDayBadge.setVisibility(viewModel.isFinalDay() ? View.VISIBLE : View.GONE);
    }

    private void renderPieChart() {
        if (viewModel.hasNoExpenses()) {
            pieChartView.setVisibility(View.GONE);
            tvEmptyState.setVisibility(View.VISIBLE);
        } else {
            tvEmptyState.setVisibility(View.GONE);
            pieChartView.setVisibility(View.VISIBLE);
            pieChartView.setData(viewModel.getCategoryTotals());
        }
    }
}
