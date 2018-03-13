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
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderBal;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

import static patel.jay.expensetrack.ConstClass.ExcelRows.addHeadCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addRsCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addSmplCol;

/**
 * Created by Jay on 02-08-2017.
 */

public class ExportBalData {

    private final String FILE_BAL = "RM Balance Info", FILE_REC = "RM Recharge History", FILE_TOT = "RM Total", XLS = ".xls";
    private Activity activity;
    private String type;

    public ExportBalData(Activity activity, String type) {
        this.activity = activity;
        this.type = type;
    }

    //region SQLite To Excel

    public void write() throws IOException, WriteException {
        String csvFile = "";
        switch (type) {
            case MyConstants.BAL:
                csvFile = FILE_BAL + XLS;
                break;

            case MyConstants.RECHARGE:
                csvFile = FILE_REC + XLS;
                break;

            case MyConstants.Total:
                csvFile = FILE_TOT + XLS;
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

        int col = 0, i = 0;

        //create array list of columns

        // Write a headers
        addHeadCol(sheet, col, i++, MyConstants.OM);

        switch (type) {
            case MyConstants.BAL:
                addHeadCol(sheet, col++, i, "No");
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_EXPENSE);
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_INCOME);
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_DATE);
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_TIME);
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_DSC);
                break;

            case MyConstants.RECHARGE:
                addHeadCol(sheet, col++, i, "No");
                addHeadCol(sheet, col++, i, "Date");
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_RECHARGE);
                addHeadCol(sheet, col++, i, SQLiteHelper.COL_INCENTIVE);
                addHeadCol(sheet, col++, i, MyConstants.Total);
                break;

            case MyConstants.Total:
                addHeadCol(sheet, col + 1, i++, TimeConvert.toDay());
                addHeadCol(sheet, col++, i, "No");
                addHeadCol(sheet, col++, i, MyConstants.Total);
                addHeadCol(sheet, col++, i, MyConstants.Name);
                break;
        }
    }

    private void createContent(WritableWorkbook workbook) throws WriteException {
        switch (type) {
            case MyConstants.BAL:
                //region Balance Data
                try {
                    ArrayList<String> sCustName = SqlCustomer.allCustDataString(activity);
                    sCustName.remove(0);
                    int rowIndex = 0;
                    WritableSheet excelSheet = null;
                    Cursor cursor = null;

                    //region If No Data File Then Deleted
                    if (sCustName.size() == 0) {
                        File file = new File(MyConstants.setPath(), FILE_BAL + XLS);
                        if (file.delete()) {
                            MyConstants.toast(activity, MyConstants.BAL + " Have No Data");
                        }
                    }
                    //endregion

                    for (int l = 0; l < sCustName.size(); l++) {
                        String mobile = SqlCustomer.nameToMobile(activity, sCustName.get(l));
                        cursor = SqlBalAccount.allDetailCursor(activity, mobile);

                        //region New Sheet
                        if (excelSheet == null || !sCustName.get(l).equals(sCustName.get(0))) {
                            excelSheet = workbook.createSheet(sCustName.get(l), l);
                            createLabel(excelSheet);
                            rowIndex = 1;
                        }
                        //endregion

                        if (cursor.moveToFirst()) {
                            //k is For Excel Column Id
                            int k = 1, eAmount = 0, iAmount = 0;
                            rowIndex++;
                            do {
                                //region Define Row Data
                                String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EXPENSE));
                                String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCOME));
                                String description = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DSC));
