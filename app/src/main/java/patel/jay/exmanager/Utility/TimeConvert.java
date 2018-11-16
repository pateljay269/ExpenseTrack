package patel.jay.exmanager.Utility;

import android.annotation.SuppressLint;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import patel.jay.exmanager.SQL.SQL;

/**
 * Created by Jay on 31-07-2017.
 */

public class TimeConvert {

    private int dd, mm, yy, hh, min, week, hh24;
    private long myMillies;
    private String am_pm, DD_MM_YY, HH_Min_AP, MMM_YY, DD_MMM_YY, weekDay, weekGujDay, DD_MM, dateTime, week_MMM_YY, YYYY_MM_DD, MMM, File_Path;

    public static TimeConvert timeMilies(long timeInMilies) {
        TimeConvert tc = new TimeConvert();

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMilies);

        int dd = cal.get(Calendar.DAY_OF_MONTH);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        int mm = cal.get(Calendar.MONTH);
        int yy = cal.get(Calendar.YEAR);
        int hh = cal.get(Calendar.HOUR);
        int H = cal.get(Calendar.HOUR_OF_DAY);
        int min = cal.get(Calendar.MINUTE);
        int ap = cal.get(Calendar.AM_PM);
        int week = cal.get(Calendar.WEEK_OF_MONTH);
        String am_pm = ap == 0 ? "AM" : "PM";

        String ddmmyy = tc.numberOf(dd) + "-" + tc.numberOf(mm + 1) + "-" + yy;
        String ddmmmyy = tc.numberOf(dd) + "-" + tc.monthName(mm) + "-" + yy;

        String ddmm = tc.numberOf(dd) + " - " + tc.numberOf(mm + 1);
        String mmmyy = tc.monthName(mm) + " " + yy;

        String hhminap = tc.numberOf(H) + ":" + tc.numberOf(min) + " " + am_pm;
        String datetime = ddmmyy + " " + hhminap;

        String week_MMM_YY = week + " " + mmmyy;
        String yymmdd = yy + tc.numberOf(mm + 1) + tc.numberOf(dd);

        String weekName = tc.dayName(day);
        String weekGujDay = tc.dayGujName(day);
        String mmm = tc.monthName(mm);

        String file_path = "/Control_Money/MoneyManager/" + "tmp";

