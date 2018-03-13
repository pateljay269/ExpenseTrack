package patel.jay.expensetrack.ExpenseManager.SQLs;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.SQLiteHelper;


/**
 * Created by Jay on 22-07-2017.
 */

public class SqlExCategory implements Serializable {

    private static final String table = SQLiteHelper.TABLE_CATEGORY;
    private static final String[] columns = {SQLiteHelper.COL_CID, SQLiteHelper.COL_CATYPE, SQLiteHelper.COL_CATNAME};
    private static String selection = null;

    //region Getter Setter
    private static String[] selectionArgs = null;
    private static String groupBy = null;
    private static String having = null;

    //endregion
    private static String orderBy = null;
    private int cid;
    private String catName, caType;
    public SqlExCategory(int cid, String catName, String caType) {
        this(catName, caType);
        this.cid = cid;
    }
    public SqlExCategory(String catName, String caType) {
        this.catName = catName;
        this.caType = caType;
    }

    public static String categoryString(Activity context, int cid) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CID + "='" + cid + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        String catname = "";
        if (cursor.moveToFirst()) {
            catname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATNAME));
        }
        db.close();
        cursor.close();
        return catname;
    }

    public static int categoryInt(Activity context, String catName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATNAME + "='" + catName + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = null;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        int cid = 0;
        if (cursor.moveToFirst()) {
            cid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_CID));
        }
        db.close();
        cursor.close();
        return cid;
    }

    public static ArrayList<SqlExCategory> allCatType(Activity context, String cType) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CATYPE + "='" + cType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_CATNAME;

        Cursor cursor = db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<SqlExCategory> sqlCategories = new ArrayList<>();

        try {
            if (cursor.moveToFirst()) {
                do {
                    SqlExCategory sqlExCategory = cursorToCategory(cursor);
                    sqlCategories.add(sqlExCategory);
                } while (cursor.moveToNext());
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlCategories;
    }

    public static ArrayList<String> allCategoryString(Activity context, String caType) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] column_s = {SQLiteHelper.COL_CATNAME};
        selection = SQLiteHelper.COL_CATYPE + "='" + caType + "'";
        selectionArgs = null;
        groupBy = null;
        having = null;
        orderBy = SQLiteHelper.COL_CATNAME;

        Cursor cursor = db.query(table, column_s, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> sqlCategories = new ArrayList<>();
        sqlCategories.add("Category");

        if (cursor.moveToFirst()) {
            do {
                String catname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATNAME));
                sqlCategories.add(catname);
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return sqlCategories;
    }

    public static ArrayList<String> allCategoryString(Activity context, String caType, String accName) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String[] column_s = {SQLiteHelper.COL_CID};
        int acId = SqlExAccount.accInt(context, accName);
        selection = SQLiteHelper.COL_CATYPE + "='" + caType + "' AND " + SQLiteHelper.COL_ACID + "='" + acId + "'";
        selectionArgs = null;
        groupBy = SQLiteHelper.COL_CID;
        having = null;
        orderBy = SQLiteHelper.COL_CID;

        Cursor cursor = db.query(SQLiteHelper.TABLE_INOUTCOME, column_s, selection, selectionArgs, groupBy, having, orderBy);
        ArrayList<String> sqlCategories = new ArrayList<>();
        sqlCategories.add("Category");

        if (cursor.moveToFirst()) {
            do {
                int cid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_CID));
                sqlCategories.add(categoryString(context, cid));
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return sqlCategories;
    }

    private static SqlExCategory cursorToCategory(Cursor cursor) {
        int cid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_CID));
        String catname = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATNAME));
        String caType = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CATYPE));

        return new SqlExCategory(cid, catname, caType);
    }

    public int getCid() {
        return cid;
    }

    public String getCatName() {
        return catName;
    }

    public String getCaType() {
        return caType;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_CATNAME, catName);
        contentValues.put(SQLiteHelper.COL_CATYPE, caType);
        return contentValues;
    }

    public long insert(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(table, null, getValues());
        db.close();
        return value;
    }

    public long update(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CID + "='" + cid + "'";
        selectionArgs = null;

        long value = 0;
        try {
            value = db.update(table, getValues(), selection, selectionArgs);
        } catch (Exception e) {
            Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show();
        }

        db.close();
        return value;
    }

    public long delete(Activity context) {
        SQLiteHelper helper = new SQLiteHelper(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        selection = SQLiteHelper.COL_CID + "='" + cid + "'";
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
