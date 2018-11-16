package patel.jay.exmanager.Utility.Export;

import jxl.CellView;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.VerticalAlignment;
import jxl.write.Label;
import jxl.write.WritableCell;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WriteException;

import static jxl.format.Alignment.CENTRE;
import static jxl.format.Border.ALL;
import static jxl.format.Border.BOTTOM;
import static jxl.format.Border.TOP;
import static jxl.format.BorderLineStyle.DOTTED;
import static jxl.format.BorderLineStyle.THICK;
import static jxl.format.BorderLineStyle.THIN;
import static jxl.format.UnderlineStyle.NO_UNDERLINE;
import static jxl.write.WritableFont.BOLD;
import static jxl.write.WritableFont.TIMES;

/**
 * Created by Jay on 23-Dec-17.
 */

public class ExcelRows {

    public void addHeadBrdCell(int col, int row, String s, BorderLineStyle style, Alignment alignment) throws WriteException {
        WritableFont font = new WritableFont(TIMES, 12, BOLD, false, NO_UNDERLINE);
        WritableCellFormat format = new WritableCellFormat(font);

        int heightInPoints = 450;
        sheet.setRowView(row, heightInPoints);

        if (style == THIN) {
            format.setBorder(ALL, style);
        } else if (style == THICK) {
            if (col == 0) {
                format.setBorder(Border.LEFT, style);
            } else if (col == 5) {
                format.setBorder(Border.RIGHT, style);
            }
            format.setBorder(TOP, style);
            format.setBorder(BOTTOM, style);
        } else {

            format.setBorder(TOP, style);
            if (col == 0) {
                format.setBorder(Border.LEFT, style);
            } else if (col == 5) {
                format.setBorder(Border.RIGHT, style);
            }
            format.setBorder(BOTTOM, DOTTED);

        }
        format.setAlignment(alignment);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);
        format.setWrap(false);

        Label label = new Label(col, row, s, format);
        sheet.addCell(label);
        sheet = getFit(col);
    }

    public void addHeadCell(int col, int row, int s) throws WriteException {
        addHeadCell(col, row, s + "");
    }

    public void addHeadCell(int col, int row, String s) throws WriteException {
        WritableFont font = new WritableFont(TIMES, 12, BOLD, false, NO_UNDERLINE);
        WritableCellFormat format = new WritableCellFormat(font);

        format.setAlignment(CENTRE);
        format.setWrap(false);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);

        int heightInPoints = 350;
        sheet.setRowView(row, heightInPoints);

        Label label = new Label(col, row, s, format);

        sheet.addCell(label);
        sheet = getFit(col);
    }

    public void addSmplCell(int col, int row, String val, boolean isRight) throws WriteException {

        WritableFont font = new WritableFont(TIMES, 12);
        WritableCellFormat format = new WritableCellFormat(font);

        int heightInPoints = 350;
        sheet.setRowView(row, heightInPoints);

        Alignment align = isRight ? Alignment.RIGHT : Alignment.LEFT;
        format.setAlignment(align);
        format.setWrap(false);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);

        WritableCell number = new Label(col, row, val, format);
        sheet.addCell(number);
        sheet = getFit(col);
    }

    public void addSmplCell(int col, int row, String val) throws WriteException {

        WritableFont font = new WritableFont(TIMES, 9, BOLD);
        WritableCellFormat format = new WritableCellFormat(font);

        int heightInPoints = 350;
        sheet.setRowView(row, heightInPoints);

        format.setAlignment(Alignment.CENTRE);
        format.setWrap(false);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);

        WritableCell number = new Label(col, row, val, format);
        sheet.addCell(number);
        sheet = getFit(col);
    }

    public void addRsCell(int col, int row, int val) throws WriteException {
        addRsCell(col, row, val + "");
    }

    public void addRsCell(int col, int row, String val) throws WriteException {
        WritableFont font = new WritableFont(TIMES, 12);
        WritableCellFormat format = new WritableCellFormat(font);

        int heightInPoints = 350;
        sheet.setRowView(row, heightInPoints);

        format.setAlignment(CENTRE);
        format.setWrap(false);
        format.setVerticalAlignment(VerticalAlignment.CENTRE);

        WritableCell number = new Label(col, row, val + "", format);
        sheet.addCell(number);
        sheet = getFit(col);
    }

    private WritableSheet getFit(int col) {
        CellView cell = sheet.getColumnView(col);
        cell.setAutosize(true);
        sheet.setColumnView(col, cell);
        return sheet;
    }

    private WritableSheet sheet;

    public ExcelRows(WritableSheet sheet) {
        this.sheet = sheet;
    }
}
