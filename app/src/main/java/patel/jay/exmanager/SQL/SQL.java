package patel.jay.exmanager.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static patel.jay.exmanager.Utility.MyConst.error;

/**
 * Created by Jay on 26-06-2017.
 */

public class SQL extends SQLiteOpenHelper {

    public static final String DBNAME = "ControlMoney";

    //region Columns
    public static final String COL_EXPENSE = "Expense";

    public static final String COL_CID = "cid", COL_CATNAME = "cat_name", CAT_AC = "Accounts", ALL = "All";

    public static final String COL_ACID = "acid", COL_ACCNAME = "accName";

    public static final String COL_AID = "aid", COL_EAMOUNT = "ex_rs",
            COL_DSC = "Description", COL_TIME = "Time", COL_DATE = "Date", COL_MONTH = "Month",
            COL_YEAR = "Year", COL_WEEK = "WeekNumber";
    //endregion

    public static final String TBL_INOUTCOME = "inoutcome";
    public static final String TBL_ACC = "account_list";
    public static final String TBL_CAT = "category";

    private Context context;

    public SQL(Context context) {
        super(context, DBNAME, null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            //expense Manager
            String CREATE_CAT = "create table " + TBL_CAT + " ("
                    + COL_CID + " integer primary key autoincrement, "
                    + COL_CATNAME + " text UNIQUE);";

            db.execSQL(CREATE_CAT);

            String CREATE_ACC = "create table " + TBL_ACC + " ("
                    + COL_ACID + " integer primary key, "
                    + COL_ACCNAME + " text UNIQUE);";
            db.execSQL(CREATE_ACC);

            String CREATE_INOUT = "create table " + TBL_INOUTCOME + " ("
                    + COL_AID + " integer primary key autoincrement, "
                    + COL_EAMOUNT + " integer NOT NULL, "
                    + COL_DSC + " text, "
                    + COL_ACID + " integer NOT NULL, "
                    + COL_TIME + " text NOT NULL, "
                    + COL_DATE + " integer NOT NULL, "
                    + COL_MONTH + " integer NOT NULL, "
                    + COL_YEAR + " integer NOT NULL, "
                    + COL_WEEK + " integer NOT NULL, "
                    + COL_CID + " integer NOT NULL, "
                    + " FOREIGN KEY (" + COL_ACID + ") REFERENCES " + TBL_ACC + "(" + COL_ACID + "),"
                    + " FOREIGN KEY (" + COL_CID + ") REFERENCES " + TBL_CAT + "(" + COL_CID + "));";
            db.execSQL(CREATE_INOUT);

            //region Category & Account
            ContentValues eCategory = new ContentValues();
            eCategory.put(COL_CATNAME, "Extra");

            ContentValues eAcc = new ContentValues();
            eAcc.put(COL_ACCNAME, "Home");

            db.insert(TBL_CAT, null, eCategory);
            db.insert(TBL_ACC, null, eAcc);

            //endregion

        } catch (Exception e) {
            error(context, e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}