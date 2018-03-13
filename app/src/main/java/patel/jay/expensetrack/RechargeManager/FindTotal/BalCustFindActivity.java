package patel.jay.expensetrack.RechargeManager.FindTotal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.ConstClass.SQLiteHelper;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.BalTransAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCustomer;
import patel.jay.expensetrack.RechargeManager.Transactions.SetBalTransLayout;

public class BalCustFindActivity extends AppCompatActivity {

    static RecyclerView rvDetail;
    static ArrayList<SqlBalAccount> accountArrayList;
    static BalTransAdapter accountAdapter;
    static TextView tvTotal;

    Spinner spnCust;
    ArrayList<String> custString;
    ArrayAdapter<String> spinnerArrayAdapter;
    FloatingActionButton fab;

    @SuppressLint("SetTextI18n")
    public static void setRv(Activity context, String custName) {
        try {
//            rvDetail.setLayoutManager(new LinearLayoutManager(context));
            rvDetail.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            String mobile = SqlCustomer.nameToMobile(context, custName);
            accountArrayList = SqlBalAccount.custWise(context, mobile);
            accountAdapter = new BalTransAdapter(accountArrayList, context);
            rvDetail.setAdapter(accountAdapter);

            int exRs = 0, inRs = 0;

            for (SqlBalAccount sqlBalAccount : accountArrayList) {
                exRs += Integer.parseInt(sqlBalAccount.getExpense());
                inRs += Integer.parseInt(sqlBalAccount.getIncome());
            }

            tvTotal.setText(exRs + " - " + inRs + " = " + (exRs - inRs));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cust_find);
        setTitle("Customer Wise Find");

        fab = (FloatingActionButton) findViewById(R.id.fab);
        spnCust = (Spinner) findViewById(R.id.spnCust);
        tvTotal = (TextView) findViewById(R.id.tvTotal);
        rvDetail = (RecyclerView) findViewById(R.id.recyclerView);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cType = SQLiteHelper.COL_EXPENSE;
                SetBalTransLayout setBalTransLayout =
                        new SetBalTransLayout(BalCustFindActivity.this, spnCust.getSelectedItem() + "");
                setBalTransLayout.alertAddCat(cType);
            }
        });

        fillDetails();
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
//                MyConstants.goToHomeBalance(this);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.onOptionsItemSelected(item);
    }

    private void fillDetails() {
        try {
            custString = SqlCustomer.allCustDataString(this);
            spinnerArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, custString);
            spnCust.setAdapter(spinnerArrayAdapter);

            spnCust.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    setRv(BalCustFindActivity.this, custString.get(position));
                    if (position == 0) {
                        tvTotal.setText("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            MyConstants.toast(BalCustFindActivity.this, e.getMessage());
        }
    }

}