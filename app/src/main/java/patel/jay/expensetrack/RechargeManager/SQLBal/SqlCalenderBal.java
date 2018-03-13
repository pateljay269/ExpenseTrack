package patel.jay.expensetrack.RechargeManager.SQLBal;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;

import static patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount.balAccounts;


/**
 * Created by Jay on 30-07-2017.
 */

public class SqlCalenderBal implements Serializable {

    private final static String table = SQLiteHelper.TABLE_TRANS;

    public static String[] columns = {SQLiteHelper.COL_TID, SQLiteHelper.COL_CUSTMOBILE,
            "SUM(" + SQLiteHelper.COL_EXPENSE + ") AS " + SQLiteHelper.COL_EXPENSE,
            "SUM(" + SQLiteHelper.COL_INCOME + ") AS " + SQLiteHelper.COL_INCOME,
            SQLiteHelper.COL_DSC, SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE,
            SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR, SQLiteHelper.COL_YYMMDD};

    private static String[] column = {SQLiteHelper.COL_TID};
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;

    public static ArrayList<SqlBalAccount> dayWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> monthWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> yearWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return balAccounts(db, cursor);
    }

    public static ArrayList<SqlBalAccount> totalWise(Activity activity) {
        Cursor cursor = null;
        ArrayList<SqlBalAccount> sqlBalAccounts = new ArrayList<>();

        try {
            cursor = totalCursor(activity);

            if (cursor.moveToFirst()) {
                do {
                    SqlBalAccount sqlBalAccount = cursorTotal(cursor);
                    sqlBalAccounts.add(sqlBalAccount);
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(activity, e.getMessage());
        }

        return sqlBalAccounts;
    }

    public static Cursor totalCursor(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String qry = "SELECT t." + SQLiteHelper.COL_CUSTMOBILE
                + ", SUM(" + SQLiteHelper.COL_EXPENSE + ") AS " + SQLiteHelper.COL_EXPENSE
                + ", SUM(" + SQLiteHelper.COL_INCOME + ") AS " + SQLiteHelper.COL_INCOME
                + " FROM "
                + SQLiteHelper.TABLE_TRANS + " AS t ,"
                + SQLiteHelper.TABLE_CUST + " AS c "
                + " WHERE t." + SQLiteHelper.COL_CUSTMOBILE + " = c." + SQLiteHelper.COL_CUSTMOBILE
                + " GROUP BY t." + SQLiteHelper.COL_CUSTMOBILE
                + " ORDER BY c." + SQLiteHelper.COL_CUSTNAME + ";";

        return db.rawQuery(qry, null);
    }

    private static SqlBalAccount cursorTotal(Cursor cursor) {
//        int tid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_TID));
        String ex = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EXPENSE));
        String in = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCOME));
//        String dsc = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));
        String time = System.currentTimeMillis() + "";// cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
//        String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
//        String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
//        String year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
//        String yymmdd = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YYMMDD));
        String mobile = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));

        return new SqlBalAccount(0, mobile, "", time, "", "", "", "", ex, in);
    }

}