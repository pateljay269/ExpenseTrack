package patel.jay.exmanager.Utility;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;

import patel.jay.exmanager.Calender.DayFragment;
import patel.jay.exmanager.Calender.MonthFragment;
import patel.jay.exmanager.Calender.WeekFragment;
import patel.jay.exmanager.Calender.YearFragment;
import patel.jay.exmanager.R;

import static android.content.Intent.ACTION_VIEW;
import static android.net.Uri.parse;
import static android.os.Environment.getExternalStorageDirectory;
import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.widget.Toast.LENGTH_SHORT;
import static java.lang.System.currentTimeMillis;
import static patel.jay.exmanager.SQL.SQL.ALL;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

/**
 * Created by Jay on 23-07-2017.
 */

public class MyConst {

    //region Declaration
    public static final String day = "Day", month = "Month", week = "Week", year = "Year", Total = "Total";

    public static String accName = ALL;
    //endregion

    public static void toast(Context activity, String msg) {
        Toast.makeText(activity, msg, LENGTH_SHORT).show();
    }

    public static void error(Context activity, Exception e) {
        toast(activity, e.getMessage());
        e.printStackTrace();
    }

    public static void snack(View view, String msg) {
        Snackbar sb = Snackbar
                .make(view, msg, Snackbar.LENGTH_LONG);

        sb.setActionTextColor(Color.RED);
        View sbView = sb.getView();
        TextView tv = sbView.findViewById(android.support.design.R.id.snackbar_text);
        tv.setTextSize(20);
        tv.setTextColor(Color.YELLOW);

        sb.show();
    }

    public static File setPath() {
        TimeConvert tc = timeMilies(currentTimeMillis());

        File sd = getExternalStorageDirectory();
        File directory = new File(sd.getAbsolutePath() + tc.getFile_Path());
        if (!directory.isDirectory()) {
            directory.mkdirs();
        }
        return directory;
    }

    public static void hideKey(Context context) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showExcel(Activity activity, String csvFile) {
        Intent intent = null;
        String excel = "https://play.google.com/store/apps/details?id=com.microsoft.office.excel";
        try {

            File file = new File(setPath(), csvFile);
            Uri uri = Uri.fromFile(file);

            intent = new Intent(ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            activity.startActivity(intent);

        } catch (ActivityNotFoundException e) {
            intent = new Intent(ACTION_VIEW);
            intent.setData(parse(excel));
            activity.startActivity(intent);
        } catch (Exception e) {
            error(activity, e);
        }
    }

    public static View setExView(Activity context, LayoutInflater inflater, ViewGroup container, String sCalender) {
        View view = inflater.inflate(R.layout.fragment_calender, container, false);
        try {
            RecyclerView rvCat = view.findViewById(R.id.recyclerView);
            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));

            switch (sCalender) {
                case day:
                    DayFragment.setRv(context, rvCat);
                    break;

                case week:
                    WeekFragment.setRv(context, rvCat);
                    break;

                case month:
                    MonthFragment.setRv(context, rvCat);
                    break;

                case year:
                    YearFragment.setRv(context, rvCat);
                    break;
            }
        } catch (Exception e) {
            error(context, e);
        }
        return view;
    }
}