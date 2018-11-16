package patel.jay.exmanager.Calender;

import android.app.Activity;
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
import android.widget.Spinner;

import java.util.ArrayList;

import patel.jay.exmanager.Adapter.PagerAdapter;
import patel.jay.exmanager.R;
import patel.jay.exmanager.SQL.Account;
import patel.jay.exmanager.SQL.SQL;

import static android.R.layout.simple_list_item_1;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static java.lang.System.gc;
import static patel.jay.exmanager.Utility.MyConst.accName;
import static patel.jay.exmanager.Utility.MyConst.day;
import static patel.jay.exmanager.Utility.MyConst.error;
import static patel.jay.exmanager.Utility.MyConst.month;
import static patel.jay.exmanager.Utility.MyConst.week;
import static patel.jay.exmanager.Utility.MyConst.year;

public class CalenderActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public static boolean isRepo = false;
    Activity activity = CalenderActivity.this;

    ArrayList<String> accString;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (Object obj : all) {
            obj = null;
        }

        gc();
        all = null;
    }

    private Object[] all = new Object[]{activity, accString};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);
        setTitle("Calender");

        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager viewPager = findViewById(R.id.viewpager);

        findViewById(R.id.linearColor).setVisibility(VISIBLE);
        findViewById(R.id.fab).setVisibility(GONE);

        Spinner spnAccount = findViewById(R.id.spnAcc);

        findViewById(R.id.linearAcc).setVisibility(VISIBLE);

        accString = new Account().allAccString(activity);
        accString.set(0, SQL.ALL);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(activity, simple_list_item_1, accString);
        spnAccount.setAdapter(adapter);
        spnAccount.setOnItemSelectedListener(this);

        tabLayout.setupWithViewPager(viewPager);

        setViewPager(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        try {
            RecyclerView rvCat = findViewById(R.id.recyclerView);
            rvCat.setLayoutManager(new LinearLayoutManager(activity));

            accName = accString.get(position);

            if (!isRepo) {
                YearFragment.setRv(activity, rvCat);
                WeekFragment.setRv(activity, rvCat);
            }
            MonthFragment.setRv(activity, rvCat);
            DayFragment.setRv(activity, rvCat);

            setViewPager(this);
        } catch (Exception e) {
            error(activity, e);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //endregion

    public static void setViewPager(AppCompatActivity activity) {
        PagerAdapter adapter = new PagerAdapter(activity.getSupportFragmentManager());

        adapter.addFragment(new DayFragment(), day);
        if (!isRepo) {
            adapter.addFragment(new WeekFragment(), week);
        }
        adapter.addFragment(new MonthFragment(), month);
        if (!isRepo) {
            adapter.addFragment(new YearFragment(), year);
        }

        ((ViewPager) activity.findViewById(R.id.viewpager)).setAdapter(adapter);
    }

}