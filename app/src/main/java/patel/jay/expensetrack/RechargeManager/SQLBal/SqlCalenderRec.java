package patel.jay.expensetrack.RechargeManager.SQLBal;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.SQLiteHelper;

import static patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge.recharges;

/**
 * Created by Jay on 8/20/2017.
 */

public class SqlCalenderRec {

    private final static String table = SQLiteHelper.TABLE_RECHARGE;

    public static String[] columns = {SQLiteHelper.COL_RID, SQLiteHelper.COL_YEAR,
            "SUM(" + SQLiteHelper.COL_INCENTIVE + ") AS " + SQLiteHelper.COL_INCENTIVE,
            "SUM(" + SQLiteHelper.COL_RECHARGE + ") AS " + SQLiteHelper.COL_RECHARGE,
            SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE, SQLiteHelper.COL_MONTH};

    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;

    public static ArrayList<SqlRecharge> dayWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

    public static ArrayList<SqlRecharge> monthWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

    public static ArrayList<SqlRecharge> yearWise(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return recharges(db, cursor);
    }

}