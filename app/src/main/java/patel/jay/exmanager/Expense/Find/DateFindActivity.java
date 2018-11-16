package patel.jay.exmanager.Expense.Find;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.TransAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Trans;
import patel.jay.exmanager.Utility.TimeConvert;

import static android.support.v7.widget.StaggeredGridLayoutManager.VERTICAL;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.gc;
import static patel.jay.exmanager.Utility.MyConst.Total;
import static patel.jay.exmanager.Utility.MyConst.toast;
import static patel.jay.exmanager.Utility.TimeConvert.timeMilies;

public class DateFindActivity extends AppCompatActivity implements View.OnClickListener {

    private static TimeConvert timeStart, timeEnd;

    Activity activity = DateFindActivity.this;
    TextView tvStart, tvEnd;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Object obj : all) {
            obj = null;
        }

        gc();
        all = null;
    }

    private Object[] all = new Object[]{activity, timeEnd, timeStart};

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_trans);
        setTitle("Date Wise Find");

        tvStart = findViewById(R.id.tvStartDate);
        tvEnd = findViewById(R.id.tvEndDate);

        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);

        findViewById(R.id.btnFind).setOnClickListener(this);
        findViewById(R.id.fab).setVisibility(GONE);

        timeStart = timeMilies(currentTimeMillis());

        int month, dayOfMonth, year;

        dayOfMonth = timeStart.getDd();
        month = timeStart.getMm();
        year = timeStart.getYy();

        String temp = dayOfMonth + "-" + month + "-" + year;
        timeStart = timeMilies(temp, "00:00");

        temp = dayOfMonth + "-" + (month + 1) + "-" + year;
        timeEnd = timeMilies(temp, "23:59");

        tvStart.setText(timeStart.getDD_MMM_YY());
        tvEnd.setText(timeEnd.getDD_MMM_YY());

        setRv(activity);

    }

    @SuppressLint("SetTextI18n")
    public static void setRv(Activity context) {
        try {
            if (timeStart.getMyMillies() > timeEnd.getMyMillies()) {
                toast(context, "Please Select proper dates");
//                return;
            }
            ArrayList<Trans> list = new Trans(context).findWise(timeStart.getMyMillies(), timeEnd.getMyMillies());
            TransAdapter adapter = new TransAdapter(list, context);

            RecyclerView rvDetail = context.findViewById(R.id.recyclerView);
            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, VERTICAL));
            rvDetail.setAdapter(adapter);

            int ex = 0;
            String text = "";
            for (Trans trans : list) {
                ex += trans.getEx_rs();
            }
            text = Total + " : " + ex;

            if (list.size() == 0) {
                text = "No Data Found";
            }
            ((TextView) context.findViewById(R.id.tvTotal)).setText(text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trans_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_add) {
                finish();
            } else if (id == R.id.action_show) {
                LinearLayout linearLayout = findViewById(R.id.linearFind);
                if (linearLayout.getVisibility() == GONE) {
                    linearLayout.setVisibility(VISIBLE);
                } else {
                    linearLayout.setVisibility(GONE);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvStartDate:
                startTimeDialog();
                break;

            case R.id.tvEndDate:
                endTimeDialog();
                break;

            case R.id.btnFind:
                setRv(activity);
                break;
        }
    }

    private void startTimeDialog() {
        showDatePicker(timeStart.getDd(), timeStart.getMm(), timeStart.getYy(), false);
    }

    private void endTimeDialog() {
        showDatePicker(timeEnd.getDd(), timeEnd.getMm(), timeEnd.getYy(), true);
    }

    public void showDatePicker(int dayOfMonth, int month, int year, final boolean type) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "-" + (month + 1) + "-" + year;
                if (type) {
                    timeEnd = timeMilies(date, "23:59");
                    tvEnd.setText(timeEnd.getDD_MMM_YY());
                    setRv(activity);
                } else {
                    timeStart = timeMilies(date, "00:00");
                    tvStart.setText(timeStart.getDD_MMM_YY());
                    setRv(activity);
                }
            }
        }, year, month, dayOfMonth).show();
    }

}