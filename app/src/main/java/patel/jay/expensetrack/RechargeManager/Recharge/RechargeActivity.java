package patel.jay.expensetrack.RechargeManager.Recharge;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalRechargeAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlRecharge;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    static RecyclerView recyclerView;
    static ArrayList<SqlRecharge> rechargeArrayList;
    static BalRechargeAdapter adapter;
    static TextView tvTotal;
    Spinner spnCust;
    FloatingActionButton fab;

    @SuppressLint("SetTextI18n")
    public static void setRv(Activity activity) {
        try {
            SqlRecharge sqlRecharge = SqlRecharge.totalWise(activity);
            String rec = sqlRecharge.getRecharge();
            String ince = sqlRecharge.getIncentive();
            int total = Integer.parseInt(rec) + Integer.parseInt(ince);
            tvTotal.setText(rec + " + " + ince + " = " + total);
        } catch (Exception e) {
            tvTotal.setText("No Data Found");
        }
        try {
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            rechargeArrayList = SqlRecharge.allDetails(activity, false);
            adapter = new BalRechargeAdapter(rechargeArrayList, activity);
            recyclerView.setAdapter(adapter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_find);

        setTitle("Recharge");

        spnCust = (Spinner) findViewById(R.id.spnCust);
        spnCust.setVisibility(View.GONE);

        tvTotal = (TextView) findViewById(R.id.tvTotal);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        fab = (FloatingActionButton) findViewById(R.id.fab);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab.setOnClickListener(this);
        setRv(this);
    }

    //region Action Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_category, menu);
        return true;
    }

    //endregion

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        try {
            int id = item.getItemId();

            if (id == R.id.action_home) {
                finish();
//                MyConstants.goToHomeRecharge(this);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        try {
            PopupMenu popup = new PopupMenu(RechargeActivity.this, fab);
            popup.getMenuInflater().inflate(R.menu.popup_recharge, popup.getMenu());

            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                public boolean onMenuItemClick(MenuItem item) {
                    String cType = "";
                    switch (item.getItemId()) {
                        case R.id.action_recharge:
                            cType = SQLiteHelper.COL_RECHARGE;
                            break;

                        case R.id.action_incentive:
                            cType = SQLiteHelper.COL_INCENTIVE;
                            break;
                    }

                    try {
                        SetRecLayout layout = new SetRecLayout(RechargeActivity.this);
                        layout.alertAddCat(cType);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return true;
                }
            });

            popup.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
