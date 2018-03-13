package patel.jay.expensetrack.ConstClass;

import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Jay on 26-06-2017.
 */

public class SQLiteHelper extends SQLiteOpenHelper {

    public static final String DBNAME = "DB_ExpenseTrack";
    public static final int VERSION = 1;

    //region Columns
    public static final String COL_CUSTID = "cust_id";
    public static final String COL_CUSTNAME = "cust_name";
    public static final String COL_CUSTMOBILE = "cust_mob";

    public static final String COL_TID = "Tid";
    public static final String COL_EXPENSE = "Expense";
    public static final String COL_INCOME = "Income";

    public static final String COL_RID = "rid";
    public static final String COL_RECHARGE = "Recharge";
    public static final String COL_INCENTIVE = "Incentive";

    public static final String COL_UID = "uid";
    public static final String COL_PIN = "pin";
    public static final String COL_USERNAME = "user_name";
    public static final String COL_PASS = "pass_word";
    public static final String COL_ACNO = "acNo";

    public static final String COL_CID = "cid";
    public static final String COL_CATNAME = "cat_name";
    public static final String COL_CATYPE = "Category";
    public static final String CAT_EX = "Expense", CAT_IN = "Income", CAT_AC = "Accounts", ALL = "All";

    public static final String COL_ACCNAME = "accName";
    public static final String COL_ACID = "acid";

    public static final String COL_AID = "aid";
    public static final String COL_IAMOUNT = "in_rs";
    public static final String COL_EAMOUNT = "ex_rs";
    public static final String COL_DSC = "Description";
    public static final String COL_TIME = "Time";
    public static final String COL_DATE = "Date";
    public static final String COL_MONTH = "Month";
    public static final String COL_YEAR = "Year";
    public static final String COL_YYMMDD = "yymmdd";
    public static final String COL_WEEK = "WeekNumber";
    //endregion

    //region User
    public static final String TABLE_USER = "user";

    public static final String CREATE_USER =
            "create table " + TABLE_USER + " ("
                    + COL_UID + " integer primary key autoincrement,"
                    + COL_USERNAME + " text UNIQUE, "
                    + COL_PASS + " text NOT NULL, "
                    + COL_PIN + " integer UNIQUE, "
                    + COL_ACNO + " text NOT NULL);";
    //endregion

    //region CATEGORY
    public static final String TABLE_CATEGORY = "category";

    public static final String CREATE_CATEGORY =
            "create table " + TABLE_CATEGORY + " ("
                    + COL_CID + " integer primary key autoincrement, "
                    + COL_CATNAME + " text UNIQUE, "
                    + COL_CATYPE + " text NOT NULL);";
    //endregion

    //region ACCOUNT
    public static final String TABLE_ACCOUNT = "account_list";

    public static final String CREATE_ACCOUNT =
            "create table " + TABLE_ACCOUNT + " ("
                    + COL_ACID + " integer primary key, "
                    + COL_ACCNAME + " text UNIQUE);";
    //endregion

    //region INOUTCOME
    public static final String TABLE_INOUTCOME = "inoutcome";

    public static final String CREATE_INOUTCOME =
            "create table " + TABLE_INOUTCOME + " ("
                    + COL_AID + " integer primary key autoincrement, "
                    + COL_EAMOUNT + " text NOT NULL, "
                    + COL_IAMOUNT + " text NOT NULL, "
                    + COL_DSC + " text, "
                    + COL_ACID + " integer NOT NULL, "
                    + COL_TIME + " text NOT NULL, "
                    + COL_DATE + " text NOT NULL, "
                    + COL_MONTH + " text NOT NULL, "
                    + COL_YEAR + " text NOT NULL, "
                    + COL_YYMMDD + " text NOT NULL, "
                    + COL_WEEK + " text NOT NULL, "
                    + COL_CID + " integer NOT NULL, "
                    + COL_CATYPE + " text NOT NULL,"
                    + " FOREIGN KEY (" + COL_ACID + ") REFERENCES " + TABLE_ACCOUNT + "(" + COL_ACID + "),"
                    + " FOREIGN KEY (" + COL_CID + ") REFERENCES " + TABLE_CATEGORY + "(" + COL_CID + "));";
    //endregion

