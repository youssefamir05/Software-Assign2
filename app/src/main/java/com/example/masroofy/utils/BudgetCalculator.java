package com.example.masroofy.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Utility class for performing budget-related calculations.
 */
public class BudgetCalculator {

    /**
     * Calculates the safe daily spending limit based on the remaining balance and time.
     *
     * @param remainingBalance The current balance available.
     * @param endDate          The end date of the budget cycle (yyyy-MM-dd).
     * @return The amount that can be safely spent per day.
     */
    public static float calcDailyLimit(float remainingBalance, String endDate) {
        long days = getRemainingDays(endDate);
        if (days <= 0) return remainingBalance;
        return remainingBalance / days;
    }

    /**
     * Calculates the rollover balance for a new day.
     * Currently returns the balance unchanged.
     *
     * @param remainingBalance Current balance.
     * @param endDate          End date of the cycle.
     * @return The updated balance.
     */
    public static float calcRollover(float remainingBalance, String endDate) {
        return remainingBalance;
    }

    /**
     * Checks if the spent amount has reached or exceeded a certain percentage of the total budget.
     *
     * @param spent            Total amount spent.
     * @param total            Total budget allowance.
     * @param thresholdPercent The percentage threshold to check against.
     * @return True if the threshold is reached, false otherwise.
     */
    public static boolean checkThreshold(float spent, float total, float thresholdPercent) {
        if (total == 0) return false;
        return (spent / total) * 100 >= thresholdPercent;
    }

    /**
     * Calculates the number of days remaining until the end date, inclusive of today.
     *
     * @param endDate The end date (yyyy-MM-dd).
     * @return The number of remaining days. Returns at least 1 if parsing fails.
     */
    public static long getRemainingDays(String endDate) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date end   = sdf.parse(endDate);
            Date today = sdf.parse(sdf.format(new Date()));
            long diff  = end.getTime() - today.getTime();
            return TimeUnit.MILLISECONDS.toDays(diff) + 1;
        } catch (Exception e) {
            return 1;
        }
    }
}
