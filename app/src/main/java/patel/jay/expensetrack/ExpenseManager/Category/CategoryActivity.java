package patel.jay.expensetrack.ExpenseManager.Category;

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

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fab;
    ViewPager viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);

        setTitle("Category");
        try {
            tabLayout = (TabLayout) findViewById(R.id.tablayout);
            viewPager = (ViewPager) findViewById(R.id.viewpager);
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(this);

            tabLayout.setupWithViewPager(viewPager);

            MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
            adapter.addFragment(new ECatFragment(), SQLiteHelper.CAT_EX);
            adapter.addFragment(new ICatFragment(), SQLiteHelper.CAT_IN);
            adapter.addFragment(new AccFragment(), SQLiteHelper.CAT_AC);

            viewPager.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
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
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    //endregion

    @Override
    public void onClick(View v) {
        try {
            String cType = "";
            switch (viewPager.getCurrentItem()) {
                case 0:
                    cType = SQLiteHelper.CAT_EX;
                    break;

                case 1:
                    cType = SQLiteHelper.CAT_IN;
                    break;

                case 2:
                    cType = SQLiteHelper.CAT_AC;
                    break;
            }

            SetExCatLayout setExCatLayout = new SetExCatLayout(this);
            setExCatLayout.alertAddCat(cType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
