package patel.jay.expensetrack.RechargeManager.FindTotal;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;

import patel.jay.expensetrack.ConstClass.MyConstants;
import patel.jay.expensetrack.R;
import patel.jay.expensetrack.RechargeManager.Adapters.CalenderBalAdapter;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlBalAccount;
import patel.jay.expensetrack.RechargeManager.SQLBal.SqlCalenderBal;

public class TotalActivity extends AppCompatActivity {

    RecyclerView rvCat;
    FloatingActionButton fab;
    MaterialEditText etFind;
    ArrayList<SqlBalAccount> accountArrayList, tempArrayList;
    CalenderBalAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.floating_rv_layout);
        setTitle("Total Amount");

        try {
            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setVisibility(View.GONE);

            rvCat = (RecyclerView) findViewById(R.id.recyclerView);
//        rvCat.setLayoutManager(new LinearLayoutManager(getActivity()));
            etFind = (MaterialEditText) findViewById(R.id.etFind);
            etFind.setHint("Name");
            etFind.setFloatingLabelText("Name");
            etFind.setVisibility(View.VISIBLE);

            rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            accountArrayList = SqlCalenderBal.totalWise(this);
            tempArrayList = accountArrayList;
            adapter = new CalenderBalAdapter(accountArrayList, this, MyConstants.Total);
            rvCat.setAdapter(adapter);

            etFind.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //region TextChangedArrayList
                    try {
                        String cName = s + "";//etFind.getText().toString();
                        if (cName.isEmpty()) {
                            accountArrayList = tempArrayList;
                        } else {
                            //region Search
                            accountArrayList = new ArrayList<>();
                            for (SqlBalAccount sqlBalAccount : tempArrayList) {
                                if (sqlBalAccount.getCustName().contains(cName)) {
                                    accountArrayList.add(sqlBalAccount);
                                }
                            }
                            //endregion
                        }
                        rvCat.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                        adapter = new CalenderBalAdapter(accountArrayList, TotalActivity.this, MyConstants.Total);
                        rvCat.setAdapter(adapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //endregion
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
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
                    if (MyConstants.DASH_CLICK.equalsIgnoreCase(MyConstants.BAL)) {
                        MyConstants.goToHomeBalance(this);
                    } else if (MyConstants.DASH_CLICK.equalsIgnoreCase(MyConstants.RECHARGE)) {
                        MyConstants.goToHomeRecharge(this);
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

}
