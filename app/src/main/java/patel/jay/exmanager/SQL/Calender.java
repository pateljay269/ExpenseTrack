package patel.jay.exmanager.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static patel.jay.exmanager.SQL.SQL.ALL;
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


/**
 * Created by Jay on 30-07-2017.
 */

public class Calender {

    public String[] columns = new String[]{COL_AID,
            "SUM(" + COL_EAMOUNT + ") AS " + COL_EAMOUNT,
            COL_TIME, COL_DATE, COL_MONTH, COL_YEAR, COL_WEEK,
            COL_DSC, COL_CID, COL_ACID};

    public ArrayList<Trans> dayWise(String accName) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = null;
        if (!accName.equalsIgnoreCase(ALL)) {
            int acid = new Account().accInt(context, accName);
            selection = COL_ACID + "='" + acid + "'";
        }

        String groupBy = COL_DATE + " , " + COL_MONTH + " , " + COL_YEAR;

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, groupBy, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> weekWise(String accName) {

        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = null;
        if (!accName.equals(ALL)) {
            int acid = new Account().accInt(context, accName);
            selection = COL_ACID + "='" + acid + "'";
        }

        String groupBy = COL_WEEK + " , " + COL_MONTH + " , " + COL_YEAR;

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, groupBy, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> monthWise(String accName) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = null;
        if (!accName.equals(ALL)) {
            int acid = new Account().accInt(context, accName);
            selection = COL_ACID + "='" + acid + "'";
        }

        String groupBy = COL_MONTH + " , " + COL_YEAR;

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, groupBy, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> yearWise(String accName) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = null;
        if (!accName.equals(ALL)) {
            int acid = new Account().accInt(context, accName);
            selection = COL_ACID + "='" + acid + "'";
        }

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, COL_YEAR, null, COL_TIME);

        return sqlTranses(db, cursor);
    }

    public ArrayList<Trans> totalWise(String accName, String col) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String selection = null;
        if (!accName.equalsIgnoreCase(ALL)) {
            int acid = new Account().accInt(context, accName);
            selection = COL_ACID + "='" + acid + "'";
        }

        Cursor cursor = db.query(TBL_INOUTCOME, columns, selection, null, col, null, col);

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

    public Trans cursorTo(Cursor cursor) {
        try {
            int aid = cursor.getInt(cursor.getColumnIndex(COL_AID));
            int ex_rs = cursor.getInt(cursor.getColumnIndex(COL_EAMOUNT));
            String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
            int date = cursor.getInt(cursor.getColumnIndex(COL_DATE));
            int month = cursor.getInt(cursor.getColumnIndex(COL_MONTH));
            int year = cursor.getInt(cursor.getColumnIndex(COL_YEAR));
            int weekN = cursor.getInt(cursor.getColumnIndex(COL_WEEK));

            return new Trans(aid, ex_rs, time, date, month, year, weekN);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private Context context;

    public Calender(Context context) {
        this.context = context;
    }

    public Calender() {
    }
}