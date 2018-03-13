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
 * Created by Jay on 8/16/2017.
 */

public class SqlRecharge implements Serializable {

    private final static String table = SQLiteHelper.TABLE_RECHARGE;
    private static String[] columns = {SQLiteHelper.COL_RID, SQLiteHelper.COL_RECHARGE,
            SQLiteHelper.COL_INCENTIVE, SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE,
            SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR};

    //region Getter Setter
    private static String[] columnCursor = {SQLiteHelper.COL_RID, SQLiteHelper.COL_YEAR,
            "SUM(" + SQLiteHelper.COL_INCENTIVE + ") AS " + SQLiteHelper.COL_INCENTIVE,
            "SUM(" + SQLiteHelper.COL_RECHARGE + ") AS " + SQLiteHelper.COL_RECHARGE,
            SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE, SQLiteHelper.COL_MONTH};
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;
    private int rid;
    private String recharge, incentive, time, date, month, year;

    public SqlRecharge(int rid, String recharge, String incentive, String time, String date, String month, String year) {
        this(recharge, incentive, time, date, month, year);
        this.rid = rid;
    }

    public SqlRecharge(String recharge, String incentive, String time, String date, String month, String year) {
        this.recharge = recharge;
        this.incentive = incentive;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
    }

    public static ArrayList<SqlRecharge> allDetails(Activity context, boolean type) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        having = null;
        groupBy = null;
        orderBy = SQLiteHelper.COL_TIME;

        String[] cols;
        if (type) {
            cols = columnCursor;
            groupBy = SQLiteHelper.COL_YEAR;
            orderBy = null;
        } else {
            cols = columns;
        }

        Cursor cursor = db.query(table, cols, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

    public static ArrayList<SqlRecharge> allDetailsYr(Activity context, String year) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YEAR + "='" + year + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

    public static ArrayList<String> allYearString(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_TIME + " DESC";

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select");

        if (cursor.moveToFirst()) {
            do {
                String monthName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
                list.add(monthName);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list;
    }

    public static ArrayList<String> monthArray(Activity context, String year) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YEAR + "='" + year + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME + " DESC";

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> monthArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
                    monthArray.add(month);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return monthArray;
    }
    //endregion

    public static Cursor allDetailCursor(Activity context, String month) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_MONTH + "='" + month + "'";// And " + SQLiteHelper.COL_MONTH + "='" + month + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME + " DESC";

        return db.query(table, columnCursor, selection, selectionArgs, groupBy, having, orderBy);
    }

    public static SqlRecharge yearWiseTotal(Activity activity, String year) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YEAR + "='" + year + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columnCursor, selection, selectionArgs, groupBy, having, orderBy);

        SqlRecharge sqlRecharge = null;
        try {
            if (cursor.moveToFirst()) {
                do {
                    sqlRecharge = cursorToSql(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlRecharge;
    }

    public static SqlRecharge totalWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columnCursor, selection, selectionArgs, groupBy, having, orderBy);

        SqlRecharge sqlRecharge = null;
        try {
            if (cursor.moveToFirst()) {
                do {
                    sqlRecharge = cursorToSql(cursor);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlRecharge;
    }

    public static ArrayList<SqlRecharge> recharges(SQLiteDatabase db, Cursor cursor) {
        ArrayList<SqlRecharge> sqlRecharges = new ArrayList<>();
        try {
            assert cursor != null;
            if (cursor.moveToFirst()) {
                do {
                    SqlRecharge sqlRecharge = cursorToSql(cursor);
                    sqlRecharges.add(sqlRecharge);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlRecharges;
    }

    private static SqlRecharge cursorToSql(Cursor cursor) {
        int rid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_RID));
        String recharge = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_RECHARGE));
        String incentive = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCENTIVE));
        String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
        String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
        String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
        String year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));

        return new SqlRecharge(rid, recharge, incentive, time, date, month, year);
    }

    public static ArrayList<SqlRecharge> calenderData(Activity activity, String data, String flag) {
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
        }

        selection = type + "='" + data + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

    public int getRid() {
        return rid;
    }

    public void setRid(int rid) {
        this.rid = rid;
    }

    public String getRecharge() {
        return recharge;
    }

    public void setRecharge(String recharge) {
        this.recharge = recharge;
    }

    public String getIncentive() {
        return incentive;
    }

    public void setIncentive(String incentive) {
        this.incentive = incentive;
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

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_RECHARGE, recharge);
        contentValues.put(SQLiteHelper.COL_INCENTIVE, incentive);
        contentValues.put(SQLiteHelper.COL_TIME, time);
        contentValues.put(SQLiteHelper.COL_DATE, date);
        contentValues.put(SQLiteHelper.COL_MONTH, month);
        contentValues.put(SQLiteHelper.COL_YEAR, year);
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

        selection = SQLiteHelper.COL_RID + "=" + rid;
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

    public long delete(Activity context, int rid) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_RID + "=" + rid;
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

}