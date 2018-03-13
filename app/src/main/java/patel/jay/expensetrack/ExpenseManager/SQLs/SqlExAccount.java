package patel.jay.expensetrack.ExpenseManager.SQLs;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;

/**
 * Created by Jay on 8/26/2017.
 */

public class SqlExAccount implements Serializable {

    private final static String table = SQLiteHelper.TABLE_ACCOUNT;
    private static String[] columns = {SQLiteHelper.COL_ACID, SQLiteHelper.COL_ACCNAME};
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;
    private String accName;
    private int acid;
    public SqlExAccount(String accName) {
        this.accName = accName;
    }
    public SqlExAccount(int acid, String accName) {
        this(accName);
        this.acid = acid;
    }

    public static String accString(Activity context, int acid) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        String accname = "";
        if (cursor.moveToFirst()) {
            accname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_ACCNAME));
        }
        db.close();
        cursor.close();
        return accname;
    }

    public static int accInt(Activity context, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACCNAME + "='" + accName + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        int acid = 0;
        if (cursor.moveToFirst()) {
            acid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_ACID));
        }
        db.close();
        cursor.close();
        return acid;
    }

    public static ArrayList<SqlExAccount> allAcc(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_ACCNAME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<SqlExAccount> sqlExAccounts = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    SqlExAccount sqlExAccount = cursorTo(cursor);
                    sqlExAccounts.add(sqlExAccount);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlExAccounts;
    }

    public static ArrayList<String> allAccString(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_ACCNAME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> sqlList = new ArrayList<>();
        sqlList.add(SQLiteHelper.CAT_AC);

        if (cursor.moveToFirst()) {
            do {
                String catname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_ACCNAME));
                sqlList.add(catname);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return sqlList;
    }

    private static SqlExAccount cursorTo(Cursor cursor) {
        int acid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_ACID));
        String accName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_ACCNAME));

        return new SqlExAccount(acid, accName);
    }

    public String getAccName() {
        return accName;
    }

    public int getAcid() {
        return acid;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_ACCNAME, accName);
        return contentValues;
    }

    public long insert(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(table, null, getValues());
        db.close();
        return value;
    }

    public long update(Activity activity) {

        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(activity, "This UserName Already Register");
        }

        db.close();
        return value;
    }

    public long delete(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        selectionArgs = null;
        long value = 0;

        try {
            value = db.delete(table, selection, selectionArgs);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }
        db.close();
        return value;
    }

}