//                                String date = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_DATE));
                                String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
                                TimeConvert tc = TimeConvert.timeMiliesConvert(Long.parseLong(time));

                                eAmount += Integer.parseInt(ex_rs);
                                iAmount += Integer.parseInt(in_rs);
                                int i = 0;

                                addSmplCol(excelSheet, i++, rowIndex, String.valueOf(k++));
                                addRsCol(excelSheet, i++, rowIndex, ex_rs);
                                addRsCol(excelSheet, i++, rowIndex, in_rs);
                                addSmplCol(excelSheet, i++, rowIndex, tc.getDD_MMM_YY());
                                addSmplCol(excelSheet, i++, rowIndex, tc.getHH_Min_AP());
                                addSmplCol(excelSheet, i++, rowIndex, description);
                                rowIndex++;
                                //endregion
                            } while (cursor.moveToNext());

                            //region Define Total Of Page
                            int i = 0;
                            addHeadCol(excelSheet, i++, rowIndex, MyConstants.Total);
                            addRsCol(excelSheet, i++, rowIndex, eAmount + "");
                            addRsCol(excelSheet, i, rowIndex, iAmount + "");

                            rowIndex += 1;
                            i = 1;
                            addHeadCol(excelSheet, i++, rowIndex + 1, SQLiteHelper.COL_EXPENSE);
                            addRsCol(excelSheet, i, rowIndex + 1, eAmount + "");
                            i = 1;
                            addHeadCol(excelSheet, i++, rowIndex + 2, SQLiteHelper.COL_INCOME);
                            addRsCol(excelSheet, i, rowIndex + 2, "- " + iAmount);
                            i = 1;
                            addHeadCol(excelSheet, i++, rowIndex + 3, "Remain");
                            addRsCol(excelSheet, i, rowIndex + 3, String.valueOf(eAmount - iAmount));
                            //endregion
                        }
                        cursor.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //endregion
                break;

            case MyConstants.RECHARGE:
                //region Recharge Data
                try {
                    ArrayList<String> sYear = SqlRecharge.allYearString(activity);

                    sYear.remove(0);
                    int rowIndex = 0;
                    WritableSheet excelSheet = null;
                    Cursor cursor = null;

                    //region If No Data File Then Deleted
                    if (sYear.size() == 0) {
                        File file = new File(MyConstants.setPath(), FILE_REC + XLS);
                        if (file.delete()) {
                            MyConstants.toast(activity, MyConstants.RECHARGE + " Have No Data");
                        }
                    }
                    //endregion

                    for (int l = 0; l < sYear.size(); l++) {
                        //region New Sheet
                        if (excelSheet == null || !sYear.get(l).equals(sYear.get(0))) {
                            excelSheet = workbook.createSheet(sYear.get(l), l);
                            createLabel(excelSheet);
                            rowIndex = 1;
                        }
                        //endregion

                        //region Define Total Of Page
                        SqlRecharge sqlRecharge = SqlRecharge.yearWiseTotal(activity, sYear.get(l));
                        int yTotal = Integer.parseInt(sqlRecharge.getRecharge()) + Integer.parseInt(sqlRecharge.getIncentive());

                        int i = 1;
                        rowIndex++;

                        addHeadCol(excelSheet, i++, rowIndex, MyConstants.Total);
                        addRsCol(excelSheet, i++, rowIndex, sqlRecharge.getRecharge());
                        addRsCol(excelSheet, i++, rowIndex, "+ " + sqlRecharge.getIncentive());
                        addRsCol(excelSheet, i++, rowIndex, yTotal + "");
                        addSmplCol(excelSheet, i++, rowIndex, "Total Of Current Sheet");

                        rowIndex++;
                        //endregion

                        ArrayList<String> sMonth = SqlRecharge.monthArray(activity, sYear.get(l));
                        for (int j = 0; j < sMonth.size(); j++) {
                            //region Month Details
                            cursor = SqlRecharge.allDetailCursor(activity, sMonth.get(j));

                            //k is For Excel Column Id
                            int k = 1;
                            int eAmount = 0, iAmount = 0;

                            rowIndex++;
                            addHeadCol(excelSheet, 0, rowIndex, sMonth.get(j));

                            rowIndex++;
                            if (cursor.moveToFirst()) {
                                do {
                                    // if the sheet has hit the cap, then create a new sheet, new label row and reset the row count
                                    //region Define Row Data

                                    String recharge = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_RECHARGE));
                                    String incentive = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCENTIVE));
                                    String time = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_TIME));
                                    TimeConvert timeConvert = TimeConvert.timeMiliesConvert(Long.parseLong(time));

                                    eAmount += Integer.parseInt(recharge);
                                    iAmount += Integer.parseInt(incentive);
//                                    int rTotal = Integer.parseInt(recharge) + Integer.parseInt(incentive);
                                    i = 0;

                                    addSmplCol(excelSheet, i++, rowIndex, String.valueOf(k));
                                    addSmplCol(excelSheet, i++, rowIndex, timeConvert.getDD_MM() /*date.substring(0,5)*/);
                                    addRsCol(excelSheet, i++, rowIndex, recharge);
                                    addRsCol(excelSheet, i++, rowIndex, incentive);
//                                    addSmplCol(excelSheet, i++, rowIndex, rTotal + "/-");
                                    k++;
                                    //endregion

                                    rowIndex++; // increment the sheet row
                                } while (cursor.moveToNext());

                                //region Define Total Of Page
                                i = 1;
                                addHeadCol(excelSheet, i++, rowIndex, MyConstants.Total);
                                addRsCol(excelSheet, i++, rowIndex, eAmount + "");
                                addRsCol(excelSheet, i++, rowIndex, "+ " + iAmount);
                                addRsCol(excelSheet, i, rowIndex, String.valueOf(iAmount + eAmount));
                                rowIndex++;
                                //endregion
                            }

                            cursor.close();
                            //endregion
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //endregion
                break;

            case MyConstants.Total:
                //region Total
                int rowIndex = 0;
                WritableSheet excelSheet = null;

                //region New Sheet
                if (excelSheet == null) {
                    excelSheet = workbook.createSheet(MyConstants.Total, 0);
                    createLabel(excelSheet);
                    rowIndex = 1;
                }
                //endregion

                rowIndex += 2;
                Cursor cursor = SqlCalenderBal.totalCursor(activity);

                //region If No Data File Then Deleted
                if (cursor == null) {
                    File file = new File(MyConstants.setPath(), FILE_TOT + XLS);
                    if (file.delete()) {
                        MyConstants.toast(activity, MyConstants.Total + " Have No Data");
                    }
                }
                //endregion

                if (cursor.moveToFirst()) {
                    //region Define Row Data
                    //k is For Excel Column Id
                    int k = 1, total = 0;
                    rowIndex++;
                    do {
                        String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EXPENSE));
                        String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCOME));
                        String mobile = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));
                        String cust = SqlCustomer.mobileToName(activity, mobile);

                        int i = 0, eAmount = 0, iAmount = 0, tot = 0;

                        eAmount = Integer.parseInt(ex_rs);
                        iAmount = Integer.parseInt(in_rs);
                        tot = eAmount - iAmount;
                        total += tot;

                        addSmplCol(excelSheet, i++, rowIndex, String.valueOf(k++));
                        addRsCol(excelSheet, i++, rowIndex, tot + "");
                        addSmplCol(excelSheet, i++, rowIndex, cust);
                        rowIndex++;
                    } while (cursor.moveToNext());

                    //Total
                    rowIndex++;
                    addHeadCol(excelSheet, 0, rowIndex, MyConstants.Total);
                    addRsCol(excelSheet, 1, rowIndex, total + "");
                    //endregion
                }

                //endregion
                break;
        }
    }

    //endregion

}