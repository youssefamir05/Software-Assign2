package com.example.masroofy.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.masroofy.model.BudgetCycle;
import com.example.masroofy.model.Expense;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper implements IDataRepository {

    private static final String DB_NAME = "masroofy.db";
    private static final int DB_VERSION = 1;
    private static DatabaseHelper instance;

    public static final String TABLE_CYCLE    = "budget_cycles";
    public static final String COL_CYCLE_ID   = "cycle_id";
    public static final String COL_ALLOWANCE  = "allowance";
    public static final String COL_START_DATE = "start_date";
    public static final String COL_END_DATE   = "end_date";
    public static final String COL_REMAINING  = "remaining_balance";
    public static final String TABLE_EXPENSE  = "expenses";

    private DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    /** Singleton — always use this to get the database */
    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new DatabaseHelper(context.getApplicationContext());
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CYCLE + " (" +
                COL_CYCLE_ID   + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_ALLOWANCE  + " REAL NOT NULL, " +
                COL_START_DATE + " TEXT NOT NULL, " +
                COL_END_DATE   + " TEXT NOT NULL, " +
                COL_REMAINING  + " REAL NOT NULL);"
        );

        db.execSQL("CREATE TABLE " + TABLE_EXPENSE + " (" +
                "expense_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "amount REAL NOT NULL, " +
                "category TEXT NOT NULL, " +
                "timestamp TEXT NOT NULL, " +
                "cycle_id INTEGER, " +
                "note TEXT, " +
                "FOREIGN KEY(cycle_id) REFERENCES " + TABLE_CYCLE + "(" + COL_CYCLE_ID + "));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CYCLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXPENSE);
        onCreate(db);
    }

    @Override
    public void saveBudgetCycle(BudgetCycle cycle) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_ALLOWANCE,  cycle.getAllowance());
        cv.put(COL_START_DATE, cycle.getStartDate());
        cv.put(COL_END_DATE,   cycle.getEndDate());
        cv.put(COL_REMAINING,  cycle.getRemainingBalance());
        db.insert(TABLE_CYCLE, null, cv);
    }

    @Override
    public BudgetCycle getActiveCycle() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_CYCLE, null, null, null,
                null, null, COL_CYCLE_ID + " DESC", "1");
        if (c.moveToFirst()) {
            BudgetCycle cycle = new BudgetCycle(
                    c.getFloat(c.getColumnIndexOrThrow(COL_ALLOWANCE)),
                    c.getString(c.getColumnIndexOrThrow(COL_START_DATE)),
                    c.getString(c.getColumnIndexOrThrow(COL_END_DATE))
            );
            cycle.setId(c.getInt(c.getColumnIndexOrThrow(COL_CYCLE_ID)));
            cycle.setRemainingBalance(c.getFloat(c.getColumnIndexOrThrow(COL_REMAINING)));
            c.close();
            return cycle;
        }
        c.close();
        return null;
    }

    @Override
    public void updateRemainingBalance(int cycleId, float newBalance) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_REMAINING, newBalance);
        db.update(TABLE_CYCLE, cv, COL_CYCLE_ID + "=?",
                new String[]{String.valueOf(cycleId)});
    }

    @Override
    public void insertExpense(Expense expense) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("amount",    expense.getAmount());
        cv.put("category",  expense.getCategory());
        cv.put("timestamp", expense.getTimestamp());
        cv.put("cycle_id",  expense.getCycleId());
        cv.put("note",      expense.getNote());
        db.insert(TABLE_EXPENSE, null, cv);
    }

    @Override
    public List<Expense> getAllExpenses(int cycleId) {
        List<Expense> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(TABLE_EXPENSE, null, "cycle_id=?",
                new String[]{String.valueOf(cycleId)},
                null, null, "timestamp DESC");
        while (c.moveToNext()) {
            Expense e = new Expense(
                    c.getFloat(c.getColumnIndexOrThrow("amount")),
                    c.getString(c.getColumnIndexOrThrow("category")),
                    c.getInt(c.getColumnIndexOrThrow("cycle_id"))
            );
            e.setExpenseId(c.getInt(c.getColumnIndexOrThrow("expense_id")));
            e.setTimestamp(c.getString(c.getColumnIndexOrThrow("timestamp")));
            e.setNote(c.getString(c.getColumnIndexOrThrow("note")));
            list.add(e);
        }
        c.close();
        return list;
    }

    @Override
    public void deleteExpense(int expenseId) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXPENSE, "expense_id=?",
                new String[]{String.valueOf(expenseId)});
    }
}