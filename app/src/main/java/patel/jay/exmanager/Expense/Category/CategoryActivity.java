package patel.jay.exmanager.Expense.Category;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import patel.jay.exmanager.Adapter.PagerAdapter;
import patel.jay.exmanager.R;

import static java.lang.System.gc;
import static patel.jay.exmanager.SQL.SQL.CAT_AC;
import static patel.jay.exmanager.SQL.SQL.COL_EXPENSE;

public class CategoryActivity extends AppCompatActivity implements View.OnClickListener {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);

        setTitle("Category");

        viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tablayout);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(this);

        tabLayout.setupWithViewPager(viewPager);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AccFragment(), CAT_AC);
        adapter.addFragment(new ECatFragment(), COL_EXPENSE);

        viewPager.setAdapter(adapter);
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

    @Override
    public void onClick(View v) {
        try {
            String cType = "";
            switch (viewPager.getCurrentItem()) {
                case 0:
                    cType = CAT_AC;
                    break;

                case 1:
                    cType = COL_EXPENSE;
                    break;
            }

            CatDialog catDialog = new CatDialog(this);
            catDialog.alertAddCat(cType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        gc();
    }
}
