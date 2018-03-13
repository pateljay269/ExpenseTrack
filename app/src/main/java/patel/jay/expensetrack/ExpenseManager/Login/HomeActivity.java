package patel.jay.expensetrack.ExpenseManager.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.MyPagerAdapter;
import patel.jay.expensetrack.R;

public class HomeActivity extends AppCompatActivity {

    public static ViewPager viewPager;
    FloatingActionButton fab;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_tablayout);

        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setVisibility(View.GONE);

        tabLayout.setVisibility(View.GONE);

        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new SignInFragment(), MyConstants.signIn);
//        adapter.addFragment(new SignUpFragment(), MyConstants.signUp);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter(adapter);

    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dash_action, menu);
        try {
            menu.getItem(0).setIcon(R.drawable.ic_action_home);
            menu.getItem(2).setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_logout) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            } else if (id == R.id.action_info) {
                alertView();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    private void alertView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Username - password" +
                "\nUser - 1234" +
                "\nAdmin - 9902");
        builder.setTitle("Change it with use of Admin Password");
        builder.setCancelable(true);

        AlertDialog alert = builder.create();
        alert.show();
    }
    //endregion

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MyConstants.back2Login(this);
    }
}
