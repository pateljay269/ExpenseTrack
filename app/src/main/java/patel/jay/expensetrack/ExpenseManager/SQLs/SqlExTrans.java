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
 * Created by Jay on 27-07-2017.
 */

public class SqlExTrans implements Serializable {

    //region Details
    private static final String table = SQLiteHelper.TABLE_INOUTCOME;
    private static final String[] columns = {SQLiteHelper.COL_AID, SQLiteHelper.COL_EAMOUNT, SQLiteHelper.COL_IAMOUNT,
            SQLiteHelper.COL_TIME, SQLiteHelper.COL_DATE, SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR,
            SQLiteHelper.COL_YYMMDD, SQLiteHelper.COL_WEEK, SQLiteHelper.COL_CATYPE,
            SQLiteHelper.COL_DSC, SQLiteHelper.COL_CID, SQLiteHelper.COL_ACID};
    private static String[] column_s = {SQLiteHelper.COL_MONTH, SQLiteHelper.COL_YEAR};
    private static String selection = null;
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    //endregion

    //region Getter Setter
    private static String having = null;
    private static String orderBy = null;
    private int aid;    //primary key
    private int cid, acid;
    private String dsc, time, date, month, year, ex_rs, in_rs, yymmdd, weekNumber, caType;

    //region Constructors
    public SqlExTrans(String ex_rs, String in_rs, String time, String date, String month, String year,
                      String yymmdd, String weekNumber, String dsc, int acid, int cid, String caType) {
        this.dsc = dsc;
        this.acid = acid;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.ex_rs = ex_rs;
        this.in_rs = in_rs;
        this.yymmdd = yymmdd;
        this.weekNumber = weekNumber;
        this.cid = cid;
        this.caType = caType;
    }

    public SqlExTrans(int aid, String ex_rs, String in_rs, String time, String date, String month, String year,
                      String yymmdd, String weekNumber, String dsc, int acid, int cid, String caType) {
        this(ex_rs, in_rs, time, date, month, year, yymmdd, weekNumber, dsc, acid, cid, caType);
        this.aid = aid;
    }

    //Check This Constructor Otherwise Remove From In It.
    public SqlExTrans(int aid, String ex_rs, String in_rs, String time, String date, String month, String year,
                      String yymmdd, String weekNumber, String caType) {
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.ex_rs = ex_rs;
        this.in_rs = in_rs;
        this.yymmdd = yymmdd;
        this.weekNumber = weekNumber;
        this.caType = caType;
        this.aid = aid;
    }

    //region ArrayList<SqlExTrans>
    public static ArrayList<SqlExTrans> allDetails(Activity activity, String caType, boolean order, boolean type) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATYPE + "='" + caType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;

        String temp = type ? SQLiteHelper.COL_EAMOUNT : SQLiteHelper.COL_IAMOUNT;
        ;

        if (!order)
            orderBy = SQLiteHelper.COL_TIME;
        else
            orderBy = "CAST(" + temp + " AS INTEGER)" + " DESC";

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> allDetails(Activity activity, String caType, int year, boolean order, boolean type) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATYPE + "='" + caType + "' And " + SQLiteHelper.COL_YEAR + "='" + year + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;

        String temp = type ? SQLiteHelper.COL_EAMOUNT : SQLiteHelper.COL_IAMOUNT;

        if (!order)
            orderBy = SQLiteHelper.COL_TIME;
        else
            orderBy = "CAST(" + temp + " AS INTEGER)" + " DESC";

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> allDetails(Activity activity, String caType, int year, String month, boolean order,
                                                   boolean type) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATYPE + "='" + caType + "' And "
                + SQLiteHelper.COL_YEAR + "='" + year + "' And "
                + SQLiteHelper.COL_MONTH + "='" + month + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;

        String temp = type ? SQLiteHelper.COL_EAMOUNT : SQLiteHelper.COL_IAMOUNT;
        ;

