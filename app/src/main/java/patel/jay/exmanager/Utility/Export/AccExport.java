package patel.jay.exmanager.Utility.Export;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.Category;
import patel.jay.exmanager.SQL.SQL;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static java.lang.Long.parseLong;
import static jxl.Workbook.createWorkbook;
import static jxl.format.Alignment.CENTRE;
import static jxl.format.Alignment.LEFT;
import static jxl.format.Alignment.RIGHT;
import static jxl.format.BorderLineStyle.THICK;
import static jxl.format.BorderLineStyle.THIN;
import static patel.jay.exmanager.SQL.SQL.COL_ACID;
import static patel.jay.exmanager.SQL.SQL.COL_CID;
import static patel.jay.exmanager.SQL.SQL.COL_DATE;
import static patel.jay.exmanager.SQL.SQL.COL_DSC;
import static patel.jay.exmanager.SQL.SQL.COL_EAMOUNT;
import static patel.jay.exmanager.SQL.SQL.COL_MONTH;
import static patel.jay.exmanager.SQL.SQL.COL_TIME;
import static patel.jay.exmanager.SQL.SQL.COL_YEAR;
import static patel.jay.exmanager.SQL.SQL.TBL_INOUTCOME;
import static patel.jay.exmanager.Utility.MyConst.Total;
import static patel.jay.exmanager.Utility.MyConst.setPath;
import static patel.jay.exmanager.Utility.MyConst.snack;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

/**
 * Created by Jay on 21-Dec-17.
 */

public class AccExport {

    private ExcelRows p;

    private onExport caller;

    private Activity activity;
    private int year;

    public AccExport(Activity activity, int year) {
        this.activity = activity;
        this.year = year;
        caller = (onExport) activity;
    }

