package patel.jay.expensetrack.ExpenseManager.AllTransaction;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Spinner;

import com.kyleduo.switchbutton.SwitchButton;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.MyPagerAdapter;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.ConstClass.TimeConvert;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;

public class ExTransactionActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    Spinner spnYear, spnMonth;
    ArrayList<String> monthString, yearString;
    ArrayAdapter<String> monthAdapter, yearAdapter;
    /*static */ String sCatType, monthNum = "", yearNum = "";
    boolean sOrder = false;
    FloatingActionButton fab;
    SwitchButton sbtnOrder;
    ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout linearMonthYear;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);

        setTitle("Transaction");
        try {
            linearMonthYear = (LinearLayout) findViewById(R.id.linearMonthYear);
            linearMonthYear.setVisibility(View.GONE);

            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);

            sbtnOrder = (SwitchButton) findViewById(R.id.sbtnOrder);
            spnMonth = (Spinner) findViewById(R.id.spnMonth);
            spnYear = (Spinner) findViewById(R.id.spnYear);
            sbtnOrder.setOnCheckedChangeListener(this);
            tabLayout.setupWithViewPager(viewPager);
//            tabLayout.setVisibility(View.GONE);

            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new ExOutFragment(), SQLiteHelper.CAT_EX);
            adapter.addFragment(new ExInFragment(), SQLiteHelper.CAT_IN);

            viewPager.setAdapter(adapter);

            sbtnOrder.setText("Amount", "Date");

            fillYear();

            spnYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    yearNum = yearString.get(position);
                    fillMonth();
                    spnMonth.setSelection(0);
//                        monthNum = monthString.get(position);
                    setFragmentList();
                }
            });

            spnMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    monthNum = monthString.get(position);
                    setFragmentList();
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

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
//                MyConstants.goToHomeExpense(this);
                return true;
            } else if (id == R.id.action_show) {
                if (linearMonthYear.getVisibility() == View.GONE) {
                    linearMonthYear.setVisibility(View.VISIBLE);
                    item.setTitle("Hide");

                    yearNum = 0 + "";
                    monthNum = "";
                } else {
                    linearMonthYear.setVisibility(View.GONE);
                    item.setTitle("Show");

                    TimeConvert tc = TimeConvert.timeMiliesConvert(System.currentTimeMillis());
                    yearNum = tc.getYy() + "";
                    String month = tc.getMMM_YY();
                    month = month.substring(0, month.length() - 5);
                    monthNum = month;
                }
                setFragmentList();
                return true;
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
            case R.id.fab:
                String cType = "";
                if (viewPager.getCurrentItem() == 0)
                    cType = SQLiteHelper.CAT_EX;
                else if (viewPager.getCurrentItem() == 1)
                    cType = SQLiteHelper.CAT_IN;

                SetExTransLayout setExTransLayout = new SetExTransLayout(this);
                setExTransLayout.alertAddCat(cType);
                break;
        }
    }

    private void fillYear() {
        switch (tabLayout.getSelectedTabPosition()) {
            case 0:
                sCatType = SQLiteHelper.CAT_EX;
                break;

            case 1:
                sCatType = SQLiteHelper.CAT_IN;
                break;
        }

        yearString = SqlExTrans.yearArray(this);
        yearString.set(0, SQLiteHelper.ALL);
        yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearString);
        spnYear.setAdapter(yearAdapter);

        fillMonth();
    }

    private void fillMonth() {
        monthString = SqlExTrans.monthYear(this, yearNum, sCatType, SQLiteHelper.ALL);
        monthString.set(0, SQLiteHelper.ALL);
        monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, monthString);
        spnMonth.setAdapter(monthAdapter);
    }

    private void setFragmentList() {
        try {
            if (!monthNum.equals(SQLiteHelper.ALL) && !yearNum.equals(SQLiteHelper.ALL)) {
                ExInFragment.setArrayList(ExTransactionActivity.this, Integer.parseInt(yearNum), monthNum, sOrder);
                ExOutFragment.setArrayList(ExTransactionActivity.this, Integer.parseInt(yearNum), monthNum, sOrder);
            } else if (!yearNum.equals(SQLiteHelper.ALL)) {
                ExInFragment.setArrayList(ExTransactionActivity.this, Integer.parseInt(yearNum), "", sOrder);
                ExOutFragment.setArrayList(ExTransactionActivity.this, Integer.parseInt(yearNum), "", sOrder);
            } else {
                ExInFragment.setArrayList(ExTransactionActivity.this, 0, "", sOrder);
                ExOutFragment.setArrayList(ExTransactionActivity.this, 0, "", sOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(this, e.getMessage());
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        sOrder = isChecked;
        setFragmentList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}