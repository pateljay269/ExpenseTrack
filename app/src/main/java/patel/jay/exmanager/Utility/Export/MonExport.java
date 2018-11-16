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
import jxl.format.BorderLineStyle;
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
import static patel.jay.exmanager.Utility.TimeConvert.weekDayName;

/**
 * Created by Jay on 21-Dec-17.
 */

public class MonExport {

    private ExcelRows p;

    private onExport caller;

    private Activity activity;
    private int year;
    private Trans trans;

    public MonExport(Activity activity, int year) {
        this.activity = activity;
        this.year = year;
        trans = new Trans(activity);
        caller = (onExport) activity;
    }

    //region Export In SQLite To Excel
    public void write() throws IOException {
        String csvFile = year + " Month.xls";

        File file = new File(setPath(), csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = createWorkbook(file, wbSettings);

        new ExportDb(workbook, csvFile).execute();

    }

    //endregion

    @SuppressLint("StaticFieldLeak")
    private class ExportDb extends AsyncTask<String, String, String> {

        private WritableWorkbook workbook;
        private String fileName;

        public ExportDb(WritableWorkbook workbook, String fileName) {
            this.workbook = workbook;
            this.fileName = fileName;
        }

        private void createLabel(WritableSheet sheet) throws WriteException {
            int i = 0, col = 1;
            final String gujDsc = "માહિતી", gujEx = "જાવક",
                    OM = "શ્રી ગણેશાય નમઃ", sOM = "ૐ";

            sheet.mergeCells(col, i, 4, i);
            p.addHeadBrdCell(0, i, "   " + sOM, THICK, LEFT);
            p.addHeadBrdCell(col, i, OM, THICK, RIGHT);
            p.addHeadBrdCell(5, i++, sOM + "   ", THICK, RIGHT);
            i++;
            col = 0;
            p.addHeadBrdCell(col++, i, "No", THIN, CENTRE);

            p.addHeadBrdCell(col++, i, "Account", THIN, CENTRE);
            p.addHeadBrdCell(col++, i, "Category", THIN, CENTRE);
            p.addHeadCell(col, i - 1, "     ");
            p.addHeadBrdCell(col++, i, gujEx, THIN, CENTRE);
            p.addHeadBrdCell(col++, i, COL_TIME, THIN, CENTRE);
            p.addHeadBrdCell(col++, i, gujDsc, THIN, CENTRE);

            p.addSmplCell(0, 5, "Date");
        }

        @Override
        protected String doInBackground(String... strings) {
            try {

                //region Month Wise
                WritableSheet sheet = null;
                Cursor cursor = null;
                SQLiteDatabase db = new SQL(activity).getReadableDatabase();
                ArrayList<Integer> sMonth = trans.monthYearArray(year, false);

                //region If No Data Then File Deleted
                if (sMonth.size() == 0) {
                    File file = new File(setPath(), fileName);
                    file.delete();
                }
                //endregion

                for (int l = 0; l < sMonth.size(); l++) {
                    String month = new TimeConvert().monthGujName(sMonth.get(l));

                    //region New Sheet Page
                    if (sheet == null || !sMonth.get(l).equals(sMonth.get(0))) {
                        sheet = workbook.createSheet(month, l);
                        p = new ExcelRows(sheet);
                        createLabel(sheet);
                    }
                    //endregion

                    trans = trans.monthTotal(sMonth.get(l), year);
                    //region Define Total Of Page

                    int rowIndex = 0, eAmount = 0;
                    eAmount = trans.getEx_rs();

                    rowIndex = 4;
                    String[] str = new String[]{month, trans.getYear() + "", Total, eAmount + ""};
                    //here top border 0 to 5
                    for (int i = 0; i < str.length; i++) {
                        p.addHeadBrdCell(i, rowIndex, str[i], THIN, CENTRE);
                    }
                    //endregion

                    ArrayList<String> sDay = trans.dayMonthArray(sMonth.get(l), year);
                    rowIndex++;

                    for (int d = 0; d < sDay.size(); d++) {

                        String selection = COL_DATE + "=" + sDay.get(d) +
                                " And " + COL_MONTH + "=" + sMonth.get(l) +
                                " And " + COL_YEAR + "=" + year +
                                " And " + COL_EAMOUNT + " >0";

                        cursor = db.query(TBL_INOUTCOME, trans.columns, selection,
                                null, null, null, COL_TIME);

                        String date = sDay.get(d) + "-" + (sMonth.get(l) + 1) + "-" + year;

                        if (cursor.moveToFirst()) {
                            rowIndex++;

                            str = new String[]{sDay.get(d), weekDayName(date), "", "", "", ""};
                            //here top border 0 to 5
                            for (int i = 0; i < 6; i++) {
                                p.addHeadBrdCell(i, rowIndex, str[i], BorderLineStyle.DOTTED, CENTRE);
                            }
                            rowIndex++;
                            rowIndex = setRows(cursor, rowIndex);
                        }
                    }
                }
                //endregion
                workbook.write();
                workbook.close();

                if (cursor != null) {
                    cursor.close();
                }
                db.close();

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
                        int acid = cursor.getInt(cursor.getColumnIndex(COL_ACID));
                        String accName = new Account().accString(activity, acid);
                        String time = cursor.getString(cursor.getColumnIndex(COL_TIME));
                        TimeConvert tc = timeMilies(parseLong(time));

                        eAmount += ex_rs;

                        col = 0;
                        p.addRsCell(col++, rowIndex, k);
                        p.addSmplCell(col++, rowIndex, accName, true);
                        p.addSmplCell(col++, rowIndex, catName, true);
                        p.addRsCell(col++, rowIndex, ex_rs);
                        p.addSmplCell(col++, rowIndex, " " + tc.getHH_Min_AP() + " ", true);
                        p.addSmplCell(col++, rowIndex, description, false);
                        k++;

                        rowIndex++;
                        //endregion
                    } while (cursor.moveToNext());

                    //region Define Total Of Page

                    col = 2;

                    p.addSmplCell(col++, rowIndex, Total, true);
                    p.addHeadCell(col++, rowIndex, eAmount);

                    rowIndex++;
                    //endregion
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

//                String csvFile = year + " Month.xls";
//                new MyConst().showExcel(activity, csvFile);
            } else {
                msg = "Error In Export Data";
            }

            caller.onComplete("Month " + year);
            snack(activity.findViewById(R.id.linearMain), msg);
            activity = null;
        }
    }

}