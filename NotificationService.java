package com.example.masroofy;
import android.content.Context;
import android.widget.Toast;

public class NotificationService {

    public static void checkAndNotify(Context context, double spent, double total) {

        double percent = (spent / total) * 100;

        if (percent >= 80 && percent < 100) {
            sendWarning(context);
        } else if (percent >= 100) {
            sendExhausted(context);
        }
    }

    private static void sendWarning(Context context) {
        Toast.makeText(context, "Warning: You have used 80% of your budget!", Toast.LENGTH_LONG).show();
    }

    private static void sendExhausted(Context context) {
        Toast.makeText(context, "Budget exhausted!", Toast.LENGTH_LONG).show();
    }
}