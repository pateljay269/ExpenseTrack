package patel.jay.exmanager.SQL;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.Utility.MyConst;
import patel.jay.exmanager.Utility.TimeConvert;

import static java.lang.System.currentTimeMillis;
import static patel.jay.exmanager.SQL.SQL.COL_ACID;
import static patel.jay.exmanager.SQL.SQL.COL_AID;
import static patel.jay.exmanager.SQL.SQL.COL_CID;
import static patel.jay.exmanager.SQL.SQL.COL_DATE;
import static patel.jay.exmanager.SQL.SQL.COL_DSC;
import static patel.jay.exmanager.SQL.SQL.COL_EAMOUNT;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.SQL.SQL.COL_TIME;
import static patel.jay.exmanager.SQL.SQL.COL_WEEK;
import static patel.jay.exmanager.SQL.SQL.COL_YEAR;
import static patel.jay.exmanager.SQL.SQL.TBL_INOUTCOME;
import static patel.jay.exmanager.Utility.MyConst.day;
import static patel.jay.exmanager.Utility.MyConst.toast;
import static patel.jay.exmanager.Utility.MyConst.week;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

/**
 * Created by Jay on 27-07-2017.
 */

public class Trans {

    private Activity activity;

    //region ArrayList<Trans>
    public ArrayList<Trans> allCatTrans() {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        TimeConvert tc = timeMilies(currentTimeMillis());
        String selection = COL_YEAR + ">=" + tc.getYy() + " And " + COL_MONTH + ">=" + tc.getMm();

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> lastTrans() {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String s = "";
        Cursor cursor = db.query(TBL_INOUTCOME, columns, s, null, s, s, COL_TIME + " DESC", "10");

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> findWise(long startTime, long endTime) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_TIME + " BETWEEN '" + startTime + "' AND '" + endTime + "'";

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> calenderData(String data[], String flag) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = "";

        switch (flag) {
            case day:
                selection = COL_DATE + "=" + data[0] + " And " + COL_MONTH + "=" + data[1] + " And " + COL_YEAR + "=" + data[2] + "";
                break;

            case week:
                selection = COL_MONTH + "=" + data[1] + " And " + COL_YEAR + "=" + data[2] + " And " + COL_WEEK + "=" + data[3] + "";
                break;

            case MyConst.month:
                selection = COL_MONTH + "=" + data[1] + " And " + COL_YEAR + "=" + data[2] + "";
                break;

            case MyConst.year:
                selection = COL_YEAR + "=" + data[2] + "";
                break;
        }

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> calenderRepo(String data, String flag) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = flag + "=" + data;

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> getList(String selection, String col, String ord) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String temp = "";
        if (col.equalsIgnoreCase("Date")) {
            temp = COL_TIME;
        } else if (col.equalsIgnoreCase("Amount")) {
            temp = COL_EAMOUNT;
        }

        String orderBy = temp + " " + ord;

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, orderBy);

        return sqlTranses(db, cursor);
    }

