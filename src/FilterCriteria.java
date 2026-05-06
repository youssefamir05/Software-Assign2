package com.example.masroofy.ui.history;

import com.example.masroofy.model.Expense;

public class FilterCriteria {

    private String selectedCategory;
    private String fromDate;
    private String toDate;

    public FilterCriteria() { }

    public FilterCriteria(String selectedCategory) {
        this.selectedCategory = selectedCategory;
    }

    public FilterCriteria(String selectedCategory, String fromDate, String toDate) {
        this.selectedCategory = selectedCategory;
        this.fromDate         = fromDate;
        this.toDate           = toDate;
    }

    public boolean matches(Expense expense) {
        if (expense == null) return false;

        if (selectedCategory != null && !selectedCategory.isEmpty()) {
            if (!expense.getCategory().equalsIgnoreCase(selectedCategory)) return false;
        }

        String expDate = expense.getTimestamp() == null ? "" : expense.getTimestamp().substring(0, 10);

        if (fromDate != null && !fromDate.isEmpty()) {
            if (expDate.compareTo(fromDate) < 0) return false;
        }
        if (toDate != null && !toDate.isEmpty()) {
            if (expDate.compareTo(toDate) > 0) return false;
        }

        return true;
    }

    public boolean isEmpty() {
        return (selectedCategory == null || selectedCategory.isEmpty())
                && (fromDate == null || fromDate.isEmpty())
                && (toDate == null || toDate.isEmpty());
    }

    public String getSelectedCategory()                        { return selectedCategory; }
    public void setSelectedCategory(String selectedCategory)   { this.selectedCategory = selectedCategory; }
    public String getFromDate()                                { return fromDate; }
    public void setFromDate(String fromDate)                   { this.fromDate = fromDate; }
    public String getToDate()                                  { return toDate; }
    public void setToDate(String toDate)                       { this.toDate = toDate; }
}
