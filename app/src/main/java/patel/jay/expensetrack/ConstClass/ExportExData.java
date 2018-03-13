package patel.jay.expensetrack.ConstClass;

import android.app.Activity;
import android.database.Cursor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;

import static patel.jay.expensetrack.ConstClass.ExcelRows.addHeadCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addRsCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addSmplCol;
import static patel.jay.expensetrack.ConstClass.MyConstants.toast;

/**
 * Created by Jay on 21-Dec-17.
 */

public class ExportExData {

    private int col = 0;

    private Activity activity;
    private String type;
    private int year;
    private String FILE_MONTH = "EX Month ", FILE_ACC = "EX Acc ", XLS = ".xls";

    public ExportExData(Activity activity, String type, int year) {
        this.activity = activity;
        this.type = type;
        this.year = year;
    }

    //region Export In SQLite To Excel
    public void write() throws IOException, WriteException {
        String csvFile = "";
        switch (type) {
            case MyConstants.Month:
                csvFile = FILE_MONTH + year + XLS;
                break;

            case MyConstants.Account:
                csvFile = FILE_ACC + year + XLS;
                break;
        }

        File file = new File(MyConstants.setPath(), csvFile);
        WorkbookSettings wbSettings = new WorkbookSettings();

        wbSettings.setLocale(new Locale("en", "EN"));

        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

        writingToExcel(workbook);
    }

    private void writingToExcel(WritableWorkbook workbook) {
        try {
            // don't create a sheet now, instead, pass it in the workbook
            createContent(workbook);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                workbook.write();
                workbook.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void createLabel(WritableSheet sheet) throws WriteException {
        int i = 0;
        col = 0;

        // Write a few headers
        addHeadCol(sheet, col, i++, MyConstants.OM);
        addHeadCol(sheet, col++, i, "No");
        addHeadCol(sheet, col++, i, SQLiteHelper.COL_TIME);

        switch (type) {
            case MyConstants.Month:
                addHeadCol(sheet, col++, i, MyConstants.Account);
                break;

            case MyConstants.Account:
                break;
        }
//        addHeadCol(sheet, col++, i, MyConstants.gujIn);        //For Income Details
        addHeadCol(sheet, col++, i, MyConstants.gujEx);
        addHeadCol(sheet, col++, i, MyConstants.Type);
        addHeadCol(sheet, col++, i, MyConstants.gujDsc);
    }

    private void createContent(WritableWorkbook workbook) throws WriteException {

        switch (type) {
            case MyConstants.Month:
                //region Month Wise
                ArrayList<String> sMonth = SqlExTrans.monthYearArray(activity, year);

                try {
                    int rowIndex = 0;
                    WritableSheet excelSheet = null;
                    Cursor cursor = null;

                    //region If No Data File Then Deleted
                    if (sMonth.size() == 0) {
                        File file = new File(MyConstants.setPath(), FILE_MONTH + year + XLS);
                        if (file.delete()) {
                            MyConstants.toast(activity, MyConstants.Month + " Have No Data");
                        }
                    }
                    //endregion

                    for (int l = 0; l < sMonth.size(); l++) {

                        //region New Sheet Page
                        if (excelSheet == null || !sMonth.get(l).equals(sMonth.get(0))) {
                            excelSheet = workbook.createSheet(sMonth.get(l), l);
                            createLabel(excelSheet);
                            rowIndex = 1;
                        }
                        //endregion

                        SqlExTrans sqlExTrans = SqlExTrans.monthTotal(activity, sMonth.get(l));
                        //region Define Total Of Page
                        String month = sqlExTrans.getMonth();
                        month = month.substring(0, month.length() - 5);
                        month = MyConstants.monthGujName(month);

                        col = 0;
                        int eAmount = 0, iAmount = 0;
                        eAmount = Integer.parseInt(sqlExTrans.getEx_rs());
                        iAmount = Integer.parseInt(sqlExTrans.getIn_rs());

                        rowIndex++;
                        addHeadCol(excelSheet, col++, rowIndex, month);
                        addHeadCol(excelSheet, col++, rowIndex, sqlExTrans.getYear());
                        addHeadCol(excelSheet, col++, rowIndex, MyConstants.Total);
//                        addRsCol(excelSheet, col++, rowIndex, iAmount + "");        //For Income Details
                        addRsCol(excelSheet, col++, rowIndex, "" + eAmount);
//                        addRsCol(excelSheet, col++, rowIndex, String.valueOf(iAmount - eAmount));        //For Income Details
                        rowIndex++;
                        //endregion

                        ArrayList<String> sDay = SqlExTrans.dayMonthArray(activity, sMonth.get(l));

                        for (int d = 0; d < sDay.size(); d++) {
                            rowIndex++;
                            cursor = SqlExTrans.allMonthCursor(activity, sDay.get(d));
                            addHeadCol(excelSheet, 0, rowIndex, sDay.get(d).substring(0, 2));
                            addHeadCol(excelSheet, 1, rowIndex++, TimeConvert.weekDayName(sDay.get(d)));
                            rowIndex = setRows(excelSheet, cursor, rowIndex, true);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(activity, e.getMessage());
                }
                //endregion
                break;

            case MyConstants.Account:
                //region Account Wise

                ArrayList<String> sAccounts = SqlExAccount.allAccString(activity);
                sAccounts.remove(0);
                try {
                    int rowIndex = 0;
                    WritableSheet excelSheet = null;
                    Cursor cursor = null;

                    //region If No Data File Then Deleted
                    if (sAccounts.size() == 0) {
                        File file = new File(MyConstants.setPath(), FILE_ACC + year + XLS);
                        if (file.delete()) {
                            MyConstants.toast(activity, MyConstants.Account + " Have No Data");
                        }
                    }
                    //endregion

                    for (int l = 0; l < sAccounts.size(); l++) {

                        //region New Sheet Page
                        if (excelSheet == null || !sAccounts.get(l).equals(sAccounts.get(0))) {
                            excelSheet = workbook.createSheet(sAccounts.get(l), l);
                            createLabel(excelSheet);
                            rowIndex = 1;
                        }
                        //endregion

                        try {
                            int tempAcid = SqlExAccount.accInt(activity, sAccounts.get(l));
                            ArrayList<String> sDay = SqlExTrans.dayYearArray(activity, tempAcid, year);
                            SqlExTrans sqlExTrans = SqlExTrans.monthYearTotal(activity, tempAcid, year);

                            //region Define Total Of Page
                            col = 1;
                            int eAmount = 0, iAmount = 0;
                            eAmount = Integer.parseInt(sqlExTrans.getEx_rs());
                            iAmount = Integer.parseInt(sqlExTrans.getIn_rs());

                            rowIndex++;
                            addHeadCol(excelSheet, col++, rowIndex, MyConstants.Total);
//                            addRsCol(excelSheet, col++, rowIndex, iAmount + "");        //For Income Details
                            addRsCol(excelSheet, col++, rowIndex, "" + eAmount);
//                            addRsCol(excelSheet, col++, rowIndex, String.valueOf(iAmount - eAmount));        //For Income Details
                            rowIndex++;
                            //endregion

                            for (int d = 0; d < sDay.size(); d++) {
                                rowIndex++;
                                cursor = SqlExTrans.allAccCursor(activity, tempAcid, sDay.get(d));
                                addHeadCol(excelSheet, 0, rowIndex, sDay.get(d).substring(0, 5));
                                addHeadCol(excelSheet, 1, rowIndex++, TimeConvert.weekDayName(sDay.get(d)));
                                rowIndex = setRows(excelSheet, cursor, rowIndex, false);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            workbook.removeSheet(l);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    toast(activity, e.getMessage());
                }

                //endregion
                break;
        }

    }

    private int setRows(WritableSheet excelSheet, Cursor cursor, int rowIndex, boolean type) {
        try {
            //k is For Excel Column Id
            int k = 1;
            int eAmount = 0, iAmount = 0;

            if (cursor.moveToFirst()) {
                do {
                    //region Define Row Data
                    // if the sheet has hit the cap, then create a new sheet, new label row and reset the row count
                    String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EAMOUNT));
                    String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_IAMOUNT));
                    String description = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));
                    int cid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_CID));
                    String catName = SqlExCategory.categoryString(activity, cid);
                    int acid = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COL_ACID));
                    String accName = SqlExAccount.accString(activity, acid);
                    String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
                    TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(time));

