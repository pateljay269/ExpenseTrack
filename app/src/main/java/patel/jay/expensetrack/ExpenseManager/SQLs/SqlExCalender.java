package patel.jay.expensetrack.ExpenseManager.SQLs;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.SQLiteHelper;


/**
 * Created by Jay on 30-07-2017.
 */

public class SqlExCalender implements Serializable {

    public final static String[] columns = new String[]{SQLiteHelper.COL_AID,
            "SUM(" + SQLiteHelper.COL_EAMOUNT + ") AS " + SQLiteHelper.COL_EAMOUNT,
            "SUM(" + SQLiteHelper.COL_IAMOUNT + ") AS " + SQLiteHelper.COL_IAMOUNT,
            SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE, SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR,
            SQLiteHelper.COL_YYMMDD, SQLiteHelper.COL_WEEK, SQLiteHelper.COL_CATYPE,
            SQLiteHelper.COL_DSC, SQLiteHelper.COL_CID, SQLiteHelper.COL_ACID};
    private static final String table = SQLiteHelper.TABLE_INOUTCOME;
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;

    public static ArrayList<SqlExTrans> dayWise(Activity context, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equalsIgnoreCase(SQLiteHelper.ALL)) {
            selection = null;
        } else {
            int acid = SqlExAccount.accInt(context, accName);
            selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        }
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> weekWise(Activity context, String accName) {

        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equalsIgnoreCase(SQLiteHelper.ALL)) {
            selection = null;
        } else {
            int acid = SqlExAccount.accInt(context, accName);
            selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        }
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_WEEK;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> monthWise(Activity context, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equalsIgnoreCase(SQLiteHelper.ALL)) {
            selection = null;
        } else {
            int acid = SqlExAccount.accInt(context, accName);
            selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        }
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> yearWise(Activity context, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equalsIgnoreCase(SQLiteHelper.ALL)) {
            selection = null;
        } else {
            int acid = SqlExAccount.accInt(context, accName);
            selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        }
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> catWise(Activity context, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equals(SQLiteHelper.ALL)) {
            selection = null;
        } else {
            int acid = SqlExAccount.accInt(context, accName);
            selection = SQLiteHelper.COL_ACID + "='" + acid + "' AND " + SQLiteHelper.COL_CATYPE + "='" + SQLiteHelper.CAT_EX + "'";
        }
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_CID;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return SqlExTrans.sqlTranses(db, cursor);
    }

    private static ArrayList<SqlExTrans> sqlTranses(SQLiteDatabase db, Cursor cursor) {
        ArrayList<SqlExTrans> sqlAccounts = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    SqlExTrans sqlAccount = cursorTo(cursor);
                    sqlAccounts.add(sqlAccount);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlAccounts;
    }

    public static SqlExTrans cursorTo(Cursor cursor) {
        int aid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_AID));
        String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EAMOUNT));
        String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_IAMOUNT));
        String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
        String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
        String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
        String year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
        String yymmdd = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YYMMDD));
        String weekN = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_WEEK));
        String caType = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATYPE));

        return new SqlExTrans(aid, ex_rs, in_rs, time, date, month, year, yymmdd, weekN, caType);
    }

}