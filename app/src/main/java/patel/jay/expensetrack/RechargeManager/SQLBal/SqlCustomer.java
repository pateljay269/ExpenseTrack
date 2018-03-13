package patel.jay.expensetrack.RechargeManager.SQLBal;

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
 * Created by Jay on 09-08-2017.
 */

public class SqlCustomer implements Serializable {

    private final static String table = SQLiteHelper.TABLE_CUST;
    private static String[] columns = {SQLiteHelper.COL_CUSTNAME, SQLiteHelper.COL_CUSTMOBILE,
            SQLiteHelper.COL_DSC, SQLiteHelper.COL_TIME};

    //region Getter Setter
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;
    private static String orderBy = null;
    private String custName, custMobile, custRegDate, custDsc;

    public SqlCustomer(String custName, String custMobile, String custRegDate, String custDsc) {
        this.custName = custName;
        this.custMobile = custMobile;
        this.custRegDate = custRegDate;
        this.custDsc = custDsc;
    }

    public static String nameToMobile(Activity context, String custName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTNAME + "='" + custName + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        String mobileNo = "";
        if (cursor.moveToFirst()) {
            mobileNo = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));
        }
        db.close();
        cursor.close();

        return mobileNo;
    }
    //endregion

    public static String mobileToName(Activity context, String mobile) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + mobile + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        String custName = "";
        if (cursor.moveToFirst()) {
            custName = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTNAME));
        }
        db.close();
        cursor.close();

        return custName;
    }

    public static ArrayList<SqlCustomer> allCustomers(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_CUSTNAME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<SqlCustomer> sqlCustomers = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    SqlCustomer sqlCustomer = cursorToSql(cursor);
                    sqlCustomers.add(sqlCustomer);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlCustomers;
    }

    public static ArrayList<String> allCustomerString(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_CUSTNAME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select Customer");

        if (cursor.moveToFirst()) {
            do {
                String catname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTNAME));
                list.add(catname);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list;
    }

    public static ArrayList<String> allCustDataString(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

/*        String[] column_s = {SQLiteHelper.COL_CUSTMOBILE};
        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_CUSTMOBILE;
        having = null;
        orderBy = SQLiteHelper.COL_CUSTMOBILE;*/

        String qry = "SELECT " + SQLiteHelper.COL_CUSTNAME
                + " FROM " + SQLiteHelper.TABLE_CUST
                + " WHERE " + SQLiteHelper.COL_CUSTMOBILE
                + " IN ( SELECT " + SQLiteHelper.COL_CUSTMOBILE
                + " FROM " + SQLiteHelper.TABLE_TRANS
                + ") ORDER BY " + SQLiteHelper.COL_CUSTNAME + ";";

        Cursor cursor = db.rawQuery(qry, null);
        ArrayList<String> list = new ArrayList<>();
        list.add("Select Customer");

        if (cursor.moveToFirst()) {
            do {
                String temp = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTNAME));
                list.add(temp);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return list;
    }

    private static SqlCustomer cursorToSql(Cursor cursor) {
        String name = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTNAME));
        String mobile = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));
        String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
        String dsc = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));

        return new SqlCustomer(name, mobile, time, dsc);
    }

    public String getCustDsc() {
        return custDsc;
    }

    public void setCustDsc(String custDsc) {
        this.custDsc = custDsc;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustRegDate() {
        return custRegDate;
    }

    public void setCustRegDate(String custRegDate) {
        this.custRegDate = custRegDate;
    }

    public String getCustMobile() {
        return custMobile;
    }

    public void setCustMobile(String custMobile) {
        this.custMobile = custMobile;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_CUSTNAME, custName);
        contentValues.put(SQLiteHelper.COL_CUSTMOBILE, custMobile);
        contentValues.put(SQLiteHelper.COL_TIME, custRegDate);
        contentValues.put(SQLiteHelper.COL_DSC, custDsc);
        return contentValues;
    }

    public long insert(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(table, null, getValues());
        db.close();
        return value;
    }

    public long update(Activity context, String cMobile) {

        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + cMobile + "'";
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(context, "This Mobile Already Register");
        }

        db.close();
        return value;
    }

    public long delete(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CUSTMOBILE + "='" + custMobile + "'";
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