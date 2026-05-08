package com.example.masroofy.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.masroofy.R;
import com.example.masroofy.model.Expense;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExpenseAdapter extends RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder> {

    public interface OnExpenseActionListener {
        void onEditClick(Expense expense, int position);
        void onDeleteClick(Expense expense, int position);
    }

    private List<Expense>           expenses;
    private OnExpenseActionListener listener;

    public ExpenseAdapter(List<Expense> expenses, OnExpenseActionListener listener) {
        this.expenses = expenses != null ? expenses : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_expense, parent, false);
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        holder.bind(expenses.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return expenses.size();
    }

    public void updateList(List<Expense> newList) {
        this.expenses = newList != null ? newList : new ArrayList<>();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (position >= 0 && position < expenses.size()) {
            expenses.remove(position);
            notifyItemRemoved(position);
        }
    }

    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvCategoryBadge;
        private final TextView tvTimestamp;
        private final TextView tvAmount;
        private final TextView tvNote;

        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryBadge = itemView.findViewById(R.id.tvCategoryBadge);
            tvTimestamp     = itemView.findViewById(R.id.tvTimestamp);
            tvAmount        = itemView.findViewById(R.id.tvAmount);
            tvNote          = itemView.findViewById(R.id.tvNote);
        }

        public void bind(Expense expense, OnExpenseActionListener listener) {
            String cat = expense.getCategory();
            tvCategoryBadge.setText(cat != null && !cat.isEmpty()
                    ? String.valueOf(cat.charAt(0)).toUpperCase(Locale.getDefault())
                    : "?");

            String ts = expense.getTimestamp();
            tvTimestamp.setText(ts != null ? ts : "—");

            tvAmount.setText(String.format(Locale.getDefault(), "–%.0f EGP", expense.getAmount()));

            String note = expense.getNote();
            if (tvNote != null) {
                if (note != null && !note.isEmpty()) {
                    tvNote.setVisibility(View.VISIBLE);
                    tvNote.setText(note);
                } else {
                    tvNote.setVisibility(View.GONE);
                }
            }

            itemView.setOnClickListener(v -> {
                if (listener != null) listener.onEditClick(expense, getAdapterPosition());
            });

            itemView.setOnLongClickListener(v -> {
                if (listener != null) listener.onDeleteClick(expense, getAdapterPosition());
                return true;
            });
        }
    }
}
