package patel.jay.expensetrack.RechargeManager.FindTotal;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.DashBoard.HomeDashActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCalender;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExCategory;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExTrans;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderBal;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

public class PieChartActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    TextView tvName;
    Spinner spinner;
    ArrayList<String> strList;
    ArrayAdapter<String> spnAdapter;

    PieChart pieChart;
    PieDataSet dataSet;
    PieData data;

    ArrayList<String> strings;
    ArrayList<Entry> entries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        spinner = (Spinner) findViewById(R.id.spnName);
        tvName = (TextView) findViewById(R.id.tvName);
        tvName.setText("");

        pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setOnChartValueSelectedListener(this);
        pieChart.setUsePercentValues(true);
        pieChart.setDescription("Pie Chart");
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(40f);
        pieChart.setHoleRadius(40f);

    }

    private void setPieChart() {
        try {
            dataSet = new PieDataSet(entries, "");
            //Create Setting For TextSize,ColorTemplate
            dataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);

            data = new PieData(strings, dataSet);
            data.setValueTextSize(10f);
            data.setValueTextColor(Color.BLACK);
//            data.setValueFormatter(new DefaultValueFormatter(0));
            data.setValueFormatter(new PercentFormatter());

            pieChart.animateXY(1500, 1500);
            pieChart.setData(data);
        } catch (Exception e) {
            MyConstants.toast(this, e.getMessage());
        }
    }

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

            switch (id) {
                case R.id.action_home:
                    finish();
/*
                    switch (MyConstants.DASH_CLICK) {
                        case MyConstants.BAL:
                            MyConstants.goToHomeBalance(this);
                            break;
                        case MyConstants.RECHARGE:
                            MyConstants.goToHomeRecharge(this);
                            break;
                        case MyConstants.EXPENSE:
                            MyConstants.goToHomeExpense(this);
                            break;
                        default:
                            startActivity(new Intent(getApplicationContext(), HomeDashActivity.class));
                            break;
                    }
*/
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    @Override
    protected void onStart() {
        super.onStart();
        strings = new ArrayList<>();
        entries = new ArrayList<>();
        int entryNum = 0;
        final String[] str = new String[1];

        //region DASH_CLICK
        switch (MyConstants.DASH_CLICK) {
            case MyConstants.EXPENSE:
                //region Expense
                try {
                    strList = SqlExAccount.allAccString(this);
                    strList.set(0, SQLiteHelper.ALL);

                    spnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strList);
                    spinner.setAdapter(spnAdapter);

                    spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            str[0] = strList.get(position);
                            expense(str);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });

                    str[0] = spinner.getSelectedItem().toString();
                    expense(str);
                } catch (Exception e) {
                    MyConstants.toast(this, e.getMessage());
                }
                //endregion
                break;

            case MyConstants.BAL:
                //region Balance
                spinner.setVisibility(View.GONE);
                ArrayList<SqlBalAccount> balAccounts = SqlCalenderBal.totalWise(this);

                for (int i = 0; i < balAccounts.size(); i++) {
                    SqlBalAccount account = balAccounts.get(i);
                    int ex = Integer.parseInt(account.getExpense());
                    int in = Integer.parseInt(account.getIncome());
                    float amount = ex - in;
                    if (amount < 1) {
                        continue;
                    }
                    strings.add(SqlCustomer.mobileToName(this, account.getCustName()));
                    entries.add(new Entry(amount, entryNum++));
                }
                //endregion
                break;

            case MyConstants.RECHARGE:
                //region Recharge
                strList = SqlRecharge.allYearString(this);
                strList.set(0, SQLiteHelper.COL_YEAR);

                spnAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, strList);
                spinner.setAdapter(spnAdapter);

                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        str[0] = strList.get(position);
                        recharge(str);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                str[0] = spinner.getSelectedItem().toString();
                recharge(str);
                //endregion
                break;
        }
        //endregion

        setPieChart();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
        tvName.setText(strings.get(e.getXIndex()) + " " + e.getVal());
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onNothingSelected() {
        tvName.setText("");
    }

    private void recharge(String[] year) {
        strings = new ArrayList<>();
        entries = new ArrayList<>();
        int entryNum = 0;

        ArrayList<SqlRecharge> recharges = null;
        try {
            if (year[0].equals(SQLiteHelper.COL_YEAR)) {
                recharges = SqlRecharge.allDetails(this, true);
            } else {
                recharges = SqlRecharge.allDetailsYr(this, year[0]);
            }

            for (int i = 0; i < recharges.size(); i++) {
                SqlRecharge recharge = recharges.get(i);
                if (year[0].equals(SQLiteHelper.COL_YEAR)) {
                    strings.add(recharge.getYear());
                } else {
                    String month = recharge.getMonth();
                    month = month.substring(0, month.length() - 5);
                    strings.add(month);
                }
                int ex = Integer.parseInt(recharge.getRecharge());
                int in = Integer.parseInt(recharge.getIncentive());
                float amount = ex + in;
                entries.add(new Entry(amount, entryNum++));
            }
            setPieChart();
        } catch (Exception e) {
            MyConstants.toast(this, e.getMessage());
        }
    }

    private void expense(String[] accName) {
        strings = new ArrayList<>();
        entries = new ArrayList<>();
        int entryNum = 0;

        ArrayList<SqlExTrans> sqlExTrans = null;
        try {
            sqlExTrans = SqlExCalender.catWise(this, accName[0]);

            for (int i = 0; i < sqlExTrans.size(); i++) {
                SqlExTrans exTrans = sqlExTrans.get(i);
                int ex = Integer.parseInt(exTrans.getEx_rs());
                strings.add(SqlExCategory.categoryString(this, exTrans.getCid()));
                entries.add(new Entry(ex, entryNum++));
            }

            setPieChart();
        } catch (Exception e) {
            MyConstants.toast(this, e.getMessage());
        }
    }
}