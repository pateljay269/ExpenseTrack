package patel.jay.expensetrack.RechargeManager.FindTotal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalTransAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;

public class BalDateFindActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView rvDetail;
    static TextView tvTotal;
    static ArrayList<SqlBalAccount> accountArrayList;
    static BalTransAdapter adapter;
    static String dateS, dateE;
    FloatingActionButton fab;
    LinearLayout linearLayout;
    TextView tvStart, tvEnd;
    ImageButton btnFind;
    SwitchButton sbtnCaType;
    String date = "0";
    int startTime = 0, endTime = 0, month, dayOfMonth, year;
    Calendar calendar = Calendar.getInstance();
    TimeConvert time1, time2;

    public static void setRv(Activity context) {
        try {
            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            accountArrayList = SqlBalAccount.findWise(context, Long.parseLong(dateS), Long.parseLong(dateE));
            adapter = new BalTransAdapter(accountArrayList, context);
            rvDetail.setAdapter(adapter);
        } catch (Exception e) {
            MyConstants.toast(context, e.toString());
        }
    }

    public static void setRvUpdate(Activity context) {
        try {
            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            accountArrayList = SqlBalAccount.findWise(context, Long.parseLong(dateS), Long.parseLong(dateE));
            adapter = new BalTransAdapter(accountArrayList, context);
            rvDetail.setAdapter(adapter);
        } catch (Exception e) {
            MyConstants.toast(context, e.toString());
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_transaction);
        setTitle("Date Wise Find");

        sbtnCaType = (SwitchButton) findViewById(R.id.sbtnCaType);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        sbtnCaType.setVisibility(View.GONE);
        tvTotal.setVisibility(View.GONE);

        tvStart = (TextView) findViewById(R.id.tvStartDate);
        tvEnd = (TextView) findViewById(R.id.tvEndDate);
        btnFind = (ImageButton) findViewById(R.id.btnFind);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        linearLayout = (LinearLayout) findViewById(R.id.linearFind);

        fab.setOnClickListener(this);
        fab.setImageResource(R.drawable.ic_find);
        rvDetail = (RecyclerView) findViewById(R.id.recyclerView);
//        rvDetail.setLayoutManager(new LinearLayoutManager(this));

        btnFind.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);

        time1 = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
        time2 = TimeConvert.timeMiliesConvert(System.currentTimeMillis());

        dayOfMonth = time1.getDd();
        month = time1.getMm();
        year = time1.getYy();

        tvStart.setText(dayOfMonth + "-" + MyConstants.monthName(month - 1) + "-" + year);
        tvEnd.setText(time1.getDD_MMM_YY());

        dateE = time1.getYYYY_MM_DD();
        dateS = startTime(dayOfMonth, month + 1, year);

        setRv(this);
    }
    //endregion

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_home) {
                finish();
//                MyConstants.goToHomeBalance(this);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fab:
                if (linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
                break;

            case R.id.tvStartDate:
                startTimeDialog();
                break;

            case R.id.tvEndDate:
                endTimeDialog();
                break;

            case R.id.btnFind:
                if (endTime == 0) {
                    endTimeDialog();
                }
                if (startTime == 0) {
                    startTimeDialog();
                    return;
                }
                if (startTime < endTime) {
                    setRv(this);
                } else {
                    dateE = "1";
                    dateS = "2";
                    setRv(this);
                    MyConstants.toast(this, "Please Select proper dates");
                }
                break;
        }
    }

    private String startTime(int date, int month, int year) {
        int tmonth = month - 1;
        int tyear = year;
        if (tmonth == 0) {
            tmonth = 11;
            tyear = year - 1;
        }
        return tyear + "" + MyConstants.numberOf(tmonth) + "" + MyConstants.numberOf(date);
    }

    private void startTimeDialog() {
        int month = time1.getMm() - 1;
        int year = time1.getYy();
        if (month == 0) {
            month = 11;
            year = time1.getYy() - 1;
        }
        showDatePicker(time1.getDd(), month, year, false);

    }

    private void endTimeDialog() {
        showDatePicker(time2.getDd(), time2.getMm(), time2.getYy(), true);
    }

    public void showDatePicker(int dayOfMonth, int month, int year, final boolean type) {
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String temp = "";
                temp = dayOfMonth + " " + MyConstants.monthName(month) + " " + year;

                month++;

                date = year + "" + MyConstants.numberOf(month) + "" + MyConstants.numberOf(dayOfMonth);
                if (type) {
                    endTime = Integer.parseInt(date);
                    tvEnd.setText(temp + "");
                    calendar.set(year, (month - 1), dayOfMonth, 0, 0, 0);
                    time2 = TimeConvert.timeMiliesConvert(calendar.getTimeInMillis());
                    dateE = time2.getYy() + "" + MyConstants.numberOf(month) + "" + MyConstants.numberOf(dayOfMonth);
                } else {
                    startTime = Integer.parseInt(date);
                    tvStart.setText(temp + "");
                    calendar.set(year, (month), dayOfMonth, 0, 0, 0);
                    time1 = TimeConvert.timeMiliesConvert(calendar.getTimeInMillis());
                    dateS = startTime(dayOfMonth, month + 1, year);
                }
            }
        }, year, month, dayOfMonth).show();
    }

}