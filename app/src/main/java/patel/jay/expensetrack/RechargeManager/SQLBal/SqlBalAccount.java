package patel.jay.expensetrack.RechargeManager.SQLBal;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;

/**
 * Created by Jay on 09-08-2017.
 */

public class SqlBalAccount implements Serializable {

    private final static String table = SQLiteHelper.TABLE_TRANS;
    private static String[] columns = {SQLiteHelper.COL_TID, SQLiteHelper.COL_CUSTMOBILE, SQLiteHelper.COL_EXPENSE,
            SQLiteHelper.COL_INCOME, SQLiteHelper.COL_DSC, SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE,
            SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR, SQLiteHelper.COL_YYMMDD};
    private static String[] column = {SQLiteHelper.COL_TID};
    private static String selection = null;

    //region Getter Setter
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;
    private int tid;    //primary key
    //    private int cid;
    private String custName;
    private String expense, income, dsc, time, date, month, year, yymmdd;

    public SqlBalAccount(String custName, String dsc, String time, String date, String month,
                         String year, String yymmdd, String expense, String income) {
        this.expense = expense;
        this.income = income;
        this.dsc = dsc;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.yymmdd = yymmdd;
        this.custName = custName;
    }

    public SqlBalAccount(int tid, String custName, String dsc, String time, String date,
                         String month, String year, String yymmdd, String expense, String income) {
        this(custName, dsc, time, date, month, year, yymmdd, expense, income);
        this.tid = tid;
    }

    public static int countCid(Activity context, String mobile) {
        int count = 0;

        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + mobile + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, column, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }

    public static ArrayList<SqlBalAccount> allDetails(Activity context, String type) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = type + "='" + 0 + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> custWise(Activity activity, String mobile) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + mobile + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> findWise(Activity context, long startTime, long endTime) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YYMMDD + " BETWEEN '" + startTime + "' AND '" + endTime + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> calenderData(Activity activity, String data, String flag) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String type = "";
        switch (flag) {
            case MyConstants.day:
                type = SQLiteHelper.COL_DATE;
                break;

            case MyConstants.month:
                type = SQLiteHelper.COL_MONTH;
                break;

            case MyConstants.year:
                type = SQLiteHelper.COL_YEAR;
                break;

            case MyConstants.Total:
                type = SQLiteHelper.COL_CUSTMOBILE;
                break;
        }

        selection = type + "='" + data + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> balAccounts(SQLiteDatabase db, Cursor cursor) {
        ArrayList<SqlBalAccount> sqlBalAccounts = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    SqlBalAccount sqlBalAccount = cursorTo(cursor);
                    sqlBalAccounts.add(sqlBalAccount);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlBalAccounts;
    }

    public static Cursor allDetailCursor(Activity context, String mobile) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + mobile + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    private static SqlBalAccount cursorTo(Cursor cursor) {
        int tid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_TID));
        String ex = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EXPENSE));
        String in = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCOME));
        String dsc = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));
        String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
        String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
        String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
        String year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
        String yymmdd = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YYMMDD));
        String mobile = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));

        return new SqlBalAccount(tid, mobile, dsc, time, date, month, year, yymmdd, ex, in);
    }

    public int getTid() {
        return tid;
    }

    public void setTid(int tid) {
        this.tid = tid;
    }

    public String getExpense() {
        return expense;
    }

    //endregion

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getIncome() {
        return income;
    }

    public void setIncome(String income) {
        this.income = income;
    }

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getYymmdd() {
        return yymmdd;
    }

    public void setYymmdd(String yymmdd) {
        this.yymmdd = yymmdd;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_CUSTMOBILE, custName);
        contentValues.put(SQLiteHelper.COL_EXPENSE, expense);
        contentValues.put(SQLiteHelper.COL_INCOME, income);
        contentValues.put(SQLiteHelper.COL_DSC, dsc);
        contentValues.put(SQLiteHelper.COL_TIME, time);
        contentValues.put(SQLiteHelper.COL_DATE, date);
        contentValues.put(SQLiteHelper.COL_MONTH, month);
        contentValues.put(SQLiteHelper.COL_YEAR, year);
        contentValues.put(SQLiteHelper.COL_YYMMDD, yymmdd);
        return contentValues;
    }

    private ContentValues getTypeChange() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_CUSTMOBILE, custName);
        contentValues.put(SQLiteHelper.COL_EXPENSE, income);
        contentValues.put(SQLiteHelper.COL_INCOME, expense);
        contentValues.put(SQLiteHelper.COL_DSC, dsc);
        contentValues.put(SQLiteHelper.COL_TIME, time);
        contentValues.put(SQLiteHelper.COL_DATE, date);
        contentValues.put(SQLiteHelper.COL_MONTH, month);
        contentValues.put(SQLiteHelper.COL_YEAR, year);
        contentValues.put(SQLiteHelper.COL_YYMMDD, yymmdd);
        return contentValues;
    }

    public long insert(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(table, null, getValues());
        db.close();
        return value;
    }

    public long update(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_TID + "=" + tid;
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(context, e.toString());
        }

        db.close();
        return value;
    }

    public long delete(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_TID + "=" + tid;
        selectionArgs = null;
        long value = 0;

        try {
            value = db.delete(table, selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(context, e.toString());
        }
        db.close();
        return value;
    }

    public long updateTypeChange(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_TID + "=" + tid;
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getTypeChange(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(context, e.toString());
        }

        db.close();
        return value;
    }
}