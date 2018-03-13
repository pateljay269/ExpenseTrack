package patel.jay.expensetrack.ExpenseManager.SQLs;

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

public class SqlUser implements Serializable {

    private final static String table = SQLiteHelper.TABLE_USER;
    private static String[] columns = {SQLiteHelper.COL_UID, SQLiteHelper.COL_USERNAME,
            SQLiteHelper.COL_PASS, SQLiteHelper.COL_PIN, SQLiteHelper.COL_ACNO};
    private static String selection = null;

    //region Getter Setter
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;
    private int uid, pin;

    //endregion
    private String user, pass, acNo;

    public SqlUser(int uid, String user, String pass, int pin, String acNo) {
        this(user, pass, pin, acNo);
        this.uid = uid;
    }

    public SqlUser(String user, String pass, int pin, String acNo) {
        this.pin = pin;
        this.user = user;
        this.pass = pass;
        this.acNo = acNo;
    }

    public static SqlUser checkUser(Activity activity, String userName, String pass) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_USERNAME + "='" + userName + "' And " + SQLiteHelper.COL_PASS + "='" + pass + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        SqlUser sqlUser = null;
        try {
            if (cursor.moveToFirst()) {
                sqlUser = cursorToSql(cursor);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlUser;
    }

    public static SqlUser checkUserPin(Activity activity, String pin) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_PIN + "='" + pin + "' AND NOT " + SQLiteHelper.COL_USERNAME + "='Admin'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        SqlUser sqlUser = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            if (cursor.moveToFirst()) {
                sqlUser = cursorToSql(cursor);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlUser;
    }

    public static SqlUser checkAdminPin(Activity activity, String pin) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_PIN + "='" + pin + "' AND " + SQLiteHelper.COL_USERNAME + "='" + MyConstants.ADMIN + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        SqlUser sqlUser = null;
        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        try {
            if (cursor.moveToFirst()) {
                sqlUser = cursorToSql(cursor);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlUser;
    }

    public static ArrayList<String> allUserString(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> sqlUsers = new ArrayList<>();
        sqlUsers.add("Customer");

        try {
            if (cursor.moveToFirst()) {
                do {
                    String userName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_USERNAME));
                    sqlUsers.add(userName);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlUsers;
    }

    public static SqlUser findDetail(Activity activity, String userName) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_USERNAME + "='" + userName + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        SqlUser sqlUser = null;

        try {
            if (cursor.moveToFirst()) {
                sqlUser = cursorToSql(cursor);
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlUser;
    }

    public static String[] findAcPass(Activity activity, String acNo) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACNO + "='" + acNo + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        String[] password = new String[3];

        try {
            if (cursor.moveToFirst()) {
                password[0] = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_USERNAME));
                password[1] = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_PASS));
                password[2] = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_PIN));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    private static SqlUser cursorToSql(Cursor cursor) {
        int uid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_UID));
        int pin = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_PIN));
        String user = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_USERNAME));
        String pass = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_PASS));
        String acNo = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_ACNO));

        return new SqlUser(uid, user, pass, pin, acNo);
    }

    public int getPin() {
        return pin;
    }

    public int getUid() {
        return uid;
    }

    public String getUser() {
        return user;
    }

    public String getPass() {
        return pass;
    }

    public String getAcNo() {
        return acNo;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_USERNAME, user);
        contentValues.put(SQLiteHelper.COL_PASS, pass);
        contentValues.put(SQLiteHelper.COL_PIN, pin);
        contentValues.put(SQLiteHelper.COL_ACNO, acNo);
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

        selection = SQLiteHelper.COL_UID + "='" + uid + "'";
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(activity, "This UserName Or Pin Already Register");
        }

        db.close();
        return value;
    }

}