package patel.jay.expensetrack.ConstClass;

import jxl.format.Alignment;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

/**
 * Created by Jay on 23-Dec-17.
 */

public class ExcelRows {

    public static void addHeadCol(WritableSheet sheet, int column, int row, String s) throws WriteException {
        WritableFont fontBold = new WritableFont(WritableFont.TIMES, 12, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE);
        WritableCellFormat cellBold = new WritableCellFormat(fontBold);
        cellBold.setAlignment(Alignment.CENTRE);
        cellBold.setWrap(false);

        Label label = new Label(column, row, s, cellBold);
        sheet.addCell(label);
    }

    public static void addSmplCol(WritableSheet sheet, int column, int row, String value) throws WriteException {
        WritableFont fontNormal = new WritableFont(WritableFont.TIMES, 12);
        WritableCellFormat cellNormal = new WritableCellFormat(fontNormal);
        cellNormal.setWrap(false);

        WritableCell number = new Label(column, row, value, cellNormal);
        sheet.addCell(number);
    }

    public static void addRsCol(WritableSheet sheet, int column, int row, String value) throws WriteException {
        WritableFont fontRs = new WritableFont(WritableFont.TIMES, 13);
        WritableCellFormat cellRs = new WritableCellFormat(fontRs);
        cellRs.setAlignment(Alignment.RIGHT);
        cellRs.setWrap(false);

        WritableCell number = new Label(column, row, value, cellRs);
        sheet.addCell(number);
    }


}
