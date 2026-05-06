package com.example.masroofy.ui.history;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.example.masroofy.R;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class FilterDialogFragment extends DialogFragment {

    public interface OnFilterAppliedListener {
        void onFilterApplied(FilterCriteria criteria);
    }

    private OnFilterAppliedListener listener;
    private String selectedFromDate = "";
    private String selectedToDate   = "";

    public void setOnFilterAppliedListener(OnFilterAppliedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(requireContext())
                .inflate(R.layout.dialog_filter, null);

        Spinner  spinnerCategory = view.findViewById(R.id.spinnerCategory);
        TextView tvFromDate      = view.findViewById(R.id.tvFromDate);
        TextView tvToDate        = view.findViewById(R.id.tvToDate);
        Button   btnApply        = view.findViewById(R.id.btnApplyFilter);
        Button   btnClear        = view.findViewById(R.id.btnClearFilter);

        List<String> categories = Arrays.asList(
                "All", "Food", "Transport", "Entertainment", "Health", "Shopping", "Bills", "Other");
        ArrayAdapter<String> catAdapter = new ArrayAdapter<>(
                requireContext(), android.R.layout.simple_spinner_item, categories);
        catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(catAdapter);

        tvFromDate.setOnClickListener(v -> showDatePicker(date -> {
            selectedFromDate = date;
            tvFromDate.setText(date);
        }));

        tvToDate.setOnClickListener(v -> showDatePicker(date -> {
            selectedToDate = date;
            tvToDate.setText(date);
        }));

        btnApply.setOnClickListener(v -> {
            String cat = spinnerCategory.getSelectedItem().toString();
            FilterCriteria criteria = new FilterCriteria(
                    cat.equals("All") ? "" : cat,
                    selectedFromDate,
                    selectedToDate
            );
            if (listener != null) listener.onFilterApplied(criteria);
            dismiss();
        });

        btnClear.setOnClickListener(v -> {
            if (listener != null) listener.onFilterApplied(new FilterCriteria());
            dismiss();
        });

        return new android.app.AlertDialog.Builder(requireContext())
                .setTitle("Filter Transactions")
                .setView(view)
                .create();
    }

    private void showDatePicker(DateCallback callback) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(), (picker, year, month, day) -> {
            String date = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, day);
            callback.onDateSelected(date);
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show();
    }

    private interface DateCallback {
        void onDateSelected(String date);
    }
}
