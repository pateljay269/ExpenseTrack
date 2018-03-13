package patel.jay.expensetrack.ConstClass;

import android.app.Activity;
import android.content.Intent;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import patel.jay.expensetrack.DashBoard.BalanceDashActivity;
import patel.jay.expensetrack.DashBoard.ExpenseDashActivity;
import patel.jay.expensetrack.DashBoard.RechargeDashActivity;
import patel.jay.expensetrack.ExpenseManager.AllTransaction.SetExTransLayout;
import patel.jay.expensetrack.ExpenseManager.Login.LoginActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;

/**
 * Created by Jay on 23-07-2017.
 */

public class MyConstants {

    //region Declaration
    public static final String[] SPECIAL_CHAR = {"/", "\\", "[", "]", "*", ":", "?"};
    public static final String[] NUM_CHAR = {" ", "+91", "*122*", "#"};

    public static final int PICK_CONTACT = 26;
    public static final String F_URL = "https://www.facebook.com/pateljay269";
    public static final String EMAILID = "pateljay269@yahoo.com";
    public static final String MOBILENO = "09714151262";
    public static final String day = "Day", month = "Month", week = "Week", year = "Year",
            find = "find", signIn = "Login", signUp = "Register", ADMIN = "Admin", cust = "cust",
            Account = "Account", BAL = "Balance", RECHARGE = "Recahrge", EXPENSE = "Expense",
            Total = "Total", Month = "Month", Type = "Type", Name = "Name";
    public static final String gujEx = "ખર્ચ", gujIn = "આવક", gujDsc = "માહિતી", OM = "!! ૐ શ્રી ગણેશાય નમઃ !!";
    public static String DASH_CLICK = "";

    //endregion
    public static String accName = SQLiteHelper.ALL;
    public static String frgSelect = "";

    public static void toast(Activity activity, String msg) {
        Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
    }

    //region Return String
    public static String numberOf(int number) {
        if (number >= 0 && number <= 9) {
            return "0" + number;
        } else {
            return "" + number;
        }
    }

    public static String dayName(int dayOfWeek) {
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

    public static String weekSuper(int day) {
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
        return superScript;
    }

    public static String monthName(int month) {
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

    public static String dayGujName(int dayOfWeek) {
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

    public static String monthGujName(String month) {
        String monthName = "";
        switch (month) {
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

    public static String calExTotal(String type, ArrayList<SqlExTrans> arrayList) {
        int i = 0;
        int e = 0;
        String text = "";
        for (SqlExTrans sqlExTrans : arrayList) {
            i += Integer.parseInt(sqlExTrans.getEx_rs());
            e += Integer.parseInt(sqlExTrans.getIn_rs());

            switch (type) {
                case SQLiteHelper.CAT_EX:
                    text = "Total : " + i;
                    break;

                case SQLiteHelper.CAT_IN:
                    text = "Total : " + e;
                    break;
            }
        }
        return text;
    }

    //endregion

    //region Return Void
    public static void hideSoftKeyboard(Activity activity) {
        try {
//            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void AddExpense(Activity activity) {
        String cType = SQLiteHelper.CAT_EX;
//                String cType = SQLiteHelper.CAT_IN;

        SetExTransLayout setExTransLayout = new SetExTransLayout(activity);
        setExTransLayout.alertAddCat(cType);
    }

    public static void goToHomeExpense(Activity activity) {
        activity.finish();
        activity.startActivity(new Intent(activity, ExpenseDashActivity.class));
    }

    public static void goToHomeBalance(Activity activity) {
        activity.startActivity(new Intent(activity, BalanceDashActivity.class));
    }

    public static void goToHomeRecharge(Activity activity) {
        activity.startActivity(new Intent(activity, RechargeDashActivity.class));
    }

    public static void backClick(Activity activity) {
        MyConstants.DASH_CLICK = "";
        /*
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        activity.finish();
        activity.startActivity(intent);
//        activity.finishAndRemoveTask();
        */
        activity.finishAffinity();
        System.exit(0);
    }

    public static void back2Login(Activity activity) {
        activity.finish();
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }

    //endregion

    //region Export DB
    public static File setPath() {
        TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

        File sd = Environment.getExternalStorageDirectory();
        File directory = new File(sd.getAbsolutePath() + tc.getFile_Path());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static void createBackup(Activity context) {
        String inFile, outFile;

        outFile = setPath() + "/" + SQLiteHelper.DBNAME + ".db";
        inFile = context.getDatabasePath(SQLiteHelper.DBNAME).toString();
        //"/data/data/patel.jay.sqltesting/databases/DB_Expense.db"

        try {

            FileInputStream fis = new FileInputStream(new File(inFile));

            // Open the empty db as the output stream
            OutputStream output = new FileOutputStream(outFile);

            // Transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }

            // Close the streams
            output.flush();
            output.close();
            fis.close();
            MyConstants.toast(context, "Database Exported");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //endregion

    //region Copy Db
    public static void copyFile(File src, File dst) throws IOException {
        FileInputStream fis = new FileInputStream(src);
        FileOutputStream fos = new FileOutputStream(dst);
        byte[] buffer = new byte[1024];

        int content;
        while ((content = fis.read(buffer)) > 0) {
            fos.write(buffer, 0, content);
        }

        fis.close();
        fos.close();
    }
    //endregion

}