    //region CUSTOMER
    public static final String TABLE_CUST = "cust_info";

    public static final String CREATE_CUST =
            "create table " + TABLE_CUST + " ("
                    + COL_CUSTID + " integer primary key autoincrement, "
                    + COL_CUSTNAME + " text UNIQUE, "
                    + COL_CUSTMOBILE + " text UNIQUE NOT NULL , "
                    + COL_TIME + " text NOT NULL, "
                    + COL_DSC + " text );";
    //endregion

    //region INOUTCOME
    public static final String TABLE_TRANS = "trans";

    public static final String CREATE_TRANS =
            "create table " + TABLE_TRANS + " ("
                    + COL_TID + " integer primary key autoincrement, "
                    + COL_EXPENSE + " integer NOT NULL, "
                    + COL_INCOME + " integer NOT NULL, "
                    + COL_DSC + " text, "
                    + COL_TIME + " text NOT NULL, "
                    + COL_DATE + " text NOT NULL, "
                    + COL_MONTH + " text NOT NULL, "
                    + COL_YEAR + " integer NOT NULL, "
                    + COL_YYMMDD + " integer NOT NULL, "
                    + COL_CUSTMOBILE + " text NOT NULL, "
                    + " FOREIGN KEY (" + COL_CUSTMOBILE + ") REFERENCES " + TABLE_CUST + "(" + COL_CUSTMOBILE + "));";
    //endregion

    //region RECHARGE
    public static final String TABLE_RECHARGE = "recharge";

    public static final String CREATE_RECHARGE =
            "create table " + TABLE_RECHARGE + " ( "
                    + COL_RID + " integer primary key autoincrement,"
                    + COL_RECHARGE + " integer NOT NULL, "
                    + COL_INCENTIVE + " integer NOT NULL, "
                    + COL_TIME + " text NOT NULL, "
                    + COL_DATE + " text NOT NULL, "
                    + COL_MONTH + " text NOT NULL, "
                    + COL_YEAR + " integer NOT NULL); ";
    //endregion

    private Activity context;

    public SQLiteHelper(Activity context) {
        super(context, DBNAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CREATE_USER);
            //expense Manager
            db.execSQL(CREATE_CATEGORY);
            db.execSQL(CREATE_ACCOUNT);
            db.execSQL(CREATE_INOUTCOME);
            //recharge Manager
            db.execSQL(CREATE_CUST);
            db.execSQL(CREATE_TRANS);
            db.execSQL(CREATE_RECHARGE);

            //region User
            ContentValues eUser = new ContentValues();
            eUser.put(SQLiteHelper.COL_USERNAME, "User");
            eUser.put(SQLiteHelper.COL_PASS, "1234");
            eUser.put(SQLiteHelper.COL_PIN, "1234");
            eUser.put(SQLiteHelper.COL_ACNO, "1234");

            ContentValues aUser = new ContentValues();
            aUser.put(SQLiteHelper.COL_USERNAME, MyConstants.ADMIN);
            aUser.put(SQLiteHelper.COL_PASS, "9902");
            aUser.put(SQLiteHelper.COL_PIN, "9902");
            aUser.put(SQLiteHelper.COL_ACNO, "9902");

            db.insert(TABLE_USER, null, eUser);
            db.insert(TABLE_USER, null, aUser);
            //endregion

            //region Category

            ContentValues eCategory = new ContentValues();
            eCategory.put(SQLiteHelper.COL_CATNAME, "Fuel");
            eCategory.put(SQLiteHelper.COL_CATYPE, CAT_EX);

            ContentValues iCategory = new ContentValues();
            iCategory.put(SQLiteHelper.COL_CATNAME, "Salary");
            iCategory.put(SQLiteHelper.COL_CATYPE, CAT_IN);

            db.insert(TABLE_CATEGORY, null, eCategory);
            db.insert(TABLE_CATEGORY, null, iCategory);

            //endregion

            //region Account

            ContentValues eAcc = new ContentValues();
            eAcc.put(SQLiteHelper.COL_ACCNAME, "Home");

            db.insert(TABLE_ACCOUNT, null, eAcc);

            //endregion

        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(context, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}