        if (!order)
            orderBy = SQLiteHelper.COL_TIME;
        else
            orderBy = "CAST(" + temp + " AS INTEGER)" + " DESC";

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> findWise(Activity activity, String cType, long startTime, long endTime) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YYMMDD + " BETWEEN '" + startTime + "' AND '" + endTime
                + "' And " + SQLiteHelper.COL_CATYPE + "='" + cType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> allAcc(Activity activity, String accName, String catType, String order) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        int acid = SqlExAccount.accInt(activity, accName);
        selection = SQLiteHelper.COL_ACID + "='" + acid + "' And " + SQLiteHelper.COL_CATYPE + "='" + catType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = "CAST(" + SQLiteHelper.COL_TIME + " AS INTEGER)" + " " + order;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    //endregion

    public static ArrayList<SqlExTrans> allAcc(Activity activity, String catType, String order) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATYPE + "='" + catType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = "CAST(" + SQLiteHelper.COL_TIME + " AS INTEGER)" + " " + order;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> allCat(Activity activity, String catName, String catType, String order) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        int cid = SqlExCategory.categoryInt(activity, catName);
        selection = SQLiteHelper.COL_CID + "='" + cid + "' And " + SQLiteHelper.COL_CATYPE + "='" + catType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = "CAST(" + SQLiteHelper.COL_TIME + " AS INTEGER)" + " " + order;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> calenderData(Activity activity, String data, String flag) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String type = "";
        switch (flag) {
            case MyConstants.day:
                type = SQLiteHelper.COL_DATE;
                break;

            case MyConstants.week:
                type = SQLiteHelper.COL_WEEK;
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

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> allAccCat(Activity activity, String accName, String catName, String order) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        int cid = SqlExCategory.categoryInt(activity, catName);
        int acid = SqlExAccount.accInt(activity, accName);
        selection = SQLiteHelper.COL_CID + "='" + cid + "' And " + SQLiteHelper.COL_ACID + "='" + acid + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = "CAST(" + SQLiteHelper.COL_TIME + " AS INTEGER)" + " " + order;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);

