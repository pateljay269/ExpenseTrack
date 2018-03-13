package patel.jay.expensetrack.RechargeManager.Transactions;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.MyPagerAdapter;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.R;

public class BalTransactionActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);
        setTitle("Transaction");

        try {
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);
            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            viewPager = (ViewPager) findViewById(R.id.viewpager);

            tabLayout.setupWithViewPager(viewPager);

            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new BalExFragment(), SQLiteHelper.COL_EXPENSE);
            adapter.addFragment(new BalInFragment(), SQLiteHelper.COL_INCOME);

            viewPager.setAdapter(adapter);

        } catch (Exception e) {
            e.printStackTrace();

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
    //endregion

    @Override
    public void onClick(View v) {
        String cType = "";
        switch (viewPager.getCurrentItem()) {
            case 0:
                cType = SQLiteHelper.COL_EXPENSE;
                break;

            case 1:
                cType = SQLiteHelper.COL_INCOME;
                break;
        }
//        MyConstants.toast(this,cType);
        SetBalTransLayout setBalTransLayout = new SetBalTransLayout(this);
        setBalTransLayout.alertAddCat(cType);
    }


}
