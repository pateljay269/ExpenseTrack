package patel.jay.expensetrack.ConstClass;

import android.annotation.SuppressLint;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Jay on 31-07-2017.
 */

public class TimeConvert implements Serializable {

    private int dd, mm, yy, hh, min, week;
    private String am_pm, DD_MM_YY, HH_Min_AP, MMM_YY, DD_MMM_YY, weekDay, DD_MM, dateTime, week_MMM_YY, YYYY_MM_DD, MMM, File_Path;

    public TimeConvert(int dd, int mm, int yy, int hh, int min, int week, String am_pm,
                       String DD_MM_YY, String HH_Min_AP, String MMM_YY, String DD_MMM_YY,
                       String weekDay, String DD_MM, String dateTime, String week_MMM_YY,
                       String YYYY_MM_DD, String MMM, String file_Path) {
        this.dd = dd;
        this.mm = mm;
        this.yy = yy;
        this.hh = hh;
        this.min = min;
        this.week = week;
        this.am_pm = am_pm;
        this.DD_MM_YY = DD_MM_YY;
        this.HH_Min_AP = HH_Min_AP;
        this.MMM_YY = MMM_YY;
        this.DD_MMM_YY = DD_MMM_YY;
        this.weekDay = weekDay;
        this.DD_MM = DD_MM;
        this.dateTime = dateTime;
        this.week_MMM_YY = week_MMM_YY;
        this.YYYY_MM_DD = YYYY_MM_DD;
        this.MMM = MMM;
        File_Path = file_Path;
    }

    //region Getter Setter

    public static TimeConvert timeMiliesConvert(long timeInMilies) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMilies);

        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);
        int hh = cal.get(Calendar.HOUR);
        int min = cal.get(Calendar.MINUTE);
        int ap = cal.get(Calendar.AM_PM);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        String am_pm = ap == 0 ? "AM" : "PM";

        String ddmmyy = MyConstants.numberOf(dd) + "-" + MyConstants.numberOf(mm + 1) + "-" + yy;
        String ddmmmyy = MyConstants.numberOf(dd) + "-" + MyConstants.monthName(mm) + "-" + yy;

        String ddmm = MyConstants.numberOf(dd) + " - " + MyConstants.numberOf(mm + 1);
        String mmmyy = MyConstants.monthName(mm) + " " + yy;

        String hhminap = MyConstants.numberOf(hh) + ":" + MyConstants.numberOf(min) + " " + am_pm;
        String datetime = ddmmyy + " " + hhminap;

        String week_MMM_YY = week + " " + mmmyy;
        String yymmdd = yy + MyConstants.numberOf(mm + 1) + MyConstants.numberOf(dd);

        String weekName = MyConstants.dayName(day);
        String mmm = MyConstants.monthName(mm);
//        String weekName=dayGujName(day);

        String file_path = "/ExpenseManager"
                + "/" + yy
                + "/" + MyConstants.numberOf(mm + 1) + " " + mmm
                + "/" + MyConstants.numberOf(dd);

        return new TimeConvert(dd, mm, yy, hh, min, week, am_pm, ddmmyy, hhminap, mmmyy, ddmmmyy,
                weekName, ddmm, datetime, week_MMM_YY, yymmdd, mmm, file_path);
    }

    public static String weekDayName(String date) {
        try {
            @SuppressLint("SimpleDateFormat") Date d = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            return MyConstants.dayGujName(day);
//            return new DateFormatSymbols().getWeekdays()[day];

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    public static String toDay() {
        TimeConvert tc = timeMiliesConvert(System.currentTimeMillis());
        return tc.getDD_MMM_YY();
    }

    public String getFile_Path() {
        return File_Path;
    }

    public String getMMM() {
        return MMM;
    }

    public String getYYYY_MM_DD() {
        return YYYY_MM_DD;
    }

    public String getWeek_MMM_YY() {
        return week_MMM_YY;
    }

    public int getWeek() {
        return week;
    }

    public int getDd() {
        return dd;
    }

    public int getMm() {
        return mm;
    }

    public int getYy() {
        return yy;
    }

    public int getHh() {
        return hh;
    }

    public int getMin() {
        return min;
    }

    public String getAm_pm() {
        return am_pm;
    }

    public String getDD_MM_YY() {
        return DD_MM_YY;
    }

    public String getHH_Min_AP() {
        return HH_Min_AP;
    }

    public String getMMM_YY() {
        return MMM_YY;
    }

    public String getDD_MMM_YY() {
        return DD_MMM_YY;
    }

    //endregion

    public String getWeekDay() {
        return weekDay;
    }

    public String getDD_MM() {
        return DD_MM;
    }

    public String getDateTime() {
        return dateTime;
    }

}