package patel.jay.expensetrack.DashBoard;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import patel.jay.expensetrack.Calender.CalenderActivity;
import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Customer.CustomersActivity;
import patel.jay.expensetrack.RechargeManager.FindTotal.BalCustFindActivity;
import patel.jay.expensetrack.RechargeManager.FindTotal.BalDateFindActivity;
import patel.jay.expensetrack.RechargeManager.FindTotal.PieChartActivity;
import patel.jay.expensetrack.RechargeManager.FindTotal.TotalActivity;
import patel.jay.expensetrack.RechargeManager.Transactions.BalTransactionActivity;

public class BalanceDashActivity extends AppCompatActivity implements View.OnClickListener {

    ImageButton btnInOut, btnCalender, btnFind, btnCustomer, btnTotal, btnPie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_dash_balance);
            setTitle("Dashboard");

            btnCalender = findViewById(R.id.btnCalender);
            btnCustomer = findViewById(R.id.btnCust);
            btnInOut = findViewById(R.id.btnInOut);
            btnFind = findViewById(R.id.btnFind);
            btnTotal = findViewById(R.id.btnTotal);
            btnPie = findViewById(R.id.btnPie);

            btnCalender.setOnClickListener(this);
            btnCustomer.setOnClickListener(this);
            btnInOut.setOnClickListener(this);
            btnFind.setOnClickListener(this);
            btnTotal.setOnClickListener(this);
            btnPie.setOnClickListener(this);
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
//                startActivity(new Intent(getApplicationContext(), HomeDashActivity.class));
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
        MyConstants.DASH_CLICK = MyConstants.BAL;

        switch (v.getId()) {
            case R.id.btnCalender:
                MyConstants.DASH_CLICK = MyConstants.BAL;
                startActivity(new Intent(getApplicationContext(), CalenderActivity.class));
                break;

            case R.id.btnCust:
                startActivity(new Intent(getApplicationContext(), CustomersActivity.class));
                break;

            case R.id.btnInOut:
                startActivity(new Intent(getApplicationContext(), BalTransactionActivity.class));
                break;

            case R.id.btnTotal:
                startActivity(new Intent(getApplicationContext(), TotalActivity.class));
                break;

            case R.id.btnPie:
                startActivity(new Intent(getApplicationContext(), PieChartActivity.class));
                break;

            case R.id.btnFind:
                PopupMenu popup = new PopupMenu(BalanceDashActivity.this, btnFind);
                popup.getMenuInflater().inflate(R.menu.popup_re_find, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_cust:
                                startActivity(new Intent(getApplicationContext(), BalCustFindActivity.class));
                                break;

                            case R.id.action_date:
                                startActivity(new Intent(getApplicationContext(), BalDateFindActivity.class));
                                break;
                        }
                        return true;
                    }
                });

                popup.show();
                break;
        }
    }

}
