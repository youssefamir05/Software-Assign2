package com.example.masroofy;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "masroofy.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE expenses (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "amount REAL," +
                "category TEXT," +
                "timestamp INTEGER" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM expenses", null);
    }

    public boolean insertExpense(double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("amount", amount);
        values.put("category", category);

        long result = db.insert("expenses", null, values);

        return result != -1;
    }

    // Insert Expense
    public void insertExpense(Expense expense) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("amount", expense.getAmount());
        values.put("category", expense.getCategory().getName());
        values.put("timestamp", expense.getTimestamp());

        db.insert("expenses", null, values);
        db.close();
    }

    // Get total spent
    public double getTotalSpent() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(amount) FROM expenses", null);

        double total = 0;

        if (cursor.moveToFirst()) {
            total = cursor.getDouble(0);
        }

        cursor.close();
        return total;
    }

}