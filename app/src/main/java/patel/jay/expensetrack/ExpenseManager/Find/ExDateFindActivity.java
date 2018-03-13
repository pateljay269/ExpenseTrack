package patel.jay.expensetrack.ExpenseManager.Find;

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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.Calendar;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.Adapters.ExTransAdapter;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

import static patel.jay.expensetrack.ConstClass.MyConstants.numberOf;

public class ExDateFindActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView rvDetail;
    static TextView tvTotal;
    static ArrayList<SqlExTrans> accountArrayList;
    static ExTransAdapter accountAdapter;
    static String sCatType;
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

    @SuppressLint("SetTextI18n")
    public static void setRv(Activity context) {
        try {
            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            accountArrayList = SqlExTrans.findWise(context, sCatType, Long.parseLong(dateS), Long.parseLong(dateE));//startTime, endTime);
            accountAdapter = new ExTransAdapter(accountArrayList, context);
            rvDetail.setAdapter(accountAdapter);

            tvTotal.setText(MyConstants.calExTotal(sCatType, accountArrayList));
            if (accountArrayList.size() == 0) {
                tvTotal.setText("No Data Found");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_transaction);
        setTitle("Date Wise Find");

        linearLayout = (LinearLayout) findViewById(R.id.linearFind);
        tvStart = (TextView) findViewById(R.id.tvStartDate);
        tvEnd = (TextView) findViewById(R.id.tvEndDate);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        btnFind = (ImageButton) findViewById(R.id.btnFind);
        sbtnCaType = (SwitchButton) findViewById(R.id.sbtnCaType);

        rvDetail = (RecyclerView) findViewById(R.id.recyclerView);
//        rvDetail.setLayoutManager(new LinearLayoutManager(this));
        fab = (FloatingActionButton) findViewById(R.id.fab);

        btnFind.setOnClickListener(this);
        tvStart.setOnClickListener(this);
        tvEnd.setOnClickListener(this);
        fab.setOnClickListener(this);

        sCatType = SQLiteHelper.CAT_EX;
        sbtnCaType.setText(SQLiteHelper.CAT_IN, SQLiteHelper.CAT_EX);
        sbtnCaType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sCatType = SQLiteHelper.CAT_IN;
                } else {
                    sCatType = SQLiteHelper.CAT_EX;
                }
                setRv(ExDateFindActivity.this);
            }
        });

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

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.trans_action, menu);
        return true;
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_add) {
                finish();
//                MyConstants.goToHomeExpense(this);
                return true;
            } else if (id == R.id.action_show) {
                if (linearLayout.getVisibility() == View.GONE) {
                    linearLayout.setVisibility(View.VISIBLE);
                } else {
                    linearLayout.setVisibility(View.GONE);
                }
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
                MyConstants.AddExpense(ExDateFindActivity.this);
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
//                    return;
                }
                setRv(this);
                if (startTime > endTime) {
                /*if (startTime < endTime) {
                    setRv(this);
                } else {*/
                    dateE = "1";
                    dateS = "2";
//                    setRv(this);
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
        return tyear + numberOf(tmonth) + numberOf(date);
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
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String temp = "";
                temp = dayOfMonth + " " + MyConstants.monthName(month) + " " + year;

                month++;

                date = year + numberOf(month) + numberOf(dayOfMonth);
                if (type) {
                    endTime = Integer.parseInt(date);
                    calendar.set(year, (month - 1), dayOfMonth, 0, 0, 0);
                    time2 = TimeConvert.timeMiliesConvert(calendar.getTimeInMillis());
                    dateE = time2.getYYYY_MM_DD();
                    tvEnd.setText(temp + "");
                } else {
                    startTime = Integer.parseInt(date);
                    calendar.set(year, (month), dayOfMonth, 0, 0, 0);
                    time1 = TimeConvert.timeMiliesConvert(calendar.getTimeInMillis());
                    dateS = startTime(dayOfMonth, month + 1, year);
                    tvStart.setText(temp + "");
                }
            }
        }, year, month, dayOfMonth).show();
    }

}