    private ArrayList<Trans> sqlTranses(SQLiteDatabase db, Cursor cursor) {
        ArrayList<Trans> sqlAccounts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                sqlAccounts.add(cursorTo(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return sqlAccounts;
    }
    //endregion

    //region Other ArrayList

    public ArrayList<String> yearArray() {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] column_s = {COL_MONTH, COL_YEAR};

        Cursor cursor = db.query(TBL_INOUTCOME, column_s, null, null, COL_YEAR, null, COL_YEAR);
        ArrayList<String> year = new ArrayList<>();
        year.add(COL_YEAR);

        try {
            if (cursor.moveToFirst()) {
                do {
                    String yr = cursor.getString(cursor.getColumnIndex(COL_YEAR));
                    if (yr != null)
                        year.add(yr);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return year;
    }

    public ArrayList<String> getDsc() {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] column_s = {COL_DSC};

        Cursor cursor = db.query(TBL_INOUTCOME, column_s, null, null, COL_DSC, null, COL_DSC);

        HashSet<String> hashSet = new HashSet<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String dsc = cursor.getString(cursor.getColumnIndex(COL_DSC));
                    if (dsc != null && !dsc.isEmpty())
                        hashSet.add(dsc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();

        return new ArrayList<>(hashSet);
    }

    public ArrayList<String> dayMonthArray(int month, int year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = "";

        if (year == 0) {
            if (month != -1) {
                selection = COL_MONTH + "=" + month;
            }
        } else {
            if (month == -1) {
                selection = COL_YEAR + "=" + year;
            } else {
                selection = COL_YEAR + "=" + year + " And " + COL_MONTH + "=" + month;
            }
        }

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, COL_DATE, null, COL_DATE);
        ArrayList<String> dayArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String date = cursor.getString(cursor.getColumnIndex(COL_DATE));
                    dayArray.add(date);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return dayArray;
    }

    public ArrayList<Integer> monthYearArray(int year, boolean isIn) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String orderBy = COL_TIME + " DESC",
                selection = COL_YEAR + " =" + year + " AND " + COL_EAMOUNT + ">0";

        if (isIn) {
            selection = COL_YEAR + " ='" + year + "'";
        }

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, COL_MONTH, null, orderBy);
        ArrayList<Integer> monthArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    int month = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
                    monthArray.add(month);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return monthArray;
    }

    public ArrayList<String> monthYearArray(String year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_YEAR + " =" + year;
        if (year.equalsIgnoreCase(COL_YEAR)) {
            selection = "";
        }
        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, COL_MONTH, null, COL_TIME);
        ArrayList<String> monthArray = new ArrayList<>();
        monthArray.add("");
        try {
            if (cursor.moveToFirst()) {
                TimeConvert tc = new TimeConvert();
                do {
                    int mm = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
                    String month = tc.monthName(mm);
                    monthArray.add(month);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return monthArray;
    }

    public ArrayList<TimeConvert> dayYearArray(int acId, int year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] column = {COL_TIME};
        String selection = COL_ACID + "=" + acId + " And " + COL_YEAR + " =" + year + "";

        Cursor cursor = db.query(TBL_INOUTCOME, column, selection, null, COL_DATE, null, COL_TIME);
        ArrayList<TimeConvert> dayArray = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
                    TimeConvert tc = timeMilies(Long.parseLong(time));

                    dayArray.add(tc);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        cursor.close();
        db.close();
        return dayArray;
    }

    //endregion

    //region Others
    public AutoCompleteTextView getDscAdapter(Context context, TransAdapter adapter, AutoCompleteTextView actFind) {
        actFind.setThreshold(1);
        actFind.setTextColor(Color.BLACK);
        actFind.setVisibility(View.VISIBLE);

        HashSet<String> hashSet = new HashSet<>();

        for (Trans trans : adapter.getTransList()) {
            hashSet.add(trans.getDescription());
        }

        ArrayList<String> arrayList = new ArrayList<>(hashSet);

        Collections.sort(arrayList);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context, R.layout.spn_row, R.id.tvRow, arrayList);
        actFind.setAdapter(adapter1);
        return actFind;
    }

    public int countCid(int cid) {
        int count = 0;

        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_CID + "='" + cid + "'";

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }

    public int countAcId(int acId) {
        int count = 0;

        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_ACID + "='" + acId + "'";

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                count++;
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return count;
    }

    public String findMaxCid(int acId) {
        String cname = "";
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String sql = "SELECT " + COL_CID + ", COUNT(*) As total FROM " + TBL_INOUTCOME + " " +
                " WHERE " + COL_ACID + "=" + acId + " GROUP BY " + COL_CID + " " +
                " ORDER BY total DESC" +
                " LIMIT 1";

        Cursor cursor = db.rawQuery(sql, null);

        if (cursor.moveToFirst()) {
            do {
                try {
                    int cid = cursor.getInt(cursor.getColumnIndex(COL_CID));
                    cname = new Category().catStr(activity, cid);
                } catch (Exception e) {
                    e.printStackTrace();
                    cname = "";
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return cname;
    }

    public Trans monthTotal(int month, int year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_MONTH + "=" + month + " And " + COL_YEAR + "=" + year + "";

        Trans trans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TBL_INOUTCOME, new Calender().columns, selection, null, COL_MONTH, null, COL_TIME);

            if (cursor.moveToFirst()) {
                trans = new Calender().cursorTo(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();

        if (trans == null) {
            trans = new Trans();
        }
        trans.setActivity(activity);

        return trans;
    }

    public Trans yearTotal(int year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_YEAR + "=" + year + "";

        Trans trans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TBL_INOUTCOME, new Calender().columns, selection, null, COL_YEAR, null, COL_TIME);

            if (cursor.moveToFirst()) {
                trans = new Calender().cursorTo(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return trans;
    }

    public Trans monthYearTotal(int acid, int year) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = COL_ACID + "='" + acid + "' And " + COL_YEAR + " ='" + year + "'";

        Trans trans = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TBL_INOUTCOME, new Calender().columns, selection, null, COL_ACID, null, COL_TIME);

            if (cursor.moveToFirst()) {
                trans = new Calender().cursorTo(cursor);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        db.close();
        return trans;
    }

    private Trans cursorTo(Cursor cursor) {
        try {
            int aid = cursor.getInt(cursor.getColumnIndex(COL_AID));
            int ex_rs = cursor.getInt(cursor.getColumnIndex(COL_EAMOUNT));
            String dsc = cursor.getString(cursor.getColumnIndex(COL_DSC));
            int acid = cursor.getInt(cursor.getColumnIndex(COL_ACID));
            String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
            int date = cursor.getInt(cursor.getColumnIndex(COL_DATE));
            int month = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
            int year = cursor.getInt(cursor.getColumnIndex(COL_YEAR));
            int weekN = cursor.getInt(cursor.getColumnIndex(COL_WEEK));
            int cid = cursor.getInt(cursor.getColumnIndex(COL_CID));

            return new Trans(aid, ex_rs, time, date, month, year, weekN, dsc, acid, cid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //endregion

    //region Constructors & Getters
    public Trans() {
    }

    public Trans(Activity activity) {
        this.activity = activity;
    }

    public Trans(int ex_rs, String time, int date, int month, int year,
                 int weekNumber, String dsc, int acid, int cid) {
        this.dsc = dsc;
        this.acid = acid;
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.ex_rs = ex_rs;
        this.weekNumber = weekNumber;
        this.cid = cid;
    }

    public Trans(int aid, int ex_rs, String time, int date, int month, int year,
                 int weekNumber, String dsc, int acid, int cid) {
        this(ex_rs, time, date, month, year, weekNumber, dsc, acid, cid);
        this.aid = aid;
    }

    public Trans(int aid, int ex_rs, String time, int date, int month, int year,
                 int weekNumber) {
        this.time = time;
        this.date = date;
        this.month = month;
        this.year = year;
        this.ex_rs = ex_rs;
        this.weekNumber = weekNumber;
        this.aid = aid;
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

    public int getDate() {
        return date;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getEx_rs() {
        return ex_rs;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getCid() {
        return cid;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_EAMOUNT, ex_rs);
        contentValues.put(COL_DSC, dsc);
        contentValues.put(COL_ACID, acid);
        contentValues.put(COL_TIME, time);
        contentValues.put(COL_DATE, date);
        contentValues.put(COL_MONTH, month);
        contentValues.put(COL_YEAR, year);
        contentValues.put(COL_WEEK, weekNumber);
        contentValues.put(COL_CID, cid);
        return contentValues;
    }

    //endregion

    //region Insert Update Delete
    public long insert(Context activity) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(TBL_INOUTCOME, null, getValues());
        db.close();

        deleteNull(activity);
        return value;
    }

    private int deleteNull(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_DATE + "=0 Or " + COL_MONTH + "=0 Or " + COL_YEAR + "=0 Or " + COL_WEEK + "=0 Or " + COL_TIME + "=0";

        int value = 0;
        try {
            value = db.delete(TBL_INOUTCOME, selection, null);
        } catch (Exception e) {
            toast(context, e.toString());
        }

        db.close();
        return value;
    }

    public long update(Context activity) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_AID + "=" + aid;

        long value = 0;
        try {
            value = db.update(TBL_INOUTCOME, getValues(), selection, null);
        } catch (Exception e) {
            toast(activity, e.toString());
        }

        db.close();

        deleteNull(activity);
        return value;
    }

    public long delete(Context activity) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_AID + "=" + aid;

        long value = 0;

        try {
            value = db.delete(TBL_INOUTCOME, selection, null);
        } catch (Exception e) {
            toast(activity, e.toString());
        }
        db.close();

        deleteNull(activity);
        return value;
    }

    //endregion

    public String[] columns = {COL_AID, COL_EAMOUNT, COL_TIME, COL_DATE,
            COL_MONTH, COL_YEAR, COL_WEEK, COL_DSC, COL_CID, COL_ACID};

    private int aid, cid, acid, ex_rs, date, month, year, weekNumber;
    private String dsc, time;

    @Override
    public String toString() {
        return date + " " + month + " " + year;
    }
}