    //region Export In SQLite To Excel
    public void write() throws IOException {
        String csvFile = year + " Accounts.xls";

        File file = new File(setPath(), csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = createWorkbook(file, wbSettings);

        new ExportDb(workbook, activity, csvFile, year).execute();

    }

    //endregion

    @SuppressLint("StaticFieldLeak")
    private class ExportDb extends AsyncTask<String, String, String> {

        private WritableWorkbook workbook;
        private String fileName;
        private Activity activity;
        private int year;

        public ExportDb(WritableWorkbook workbook, Activity activity, String fileName, int year) {
            this.workbook = workbook;
            this.activity = activity;
            this.fileName = fileName;
            this.year = year;
        }

        private void createLabel(WritableSheet sheet) throws WriteException {
            int i = 0, col = 1;
            final String gujEx = "જાવક", gujDsc = "માહિતી",
                    OM = "શ્રી ગણેશાય નમઃ", sOM = "ૐ";

            sheet.mergeCells(col, i, 3, i);
            p.addHeadBrdCell(0, i, sOM, THICK, LEFT);
            p.addHeadBrdCell(col, i, OM, THICK, RIGHT);
            p.addHeadBrdCell(4, i++, sOM, THICK, RIGHT);
            i++;
            col = 0;
            p.addHeadBrdCell(col++, i, "No", THIN, CENTRE);
            p.addHeadBrdCell(col++, i, "Category", THIN, CENTRE);
            p.addHeadCell(col, i - 1, "     ");
            p.addHeadBrdCell(col++, i, gujEx, THIN, CENTRE);
            p.addHeadBrdCell(col++, i, COL_TIME, THIN, CENTRE);
            p.addHeadBrdCell(col++, i, gujDsc, THIN, CENTRE);
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                int rowIndex = 0, col = 0;
                WritableSheet sheet = null;
                Cursor cursor = null;

                SQLiteDatabase db = new SQL(activity).getReadableDatabase();
                //region Account Wise

                ArrayList<String> sAccounts = new Account().allAccString(activity);
                sAccounts.remove(0);

                //region If No Data File Then Deleted
                if (sAccounts.size() == 0) {
                    File file = new File(setPath(), fileName);
                    file.delete();
                }
                //endregion

                for (int l = 0; l < sAccounts.size(); l++) {

                    //region New Sheet Page
                    if (sheet == null || !sAccounts.get(l).equals(sAccounts.get(0))) {
                        sheet = workbook.createSheet(sAccounts.get(l), l);
                        p = new ExcelRows(sheet);
                        createLabel(sheet);
                        rowIndex = 2;
                    }
                    //endregion

                    try {
                        int tempAcid = new Account().accInt(activity, sAccounts.get(l));
                        Trans ex = new Trans(activity);
                        ArrayList<TimeConvert> sDay = ex.dayYearArray(tempAcid, year);
                        ex = ex.monthYearTotal(tempAcid, year);

                        //region Define Total Of Page
                        col = 1;
                        int eAmount = 0;
                        eAmount = ex.getEx_rs();

                        rowIndex++;
                        p.addHeadCell(col++, rowIndex, Total);
                        p.addHeadCell(col++, rowIndex, eAmount);
                        rowIndex++;
                        //endregion

                        for (int d = 0; d < sDay.size(); d++) {
                            rowIndex++;

                            String selection = COL_ACID + "=" + tempAcid +
                                    " AND " + COL_DATE + "=" + sDay.get(d).getDd() +
                                    " And " + COL_MONTH + "=" + sDay.get(d).getMm() +
                                    " And " + COL_YEAR + "=" + sDay.get(d).getYy();

                            cursor = db.query(TBL_INOUTCOME, ex.columns, selection, null, null, null, COL_TIME);

                            p.addHeadCell(0, rowIndex, sDay.get(d).getDD_MM());
                            p.addHeadCell(1, rowIndex++, sDay.get(d).getWeekGujDay());
                            rowIndex = setRows(cursor, rowIndex);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        workbook.removeSheet(l);
                    }
                }
                //endregion

                db.close();
                workbook.write();
                workbook.close();

                workbook = null;
                return "1";
            } catch (Exception e) {
                e.printStackTrace();
                return "0";
            }
        }

        private int setRows(Cursor cursor, int rowIndex) {
            try {
                int k = 1, col = 0;
                int eAmount = 0;

                if (cursor.moveToFirst()) {
                    do {
                        //region Define Row Data
                        int ex_rs = cursor.getInt(cursor.getColumnIndex(COL_EAMOUNT));
                        String description = cursor.getString(cursor.getColumnIndex(COL_DSC));
                        int cid = cursor.getInt(cursor.getColumnIndex(COL_CID));
                        String catName = new Category().catStr(activity, cid);
                        String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
                        TimeConvert tc = timeMilies(parseLong(time));

                        eAmount += ex_rs;

                        col = 0;
                        p.addRsCell(col++, rowIndex, k);
                        p.addSmplCell(col++, rowIndex, catName, true);
                        p.addRsCell(col++, rowIndex, ex_rs);
                        p.addSmplCell(col++, rowIndex, " " + tc.getHH_Min_AP() + " ", true);
                        p.addSmplCell(col++, rowIndex, description, false);
                        k++;

                        rowIndex++;
                        //endregion
                    } while (cursor.moveToNext());

                    //region Define Total Of Page

                    col = 1;

                    p.addHeadCell(col++, rowIndex, Total);
                    p.addHeadCell(col++, rowIndex, eAmount);

                    rowIndex++;
                    //endregion

                    rowIndex++;

                }
                cursor.close();
            } catch (WriteException e) {
                e.printStackTrace();
            }
            return rowIndex;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            boolean isDone = s.equals("1");
            String msg;

            if (isDone) {
                msg = "Expense Data Exported";

//                String csvFile = year + " Accounts.xls";
//                new MyConst().showExcel(activity, csvFile);
            } else {
                msg = "Error In Export Data";
            }

            caller.onComplete("Account " + year);
            snack(activity.findViewById(R.id.linearMain), msg);

            activity = null;
        }
    }

}