                    eAmount += Integer.parseInt(ex_rs);
                    iAmount += Integer.parseInt(in_rs);

                    col = 0;
                    addSmplCol(excelSheet, col++, rowIndex, String.valueOf(k));
                    addSmplCol(excelSheet, col++, rowIndex, tc.getHH_Min_AP());
                    if (type) {
                        addSmplCol(excelSheet, col++, rowIndex, accName);
                    }
//                    addRsCol(excelSheet, col++, rowIndex, in_rs);        //For Income Details
                    addRsCol(excelSheet, col++, rowIndex, ex_rs);
                    addSmplCol(excelSheet, col++, rowIndex, catName);
                    addSmplCol(excelSheet, col++, rowIndex, description);
                    k++;

                    // increment the sheet row
                    rowIndex++;
                    //endregion
                } while (cursor.moveToNext());

                //region Define Total Of Page
                col = 1;
                if (type) {
                    col++;
                }
                addHeadCol(excelSheet, col++, rowIndex, MyConstants.Total);
//                addRsCol(excelSheet, col++, rowIndex, iAmount + "");        //For Income Details
                addRsCol(excelSheet, col++, rowIndex, "" + eAmount);
//                addHeadCol(excelSheet, col++, rowIndex, String.valueOf(iAmount - eAmount));        //For Income Details
                rowIndex++;
                //endregion
            }
            cursor.close();
        } catch (WriteException e) {
            e.printStackTrace();
            toast(activity, e.getMessage());
        }
        return rowIndex;
    }

    //endregion

}