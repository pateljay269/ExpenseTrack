package patel.jay.exmanager.SQL;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import static patel.jay.exmanager.SQL.SQL.COL_CATNAME;
import static patel.jay.exmanager.SQL.SQL.COL_CID;
import static patel.jay.exmanager.SQL.SQL.TBL_CAT;
import static patel.jay.exmanager.SQL.SQL.TBL_INOUTCOME;
import static patel.jay.exmanager.Utility.MyConst.error;


/**
 * Created by Jay on 22-07-2017.
 */

public class Category {

    private int cid;
    private String catName;

    public Category() {
    }

    public Category(int cid, String catName) {
        this(catName);
        this.cid = cid;
    }

    public Category(String catName) {
        this.catName = catName;
    }

    public String catStr(Context context, int cid) {
        for (Category cat : allCat(context)) {
            if (cat.getCid() == cid) {
                return cat.getCatName();
            }
        }

        return "";
    }

    public int catId(Context context, String catName) {
        for (Category cat : allCat(context)) {
            if (cat.getCatName().equals(catName)) {
                return cat.getCid();
            }
        }

        return 0;
    }

    public ArrayList<Category> allCat(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] columns = {COL_CID, COL_CATNAME};

        Cursor cursor = db.query(TBL_CAT, columns, null, null, null, null, COL_CATNAME);
        ArrayList<Category> sqlCategories = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                sqlCategories.add(cursorToCategory(cursor));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sqlCategories;
    }

    public ArrayList<String> allCatStr(Context context) {

        ArrayList<String> sqlCategories = new ArrayList<>();
        sqlCategories.add("Category");

        for (Category cat : allCat(context)) {
            sqlCategories.add(cat.getCatName());
        }

        return sqlCategories;
    }

    public ArrayList<String> allCatStr(Context context, String accName) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] column_s = {COL_CID};
        int acId = new Account().accInt(context, accName);

        String selection = SQL.COL_ACID + "='" + acId + "'";

        Cursor cursor = db.query(TBL_INOUTCOME, column_s, selection, null, COL_CID, null, COL_CID);
        ArrayList<String> sqlCategories = new ArrayList<>();
        sqlCategories.add("Category");

        if (cursor.moveToFirst()) {
            do {
                int cid = cursor.getInt(cursor.getColumnIndex(COL_CID));
                sqlCategories.add(new Category().catStr(context, cid));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return sqlCategories;
    }

    private Category cursorToCategory(Cursor cursor) {
        try {
            int cid = cursor.getInt(cursor.getColumnIndex(COL_CID));
            String catname = cursor.getString(cursor.getColumnIndex(COL_CATNAME));

            return new Category(cid, catname);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public int getCid() {
        return cid;
    }

    public String getCatName() {
        return catName;
    }

    private ContentValues getValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_CATNAME, catName);
        return contentValues;
    }

    public long insert(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        long value = db.insert(TBL_CAT, null, getValues());
        db.close();
        return value;
    }

    public long update(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_CID + "='" + cid + "'";

        long value = 0;
        try {
            value = db.update(TBL_CAT, getValues(), selection, null);
        } catch (Exception e) {
            error(context, e);
        }

        db.close();
        return value;
    }

    public long delete(Context context) {
        SQL helper = new SQL(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = COL_CID + "='" + cid + "'";
        long value = 0;

        try {
            value = db.delete(TBL_CAT, selection, null);
        } catch (Exception e) {
            error(context, e);
        }
        db.close();
        return value;
    }

}