        return new TimeConvert(dd, mm, yy, hh, H, min, week, am_pm, ddmmyy, hhminap, mmmyy, ddmmmyy,
                weekName, weekGujDay, ddmm, datetime, week_MMM_YY, yymmdd, mmm, file_path, timeInMilies);
    }

    @SuppressLint("SimpleDateFormat")
    public static TimeConvert timeMilies(String ddmmyy, String HMin) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy H:mm:ss");
            String dateInString = ddmmyy + " " + HMin + ":00";
            Date date = sdf.parse(dateInString);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);


            return timeMilies(calendar.getTimeInMillis());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String weekDayName(String date) {
        TimeConvert tc = new TimeConvert();
        try {
            @SuppressLint("SimpleDateFormat") Date d = new SimpleDateFormat("dd-MM-yyyy").parse(date);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            int day = cal.get(Calendar.DAY_OF_WEEK);
            return tc.dayGujName(day);

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }

    }

    //region Constructor & Getters

    public TimeConvert() {
    }

    public TimeConvert(int dd, int mm, int yy, int hh, int hh24, int min, int week, String am_pm,
                       String DD_MM_YY, String HH_Min_AP, String MMM_YY, String DD_MMM_YY,
                       String weekDay, String weekGujDay, String DD_MM, String dateTime, String week_MMM_YY,
                       String YYYY_MM_DD, String MMM, String file_Path, long myMillies) {
        this.dd = dd;
        this.mm = mm;
        this.yy = yy;
        this.hh = hh;
        this.hh24 = hh24;
        this.min = min;
        this.week = week;
        this.am_pm = am_pm;
        this.DD_MM_YY = DD_MM_YY;
        this.HH_Min_AP = HH_Min_AP;
        this.MMM_YY = MMM_YY;
        this.DD_MMM_YY = DD_MMM_YY;
        this.weekDay = weekDay;
        this.weekGujDay = weekGujDay;
        this.DD_MM = DD_MM;
        this.dateTime = dateTime;
        this.week_MMM_YY = week_MMM_YY;
        this.YYYY_MM_DD = YYYY_MM_DD;
        this.MMM = MMM;
        File_Path = file_Path;
        this.myMillies = myMillies;
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

    public String getWeekGujDay() {
        return weekGujDay;
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

    public int getHh24() {
        return hh24;
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

    public long getMyMillies() {
        return myMillies;
    }

    public String getWeekDay() {
        return weekDay;
    }

    public String getDD_MM() {
        return DD_MM;
    }

    public String getDateTime() {
        return dateTime;
    }

    @Override
    public String toString() {
        return getDateTime();
    }

    //endregion

    public String numberOf(int number) {
        if (number >= 0 && number <= 9) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    public String dayName(int dayOfWeek) {
        String weekDay = "";
        switch (dayOfWeek) {
            case 1:
                weekDay = "SUN";
                break;
            case 2:
                weekDay = "MON";
                break;
            case 3:
                weekDay = "TUE";
                break;
            case 4:
                weekDay = "WED";
                break;
            case 5:
                weekDay = "THU";
                break;
            case 6:
                weekDay = "FRI";
                break;
            case 7:
                weekDay = "SAT";
                break;
        }
        return weekDay;
    }

    public String weekSuper(int day) {
        String superScript;
        switch (day) {
            case 1:
            case 21:
            case 31:
                superScript = "st";
                break;
            case 2:
            case 22:
                superScript = "nd";
                break;
            case 3:
            case 23:
                superScript = "rd";
                break;
            default:
                superScript = "th";
                break;

        }
        return day + "" + superScript;
    }

    public String monthName(int month) {
        String monthName = "";
        switch (month) {
            case 0:
                monthName = "JAN";
                break;
            case 1:
                monthName = "FEB";
                break;
            case 2:
                monthName = "MAR";
                break;
            case 3:
                monthName = "APR";
                break;
            case 4:
                monthName = "MAY";
                break;
            case 5:
                monthName = "JUN";
                break;
            case 6:
                monthName = "JUL";
                break;
            case 7:
                monthName = "AUG";
                break;
            case 8:
                monthName = "SEP";
                break;
            case 9:
                monthName = "OCT";
                break;
            case 10:
                monthName = "NOV";
                break;
            case 11:
                monthName = "DEC";
                break;
        }
        return monthName;
    }

    public String dayGujName(int dayOfWeek) {
        String weekDay = "";
        switch (dayOfWeek) {
            case 1:
                weekDay = "રવિ";
                break;
            case 2:
                weekDay = "સોમ";
                break;
            case 3:
                weekDay = "મંગળ";
                break;
            case 4:
                weekDay = "બુધ";
                break;
            case 5:
                weekDay = "ગુરૂ";
                break;
            case 6:
                weekDay = "શુક્ર";
                break;
            case 7:
                weekDay = "શનિ";
                break;
        }
        return weekDay + "વાર";
    }

    public String monthGujName(int month) {
        String monthName = "";
        switch (monthName(month)) {
            case "JAN":
                monthName = "જાન્યુઆરી";
                break;
            case "FEB":
                monthName = "ફેબ્રુઆરી";
                break;
            case "MAR":
                monthName = "માર્ચ";
                break;
            case "APR":
                monthName = "એપ્રિલ";
                break;
            case "MAY":
                monthName = "મે";
                break;
            case "JUN":
                monthName = "જૂન";
                break;
            case "JUL":
                monthName = "જુલાઈ";
                break;
            case "AUG":
                monthName = "ઑગસ્ટ";
                break;
            case "SEP":
                monthName = "સપ્ટેમ્બર";
                break;
            case "OCT":
                monthName = "ઑક્ટોબર";
                break;
            case "NOV":
                monthName = "નવેમ્બર";
                break;
            case "DEC":
                monthName = "ડિસેમ્બર";
                break;
        }
        return monthName;
    }

    public int monthNum(String mName) {
        DateFormat fmt = new SimpleDateFormat("MMM", Locale.US);
        Calendar cal = Calendar.getInstance();
        try {
            if (mName.equals(SQL.COL_MONTH) || mName.equals("")) {
                return -1;
            }
            Date d = fmt.parse(mName);
            cal.setTimeInMillis(d.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }

        return cal.get(Calendar.MONTH);
    }

//    public int month(String month) {
//        int mon = 0;
//        switch (month) {
//            case "JAN":
//                mon = 0;
//                break;
//            case "FEB":
//                mon = 1;
//                break;
//            case "MAR":
//                mon = 2;
//                break;
//            case "APR":
//                mon = 3;
//                break;
//            case "MAY":
//                mon = 4;
//                break;
//            case "JUN":
//                mon = 5;
//                break;
//            case "JUL":
//                mon = 6;
//                break;
//            case "AUG":
//                mon = 7;
//                break;
//            case "SEP":
//                mon = 8;
//                break;
//            case "OCT":
//                mon = 9;
//                break;
//            case "NOV":
//                mon = 10;
//                break;
//            case "DEC":
//                mon = 11;
//                break;
//            default:
//                mon = -1;
//                break;
//        }
//        return mon;
//    }

}