        return sqlTranses(db, cursor);
    }

    public static ArrayList<SqlExTrans> sqlTranses(SQLiteDatabase db, Cursor cursor) {
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

    public static ArrayList<String> dayMonthArray(Activity activity, String monthName) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_MONTH + "='" + monthName + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> dayArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
                    dayArray.add(date);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayArray;
    }

    public static ArrayList<String> dayYearArray(Activity activity, int acId, int year) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] column = {SQLiteHelper.COL_DATE};
        selection = SQLiteHelper.COL_ACID + "='" + acId + "' And " + SQLiteHelper.COL_YEAR + " ='" + year + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_DATE;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, column, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> dayArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
                    dayArray.add(date);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dayArray;
    }

    public static ArrayList<String> monthYearArray(Activity activity, int year) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_YEAR + " ='" + year + "'";
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

    //region Insert Update Delete

    public static ArrayList<String> monthYear(Activity activity, String year, String caType, String accName) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        if (accName.equals(SQLiteHelper.ALL)) {
            selection = SQLiteHelper.COL_CATYPE + "='" + caType + "' AND " + SQLiteHelper.COL_MONTH + " LIKE '%" + year + "'";
        } else {
            int acid = SqlExAccount.accInt(activity, accName);
            selection = SQLiteHelper.COL_CATYPE + "='" + caType + "' AND " + SQLiteHelper.COL_MONTH + " LIKE '%" + year + "' AND "
                    + SQLiteHelper.COL_ACID + "='" + acid + "'";
        }

        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        Cursor cursor = db.query(table, column_s, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> monthArray = new ArrayList<>();
        monthArray.add(SQLiteHelper.COL_MONTH);
        try {
            if (cursor.moveToFirst()) {
                do {
                    String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
                    month = month.substring(0, month.length() - 5);
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

    public static ArrayList<String> yearArray(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = null;
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_YEAR;
        having = null;
        orderBy = SQLiteHelper.COL_YEAR;

        Cursor cursor = db.query(table, column_s, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> year = new ArrayList<>();
        year.add(SQLiteHelper.COL_YEAR);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String yr = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
                    if (yr != null)
                        year.add(yr);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return year;
    }

    //region Others
    public static int countCid(Activity activity, int cid) {
        int count = 0;

        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CID + "='" + cid + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }

    public static int countAcId(Activity activity, int acId) {
        int count = 0;

        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acId + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }
    //endregion

    public static SqlExTrans monthTotal(Activity activity, String month) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_MONTH + "='" + month + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_MONTH;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        SqlExTrans sqlExTrans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(table, SqlExCalender.columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                sqlExTrans = SqlExCalender.cursorTo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlExTrans;
    }

    public static SqlExTrans monthTotal(Activity activity, int acid) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_ACID;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        SqlExTrans sqlExTrans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(table, SqlExCalender.columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                sqlExTrans = SqlExCalender.cursorTo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlExTrans;
    }

    public static SqlExTrans monthYearTotal(Activity activity, int acid, int year) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "' And " + SQLiteHelper.COL_YEAR + " ='" + year + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_ACID;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        SqlExTrans sqlExTrans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(table, SqlExCalender.columns, selection, selectionArgs, groupBy, having, orderBy);

            if (cursor.moveToFirst()) {
                sqlExTrans = SqlExCalender.cursorTo(cursor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlExTrans;
    }

    public static Cursor allMonthCursor(Activity activity, String date) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_DATE + "='" + date + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public static Cursor allAccCursor(Activity activity, int acid, String date) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_ACID + "='" + acid + "' AND " + SQLiteHelper.COL_DATE + "='" + date + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_TIME;

        return db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
    }

    public static SqlExTrans cursorTo(Cursor cursor) {
        int aid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_AID));
        String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EAMOUNT));
        String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_IAMOUNT));
        String dsc = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));
        int acid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_ACID));
        String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
        String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
        String month = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_MONTH));
        String year = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YEAR));
        String yymmdd = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_YYMMDD));
        String weekN = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_WEEK));
        int cid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_CID));
        String caType = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATYPE));

        return new SqlExTrans(aid, ex_rs, in_rs, time, date, month, year, yymmdd, weekN, dsc, acid, cid, caType);
    }

    public int getAcid() {
        return acid;
    }

    public int getAid() {
        return aid;
    }

    public String getDescription() {
        return dsc;
    }

    public String getTime() {
        return time;
    }

    //endregion

    //region ArrayList<String>

    public String getDate() {
        return date;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public String getEx_rs() {
        return ex_rs;
    }

    public String getIn_rs() {
        return in_rs;
    }

    //endregion

    public String getYymmdd() {
        return yymmdd;
    }

    public String getWeekNumber() {
        return weekNumber;
    }

    public int getCid() {
        return cid;
    }

    public String getCaType() {
        return caType;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_EAMOUNT, ex_rs);
        contentValues.put(SQLiteHelper.COL_IAMOUNT, in_rs);
        contentValues.put(SQLiteHelper.COL_DSC, dsc);
        contentValues.put(SQLiteHelper.COL_ACID, acid);
        contentValues.put(SQLiteHelper.COL_TIME, time);
        contentValues.put(SQLiteHelper.COL_DATE, date);
        contentValues.put(SQLiteHelper.COL_MONTH, month);
        contentValues.put(SQLiteHelper.COL_YEAR, year);
        contentValues.put(SQLiteHelper.COL_YYMMDD, yymmdd);
        contentValues.put(SQLiteHelper.COL_WEEK, weekNumber);
        contentValues.put(SQLiteHelper.COL_CID, cid);
        contentValues.put(SQLiteHelper.COL_CATYPE, caType);
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

        selection = SQLiteHelper.COL_AID + "=" + aid;
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(activity, e.toString());
        }

        db.close();
        return value;
    }

    //endregion

    public long delete(Activity activity) {
        SQLiteHelper helper = new SQLiteHelper(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_AID + "=" + aid;
        selectionArgs = null;
        long value = 0;

        try {
            value = db.delete(table, selection, selectionArgs);
        } catch (Exception e) {
            MyConstants.toast(activity, e.toString());
        }
        db.close();
        return value;
    }

}