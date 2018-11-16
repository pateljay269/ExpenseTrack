package patel.jay.exmanager.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static patel.jay.exmanager.SQL.SQL.CAT_AC;
import static patel.jay.exmanager.SQL.SQL.COL_ACCNAME;
import static patel.jay.exmanager.SQL.SQL.COL_ACID;
import static patel.jay.exmanager.SQL.SQL.TBL_ACC;
import static patel.jay.exmanager.Utility.MyConst.toast;

/**
 * Created by Jay on 8/26/2017.
 */

public class Account {

    private String accName;
    private int acid;

    public Account(String accName) {
        this.accName = accName;
    }

    public Account(int acid, String accName) {
        this(accName);
        this.acid = acid;
    }

    public Account() {
    }

    public ArrayList<Account> allAcc(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {COL_ACID, COL_ACCNAME};

        Cursor cursor = db.query(TBL_ACC, columns, null, null, null, null, COL_ACCNAME);
        ArrayList<Account> accounts = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                accounts.add(cursorTo(cursor));
            } while (cursor.moveToNext());
            cursor.close();
        }

        db.close();
        return accounts;
    }

    public String accString(Context context, int acid) {
        for (Account acc : new Account().allAcc(context)) {
            if (acc.getAcid() == acid) {
                return acc.getAccName();
            }
        }

        return "";
    }

    public int accInt(Context context, String accName) {
        for (Account acc : new Account().allAcc(context)) {
            if (acc.getAccName().equals(accName)) {
                return acc.getAcid();
            }
        }

        return 0;
    }

    public ArrayList<String> allAccString(Context context) {

        ArrayList<String> sqlList = new ArrayList<>();
        sqlList.add(CAT_AC);

        for (Account acc : new Account().allAcc(context)) {
            sqlList.add(acc.getAccName());
        }

        return sqlList;
    }

    private Account cursorTo(Cursor cursor) {
        try {
            int acid = cursor.getInt(cursor.getColumnIndex(COL_ACID));
            String accName = cursor.getString(cursor.getColumnIndex(COL_ACCNAME));

            return new Account(acid, accName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getAccName() {
        return accName;
    }

    public int getAcid() {
        return acid;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ACCNAME, accName);
        return contentValues;
    }

    public long insert(Context activity) {
        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(TBL_ACC, null, getValues());
        db.close();
        return value;
    }

    public long update(Context activity) {

        SQL helper = new SQL(activity);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_ACID + "='" + acid + "'";

        long value = 0;
        try {
            value = db.update(TBL_ACC, getValues(), selection, null);
        } catch (Exception e) {
            e.printStackTrace();
            toast(activity, "This Name Already Used");
        }

        db.close();
        return value;
    }

    public long delete(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_ACID + "='" + acid + "'";

        long value = 0;

        try {
            value = db.delete(TBL_ACC, selection, null);
        } catch (Exception e) {
            e.printStackTrace();
            toast(context, e.toString());
        }
        db.close();
        return value;
    }

}
