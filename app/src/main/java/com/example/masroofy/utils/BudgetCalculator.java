package com.example.masroofy.utils;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class BudgetCalculator {

    /** Returns Safe Daily Limit = remainingBalance / remainingDays */
    public static float calcDailyLimit(float remainingBalance, String endDate) {
        long days = getRemainingDays(endDate);
        if (days <= 0) return remainingBalance;
        return remainingBalance / days;
    }

    /** Balance carries forward unchanged, daily limit recalculates automatically */
    public static float calcRollover(float remainingBalance, String endDate) {
        return remainingBalance;
    }

    /** Returns true if spent percentage >= thresholdPercent */
    public static boolean checkThreshold(float spent, float total, float thresholdPercent) {
        if (total == 0) return false;
        return (spent / total) * 100 >= thresholdPercent;
    }

    /** Returns how many days remain until endDate inclusive */
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
