package patel.jay.expensetrack.ConstClass;

import android.app.Activity;
import android.database.Cursor;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.WritableCellFormat;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderBal;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;

import static patel.jay.expensetrack.ConstClass.ExcelRows.addHeadCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addRsCol;
import static patel.jay.expensetrack.ConstClass.ExcelRows.addSmplCol;

/**
 * Created by Jay on 23-Dec-17.
 */

public class ExportTotalBal {
    private final String FILE_BAL = "RM Total", XLS = ".xls";
    private WritableCellFormat cellBold, cellNormal, cellRs;
    private Activity activity;
    private String type;

    public ExportTotalBal(Activity activity, String type) {
        this.activity = activity;
        this.type = type;
    }

    //region Export In SQLite To Excel

    public void write() throws IOException, WriteException {
        String csvFile = "";
        switch (type) {
            case MyConstants.Total:
                csvFile = FILE_BAL + XLS;
                break;
        }

        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));

        File file = new File(MyConstants.setPath(), csvFile);
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

    private void createHeader(WritableSheet sheet) throws WriteException {
        int col = 0, i = 0;

        switch (type) {
            case MyConstants.Total:
                addHeadCol(sheet, col + 1, i++, MyConstants.OM);
                addHeadCol(sheet, col + 1, i++, TimeConvert.toDay());
                addHeadCol(sheet, col++, i, "No");
                addHeadCol(sheet, col++, i, MyConstants.Name);
                addHeadCol(sheet, col++, i, MyConstants.Total);
                break;
        }
    }

    private void createContent(WritableWorkbook workbook) throws WriteException {

        int rowIndex = 0;
        WritableSheet excelSheet = null;

        //region New Sheet
        if (excelSheet == null) {
            excelSheet = workbook.createSheet(MyConstants.Total, 0);
            createHeader(excelSheet);
            rowIndex = 1;
        }
        //endregion

        switch (type) {
            case MyConstants.Total:
                //region Total
                rowIndex += 2;
                Cursor cursor = SqlCalenderBal.totalCursor(activity);

                if (cursor.moveToFirst()) {
                    //k is For Excel Column Id
                    int k = 1;
                    rowIndex++;
                    do {
                        //region Define Row Data
                        String ex_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_EXPENSE));
                        String in_rs = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_INCOME));
                        String mobile = cursor.getString(cursor.getColumnIndex(SQLiteHelper.COL_CUSTMOBILE));
                        String cust = SqlCustomer.mobileToName(activity, mobile);
                        int i = 0, eAmount = 0, iAmount = 0;
                        ;
                        eAmount = Integer.parseInt(ex_rs);
                        iAmount = Integer.parseInt(in_rs);

                        addSmplCol(excelSheet, i++, rowIndex, String.valueOf(k++));
                        addSmplCol(excelSheet, i++, rowIndex, cust);
                        addRsCol(excelSheet, i++, rowIndex, String.valueOf(eAmount - iAmount));
                        rowIndex++;
                        //endregion
                    } while (cursor.moveToNext());
                }
                //endregion
                break;

        }
    }

    //endregion

}