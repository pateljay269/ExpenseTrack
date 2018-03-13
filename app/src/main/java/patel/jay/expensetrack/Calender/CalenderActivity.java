package patel.jay.expensetrack.Calender;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.MyPagerAdapter;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.DashBoard.HomeDashActivity;
import patel.jay.expensetrack.ExpenseManager.SQLs.SqlExAccount;
import patel.jay.expensetrack.R;

public class CalenderActivity extends AppCompatActivity {

    public /*static*/ ViewPager viewPager;
    TabLayout tabLayout;
    LinearLayout linearColor, linearAcc;

    String toast;
    ArrayList<String> accString;
    ArrayAdapter<String> accAdapter;
    //    private String accNum = "";
    Spinner spnAccount;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);
        setTitle("Calender");

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        linearColor = (LinearLayout) findViewById(R.id.linearColor);
        linearColor.setVisibility(View.VISIBLE);
        linearAcc = (LinearLayout) findViewById(R.id.linearAcc);

        if (MyConstants.DASH_CLICK.equals(MyConstants.EXPENSE)) {
            try {
                spnAccount = (Spinner) findViewById(R.id.spnAcc);
                linearAcc.setVisibility(View.VISIBLE);
                fillAcc();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (MyConstants.DASH_CLICK.equals(MyConstants.RECHARGE)) {
            TextView tvBlue = (TextView) findViewById(R.id.tvBlue);
            TextView tvRed = (TextView) findViewById(R.id.tvRed);
            tvBlue.setText("Incentive");
            tvRed.setText("Recharge");
        }

        tabLayout.setupWithViewPager(viewPager);

        setViewPager();
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
/*                    switch (MyConstants.DASH_CLICK) {
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
                    }*/
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    private void fillAcc() {
        accString = SqlExAccount.allAccString(this);
        accString.set(0, SQLiteHelper.ALL);

        accAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accString);
        spnAccount.setAdapter(accAdapter);

        spnAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    RecyclerView rvCat = (RecyclerView) findViewById(R.id.recyclerView);
                    rvCat.setLayoutManager(new LinearLayoutManager(CalenderActivity.this));

                    MyConstants.accName = accString.get(position);

                    YearFragment.setRv(CalenderActivity.this, rvCat);
                    MonthFragment.setRv(CalenderActivity.this, rvCat);
                    WeekFragment.setRv(CalenderActivity.this, rvCat);
                    DayFragment.setRv(CalenderActivity.this, rvCat);

                    setViewPager();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new DayFragment(), MyConstants.day);
        if (MyConstants.DASH_CLICK.equals(MyConstants.EXPENSE))
            adapter.addFragment(new WeekFragment(), MyConstants.week);
        adapter.addFragment(new MonthFragment(), MyConstants.month);
        adapter.addFragment(new YearFragment(), MyConstants.year);

        viewPager.setAdapter(adapter);
    }
}