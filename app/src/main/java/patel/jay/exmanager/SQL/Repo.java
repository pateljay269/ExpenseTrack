package patel.jay.exmanager.SQL;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import patel.jay.exmanager.Utility.TimeConvert;

import static java.lang.System.currentTimeMillis;
import static patel.jay.exmanager.SQL.SQL.COL_EAMOUNT;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.SQL.SQL.COL_YEAR;
import static patel.jay.exmanager.SQL.SQL.TBL_INOUTCOME;

public class Repo {

    public ArrayList<Repo> getReport() {

        TimeConvert tc = TimeConvert.timeMilies(currentTimeMillis());
        String mon = "";

        ArrayList<Repo> ar = new ArrayList<>();

        String sql = "select " + COL_YEAR + " from " + TBL_INOUTCOME
                + " where " + COL_YEAR + "<" + tc.getYy() + " group by " + COL_YEAR;
        int yr = getAggregate(sql, COL_YEAR);

        sql = "select sum(" + COL_EAMOUNT + ") as " + COL_EAMOUNT + " from " + TBL_INOUTCOME
                + " where " + COL_YEAR + "<" + tc.getYy();
        int yrTot = getResult(sql, COL_EAMOUNT);
        int total = yrTot / yr;
        ar.add(new Repo("Last Year", total));

        int tempmon = tc.getMm();

        sql = "select sum(" + COL_EAMOUNT + ") as " + COL_EAMOUNT + " from " + TBL_INOUTCOME
                + " where " + COL_MONTH + "='" + tempmon + "' and " + COL_YEAR + "=" + tc.getYy();
        yrTot = getResult(sql, COL_EAMOUNT);

        ar.add(new Repo("Current Month", yrTot));

        sql = "select sum(" + COL_EAMOUNT + ") as " + COL_EAMOUNT + " from " + TBL_INOUTCOME
                + " where " + COL_MONTH + "='" + tempmon + "' and " + COL_YEAR + "<" + tc.getYy();
        yrTot = getResult(sql, COL_EAMOUNT);
        total = yrTot / yr;

        mon = tc.monthName(tempmon);
        ar.add(new Repo(mon, total));

        tempmon++;
        if (tempmon == 12) {
            tempmon = 0;
        }

        sql = "select sum(" + COL_EAMOUNT + ") as " + COL_EAMOUNT + " from " + TBL_INOUTCOME
                + " where " + COL_MONTH + "='" + tempmon + "' and " + COL_YEAR + "<" + tc.getYy();
        yrTot = getResult(sql, COL_EAMOUNT);
        total = yrTot / yr;

        mon = tc.monthName(tempmon);
        ar.add(new Repo(mon, total));

        tempmon++;
        if (tempmon == 12) {
            tempmon = 0;
        }

        sql = "select sum(" + COL_EAMOUNT + ") as " + COL_EAMOUNT + " from " + TBL_INOUTCOME
                + " where " + COL_MONTH + "='" + tempmon + "' and " + COL_YEAR + "<" + tc.getYy();
        yrTot = getResult(sql, COL_EAMOUNT);
        total = yrTot / yr;

        mon = tc.monthName(tempmon);
        ar.add(new Repo(mon, total));

        return ar;
    }

    private int getAggregate(String sql, String col) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
//        ArrayList<String> ar = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
//                ar.add(cursor.getString(cursor.getColumnIndex(col)));
                count++;
            } while (cursor.moveToNext());
        } else {
            count = 1;
        }
        cursor.close();

        return count;
    }

    private int getResult(String sql, String col) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(cursor.getColumnIndex(col));
            } while (cursor.moveToNext());
        } else {
            count = 1;
        }
        cursor.close();

        return count;
    }

    private String title;
    private int total;
    private Context context;

    public Repo(Context context) {
        this.context = context;
    }

    public Repo(String title, int total) {
        this.title = title;
        this.total = total;
    }

    public String getTitle() {
        return title;
    }

    public int getTotal() {
        return total;
    }
}
